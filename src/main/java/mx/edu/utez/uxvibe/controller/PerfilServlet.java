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
 * Controlador para la gestión del perfil del evaluador.
 * Permite modificar información personal (nombre y apellidos)
 * y cambiar la contraseña de acceso dentro de la sesión activa.
 */
@WebServlet(name = "PerfilServlet", value = "/perfil")
public class PerfilServlet extends HttpServlet {

    private final EvaluadorDao evaluadorDao = new EvaluadorDao();

    /**
     * Muestra la vista perfil.jsp con los datos actuales del usuario en sesión.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pestanaActiva", "perfil");
        request.getRequestDispatcher("perfil.jsp").forward(request, response);
    }

    /**
     * Procesa las actualizaciones del perfil (editarInfo o cambiarContrasena).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        Evaluador evaluador = (Evaluador) session.getAttribute("evaluador");
        String action = request.getParameter("action");

        if ("editarInfo".equals(action)) {
            // Actualizar nombre y apellidos en BD y en sesión
            evaluador.setNombre(request.getParameter("nombre"));
            evaluador.setApellidoP(request.getParameter("apellidoPaterno"));
            evaluador.setApellidoM(request.getParameter("apellidoMaterno"));
            evaluadorDao.update(evaluador);
            session.setAttribute("evaluador", evaluador);

            response.sendRedirect(request.getContextPath() + "/perfil?actualizado=info");
            return;

        } else if ("cambiarContrasena".equals(action)) {
            // Actualizar contraseña con nuevo hash SHA-256
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
