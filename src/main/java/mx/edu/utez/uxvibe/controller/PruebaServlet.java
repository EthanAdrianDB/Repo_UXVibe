package mx.edu.utez.uxvibe.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.uxvibe.model.Evaluador;
import mx.edu.utez.uxvibe.model.Prueba;
import mx.edu.utez.uxvibe.model.dao.PruebaDao;

import java.io.IOException;

@WebServlet(name = "PruebaServlet", value = "/prueba")
public class PruebaServlet extends HttpServlet {

    private final PruebaDao pruebaDao = new PruebaDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        if ("nueva".equals(action)) {
            request.getRequestDispatcher("nueva-prueba.jsp").forward(request, response);
            return;
        }

        if (idParam != null) {
            Evaluador evaluador = (Evaluador) request.getSession().getAttribute("evaluador");
            if (evaluador == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }

            int idPrueba = -1;
            try {
                if (!idParam.isEmpty()) {
                    idPrueba = Integer.parseInt(idParam);
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/error-acceso.jsp");
                return;
            }

            // Editar: mandamos la prueba existente al formulario de nueva-prueba.jsp
            Prueba prueba = pruebaDao.getById(idPrueba);
            if (prueba == null || prueba.getIdEvaluador() != evaluador.getIdEvaluador()) {
                response.sendRedirect(request.getContextPath() + "/error-acceso.jsp");
                return;
            }

            request.setAttribute("pruebaEditar", prueba);
            request.getRequestDispatcher("nueva-prueba.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("/inicio").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        Evaluador evaluador = (Evaluador) session.getAttribute("evaluador");

        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            pruebaDao.delete(id);

        } else if ("create".equals(action) || "update".equals(action)) {
            String nombre = request.getParameter("nombre");
            String url = request.getParameter("url");
            String tarea = request.getParameter("tarea");

            Prueba prueba = new Prueba();
            prueba.setIdEvaluador(evaluador.getIdEvaluador());
            prueba.setNombre(nombre);
            prueba.setUrlSistema(url);
            prueba.setDescripcion(tarea);

            if ("create".equals(action)) {
                pruebaDao.create(prueba);
            } else {
                prueba.setIdPrueba(Integer.parseInt(request.getParameter("id")));
                pruebaDao.update(prueba);
            }
        }

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // Patrón PRG (Post/Redirect/Get): evita reenvíos duplicados al recargar
        response.sendRedirect(request.getContextPath() + "/inicio");
    }
}
