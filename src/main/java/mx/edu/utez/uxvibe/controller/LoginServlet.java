package mx.edu.utez.uxvibe.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.uxvibe.model.Evaluador;
import mx.edu.utez.uxvibe.model.dao.EvaluadorDao;
import mx.edu.utez.uxvibe.utils.PasswordUtil;

import java.io.IOException;

/**
 * Controlador para el inicio de sesión de los evaluadores.
 * Muestra el formulario con doGet y valida las credenciales en doPost.
 */
@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    private final EvaluadorDao evaluadorDao = new EvaluadorDao();

    /**
     * Petición GET: Despacha la vista login.jsp al navegador.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    /**
     * Petición POST: Recibe el correo y la contraseña ingresada,
     * la hashea con SHA-256 y la compara con la contraseña de la base de datos.
     * Si coincide, crea la sesión HTTP y redirige a /inicio.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("correo");
        String password = request.getParameter("password");

        // Buscamos al evaluador por su correo
        Evaluador evaluador = (correo != null) ? evaluadorDao.buscarPorCorreo(correo.trim()) : null;

        if (evaluador != null && password != null) {
            // Generamos el hash de la contraseña escrita para comparar con el hash guardado
            String hashIngresado = PasswordUtil.hashPassword(password);
            if (hashIngresado.equals(evaluador.getContrasena())) {
                // Credenciales correctas: Creamos sesión por 30 minutos
                HttpSession session = request.getSession(true);
                session.setMaxInactiveInterval(60 * 30);
                session.setAttribute("evaluador", evaluador);
                response.sendRedirect(request.getContextPath() + "/inicio");
                return;
            }
        }

        // Si falló el correo o la contraseña, mandamos mensaje de error
        request.setAttribute("error", "Correo o contraseña incorrectos.");
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
