package mx.edu.utez.uxvibe.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.uxvibe.model.Evaluador;
import mx.edu.utez.uxvibe.model.dao.EvaluadorDao;
import mx.edu.utez.uxvibe.utils.EmailSender;
import mx.edu.utez.uxvibe.utils.PasswordUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Controlador para el proceso de recuperación de contraseña olvidada.
 * Flujo:
 * 1. El usuario solicita restablecer contraseña indicando su correo.
 * 2. Se genera un token UUID con vigencia de 60 min y se envía por correo vía EmailSender.
 * 3. El usuario hace clic en el enlace, se valida el token y puede ingresar una nueva contraseña.
 */
@WebServlet(name = "RecuperarServlet", value = "/recuperar")
public class RecuperarServlet extends HttpServlet {

    private final EvaluadorDao evaluadorDao = new EvaluadorDao();

    // Regla de complejidad de contraseña
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");

    /**
     * Petición GET: Valida el token que viene en la URL (?token=UUID).
     * Si es válido, lo lleva a cambiar-contra.jsp; si no, redirige con error.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        if (token != null && !token.trim().isEmpty()) {
            Evaluador evaluador = evaluadorDao.buscarPorToken(token);
            if (evaluador != null) {
                req.setAttribute("token", token);
                req.getRequestDispatcher("cambiar-contra.jsp").forward(req, resp);
                return;
            }
        }
        resp.sendRedirect("recuperar-contra.jsp?error=" + URLEncoder.encode("El enlace de recuperación es inválido o expiró.", StandardCharsets.UTF_8));
    }

    /**
     * Petición POST: Enruta la acción ("solicitar" enlace o "actualizar" contraseña).
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        if ("solicitar".equals(action)) {
            solicitarEnlace(req, resp);
        } else if ("actualizar".equals(action)) {
            actualizarContrasena(req, resp);
        } else {
            resp.sendRedirect("login.jsp");
        }
    }

    /**
     * Paso 1: Genera un token único y envía el correo con la plantilla HTML.
     */
    private void solicitarEnlace(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String correo = req.getParameter("correo");

        if (correo != null && !correo.trim().isEmpty()) {
            String correoNormalizado = correo.trim().toLowerCase();
            Evaluador evaluador = evaluadorDao.buscarPorCorreo(correoNormalizado);

            if (evaluador != null) {
                // Generamos token aleatorio único
                String token = UUID.randomUUID().toString();
                evaluadorDao.guardarTokenRecuperacion(token, evaluador.getIdEvaluador(), 60);

                // Construimos la URL completa para restablecer la contraseña
                String baseUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
                String enlaceRestablecer = baseUrl + "/recuperar?token=" + token;

                String contenidoHtml = EmailSender.generarPlantillaRecuperacion(enlaceRestablecer);

                try {
                    EmailSender.sendMail(correoNormalizado, "Recuperar tu contraseña de UXVibe", contenidoHtml);
                    resp.sendRedirect("correo-enviado.jsp?correo=" + URLEncoder.encode(correoNormalizado, StandardCharsets.UTF_8));
                } catch (Exception e) {
                    System.err.println("[RecuperarServlet] Error al enviar correo de recuperación: " + e.getMessage());
                    resp.sendRedirect("recuperar-contra.jsp?error=" + URLEncoder.encode("Error al enviar el correo: " + e.getMessage(), StandardCharsets.UTF_8));
                }
            } else {
                resp.sendRedirect("recuperar-contra.jsp?error=" + URLEncoder.encode("No se encontró ninguna cuenta registrada con ese correo.", StandardCharsets.UTF_8));
            }
        } else {
            resp.sendRedirect("recuperar-contra.jsp");
        }
    }

    /**
     * Paso 2: Valida las contraseñas nuevas, comprueba el token y actualiza el hash en la base de datos.
     */
    private void actualizarContrasena(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String token = req.getParameter("token");
        String pass1 = req.getParameter("p1");
        String pass2 = req.getParameter("p2");

        if (pass1 == null || pass2 == null || !pass1.equals(pass2)) {
            redirectError(resp, token, "Las contraseñas no coinciden.");
            return;
        }

        if (!PASSWORD_PATTERN.matcher(pass1).matches()) {
            redirectError(resp, token, "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número.");
            return;
        }

        Evaluador evaluador = evaluadorDao.buscarPorToken(token);
        if (evaluador == null) {
            redirectError(resp, token, "El token de recuperación no es válido o ya fue utilizado.");
            return;
        }

        // Hasheamos la nueva contraseña antes de persistirla
        String nuevoHash = PasswordUtil.hashPassword(pass1);
        boolean actualizado = evaluadorDao.actualizarContrasena(evaluador.getIdEvaluador(), nuevoHash);

        if (actualizado) {
            evaluadorDao.eliminarToken(token); // Invalidamos el token usado
            resp.sendRedirect("contra-actualizada.jsp");
        } else {
            redirectError(resp, token, "Ocurrió un error al actualizar la contraseña en la base de datos.");
        }
    }

    /**
     * Helper para redirigir a la pantalla de cambio de contraseña con mensaje de error codificado en la URL.
     */
    private void redirectError(HttpServletResponse resp, String token, String mensaje) throws IOException {
        String tokenEnc = token != null ? URLEncoder.encode(token, StandardCharsets.UTF_8) : "";
        String msgEnc = URLEncoder.encode(mensaje, StandardCharsets.UTF_8);
        resp.sendRedirect("cambiar-contra.jsp?token=" + tokenEnc + "&error=" + msgEnc);
    }
}

