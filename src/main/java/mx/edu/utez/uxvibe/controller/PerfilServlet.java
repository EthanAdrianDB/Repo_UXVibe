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

@WebServlet(name = "PerfilServlet", value = "/perfil")
public class PerfilServlet extends HttpServlet {

    private final EvaluadorDao evaluadorDao = new EvaluadorDao();

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
        Evaluador evaluador = (Evaluador) session.getAttribute("evaluador");
        String action = request.getParameter("action");

        if ("editarInfo".equals(action)) {
            evaluador.setNombre(request.getParameter("nombre"));
            evaluador.setApellidoP(request.getParameter("apellidoPaterno"));
            evaluador.setApellidoM(request.getParameter("apellidoMaterno"));
            evaluadorDao.update(evaluador);
            session.setAttribute("evaluador", evaluador);

            response.sendRedirect(request.getContextPath() + "/perfil?actualizado=info");
            return;

        } else if ("cambiarContrasena".equals(action)) {
            String password = request.getParameter("password");
            String confirmarPassword = request.getParameter("confirmarPassword");

            if (password != null && password.equals(confirmarPassword)) {
                String nuevoHash = PasswordUtil.hashPassword(password);
                evaluadorDao.actualizarContrasena(evaluador.getIdEvaluador(), nuevoHash);
                evaluador.setContrasena(nuevoHash);
                session.setAttribute("evaluador", evaluador);
                response.sendRedirect(request.getContextPath() + "/perfil?actualizado=password");
                return;
            }
        }

        response.sendRedirect(request.getContextPath() + "/perfil");
    }
}
