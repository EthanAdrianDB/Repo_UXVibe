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

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    private final EvaluadorDao evaluadorDao = new EvaluadorDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("correo");
        String password = request.getParameter("password");

        Evaluador evaluador = (correo != null) ? evaluadorDao.buscarPorCorreo(correo.trim()) : null;

        if (evaluador != null) {
            String hashIngresado = PasswordUtil.hashPassword(password, "");
            if (hashIngresado.equals(evaluador.getContrasena())) {
                HttpSession session = request.getSession(true);
                session.setMaxInactiveInterval(60 * 30);
                session.setAttribute("evaluador", evaluador);
                response.sendRedirect(request.getContextPath() + "/inicio");
                return;
            }
        }

        request.setAttribute("error", "Correo o contraseña incorrectos.");
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
