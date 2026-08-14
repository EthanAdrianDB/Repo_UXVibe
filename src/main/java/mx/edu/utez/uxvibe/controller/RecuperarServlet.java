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

@WebServlet(name = "RecuperarServlet", value = "/recuperar")
public class RecuperarServlet extends HttpServlet {

    private final EvaluadorDao evaluadorDao = new EvaluadorDao();

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");

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

    private void solicitarEnlace(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String correo = req.getParameter("correo");

        if (correo != null && !correo.trim().isEmpty()) {
            String correoNormalizado = correo.trim().toLowerCase();
            // Para la fase 2 usamos buscarPorCorreo como equivalente a correoExiste
            if (evaluadorDao.buscarPorCorreo(correoNormalizado) != null) {
                String token = UUID.randomUUID().toString();
                // evaluadorDao.guardarTokenRecuperacion(correoNormalizado, token); // TODO: Agregar si se requiere en BD

                String baseUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
                String enlaceRestablecer = baseUrl + "/recuperar?token=" + token;

                String contenidoHtml = EmailSender.generarPlantillaRecuperacion(enlaceRestablecer);
                EmailSender.sendMail(correoNormalizado, "Recuperar tu contraseña de UXVibe", contenidoHtml);
            }
            resp.sendRedirect("correo-enviado.jsp?correo=" + URLEncoder.encode(correoNormalizado, StandardCharsets.UTF_8));
        } else {
            resp.sendRedirect("recuperar-contra.jsp");
        }
    }

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

        String nuevoHash = PasswordUtil.hashPassword(pass1, "");
        boolean actualizado = evaluadorDao.actualizarContrasena(evaluador.getIdEvaluador(), nuevoHash, "");

        if (actualizado) {
            // evaluadorDao.eliminarToken(token); // TODO
            resp.sendRedirect("contra-actualizada.jsp");
        } else {
            redirectError(resp, token, "Ocurrió un error al actualizar la contraseña.");
        }
    }

    private void redirectError(HttpServletResponse resp, String token, String mensaje) throws IOException {
        String tokenEnc = token != null ? URLEncoder.encode(token, StandardCharsets.UTF_8) : "";
        String msgEnc = URLEncoder.encode(mensaje, StandardCharsets.UTF_8);
        resp.sendRedirect("cambiar-contra.jsp?token=" + tokenEnc + "&error=" + msgEnc);
    }
}
