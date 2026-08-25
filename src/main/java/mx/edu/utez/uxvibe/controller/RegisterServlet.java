package mx.edu.utez.uxvibe.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.uxvibe.model.Evaluador;
import mx.edu.utez.uxvibe.model.dao.EvaluadorDao;
import mx.edu.utez.uxvibe.utils.PasswordUtil;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Controlador para el registro de nuevos evaluadores.
 * Valida los datos del formulario (campos vacíos, coincidencia de correos/contraseñas,
 * formato de seguridad con Regex) y guarda el nuevo usuario con contraseña hasheada.
 */
@WebServlet(name = "RegisterServlet", value = "/registro")
public class RegisterServlet extends HttpServlet {

    private final EvaluadorDao evaluadorDao = new EvaluadorDao();

    // Regla de seguridad: Mínimo 8 caracteres, al menos una mayúscula, una minúscula y un número
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");

    /**
     * Muestra el formulario de registro (registro.jsp).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("registro.jsp").forward(request, response);
    }

    /**
     * Procesa el formulario de registro: valida datos, previene correos repetidos y registra en BD.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // Obtenemos los parámetros enviados en el formulario
        String nombres = request.getParameter("nombres");
        String apellidoPaterno = request.getParameter("apellidoPaterno");
        String apellidoMaterno = request.getParameter("apellidoMaterno");
        String correo = request.getParameter("correo");
        String confirmarCorreo = request.getParameter("confirmarCorreo");
        String password = request.getParameter("password");
        String confirmarPassword = request.getParameter("confirmarPassword");

        // Regresamos los valores al request para no obligar al usuario a reescribirlos si hay un error
        request.setAttribute("nombres", nombres);
        request.setAttribute("apellidoPaterno", apellidoPaterno);
        request.setAttribute("apellidoMaterno", apellidoMaterno);
        request.setAttribute("correo", correo);

        // 1. Validaciones lógicas (campos requeridos, contraseñas iguales, patrón regex)
        String error = validar(nombres, apellidoPaterno, apellidoMaterno, correo, confirmarCorreo, password, confirmarPassword);
        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        // 2. Comprobar que no exista ya ese correo
        String correoNormalizado = correo.trim().toLowerCase();
        if (evaluadorDao.buscarPorCorreo(correoNormalizado) != null) {
            request.setAttribute("error", "Ya existe una cuenta registrada con ese correo.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        // 3. Hasheamos la contraseña con SHA-256
        String hash = PasswordUtil.hashPassword(password);

        // 4. Creamos el objeto Evaluador y lo insertamos en Oracle DB
        Evaluador nuevo = new Evaluador(0, nombres.trim(), apellidoMaterno != null ? apellidoMaterno.trim() : "", apellidoPaterno.trim(), correoNormalizado, hash);

        boolean creado = evaluadorDao.create(nuevo);
        if (creado) {
            // Registro exitoso -> mandamos a login con parámetro de confirmación
            response.sendRedirect(request.getContextPath() + "/login?registroExitoso=true");
        } else {
            String err = EvaluadorDao.getUltimoError();
            request.setAttribute("error", "Error al registrar en la base de datos: " + (err != null ? err : "Verifica la conexión a Oracle Cloud."));
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }

    /**
     * Valida que no haya campos en blanco y que se cumplan las reglas de coincidencia y complejidad.
     */
    private String validar(String nombres, String apellidoPaterno, String apellidoMaterno,
                            String correo, String confirmarCorreo,
                            String password, String confirmarPassword) {
        if (esVacio(nombres) || esVacio(apellidoPaterno)
                || esVacio(correo) || esVacio(confirmarCorreo)
                || esVacio(password) || esVacio(confirmarPassword)) {
            return "Todos los campos son obligatorios.";
        }
        if (!correo.trim().equalsIgnoreCase(confirmarCorreo.trim())) {
            return "Los correos electrónicos no coinciden.";
        }
        if (!password.equals(confirmarPassword)) {
            return "Las contraseñas no coinciden.";
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número.";
        }
        return null;
    }

    /**
     * Helper para comprobar si una cadena viene vacía o nula.
     */
    private boolean esVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
