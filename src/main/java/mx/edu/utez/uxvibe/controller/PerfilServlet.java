package mx.edu.utez.uxvibe.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.uxvibe.model.Investigador;
import mx.edu.utez.uxvibe.model.dao.InvestigadorDao;
import mx.edu.utez.uxvibe.utils.PasswordUtil;

import java.io.IOException;

@WebServlet(name = "PerfilServlet", value = "/perfil")
public class PerfilServlet extends HttpServlet {

    private final InvestigadorDao investigadorDao = new InvestigadorDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pestanaActiva", "perfil");
        request.getRequestDispatcher("perfil.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        Investigador investigador = (Investigador) session.getAttribute("evaluador");
        String action = request.getParameter("action");

        if ("editarInfo".equals(action)) {
            investigador.setNombre(request.getParameter("nombres"));
            investigadorDao.update(investigador);
            session.setAttribute("evaluador", investigador);

            response.sendRedirect(request.getContextPath() + "/perfil?actualizado=info");
            return;

        } else if ("cambiarContrasena".equals(action)) {
            String password = request.getParameter("password");
            String confirmarPassword = request.getParameter("confirmarPassword");

            if (password != null && password.equals(confirmarPassword)) {
                String nuevoHash = PasswordUtil.hashPassword(password, "");
                investigadorDao.actualizarContrasena(investigador.getId(), nuevoHash, "");
                response.sendRedirect(request.getContextPath() + "/perfil?actualizado=password");
                return;
            }
        }

        response.sendRedirect(request.getContextPath() + "/perfil");
    }
}
