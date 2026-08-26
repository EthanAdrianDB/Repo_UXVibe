package mx.edu.utez.uxvibe.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controlador para cerrar la sesión actual del evaluador.
 * Invalida la sesión HTTP en el servidor y redirige a la pantalla de login.
 */
@WebServlet(name = "LogoutServlet", value = "/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            // Destruimos la sesión y todas sus variables guardadas
            session.invalidate();
        }
        // Redirigimos al formulario de acceso
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}
