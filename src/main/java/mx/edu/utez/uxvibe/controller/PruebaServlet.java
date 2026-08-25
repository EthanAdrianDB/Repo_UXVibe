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

/**
 * Controlador para la gestión de Pruebas de usabilidad (CRUD).
 * Permite crear una nueva prueba, cargar los datos existentes para editarlos,
 * actualizar la información y eliminar pruebas.
 */
@WebServlet(name = "PruebaServlet", value = "/prueba")
public class PruebaServlet extends HttpServlet {

    private final PruebaDao pruebaDao = new PruebaDao();

    /**
     * Petición GET:
     * - action="nueva": Abre el formulario vacío en nueva-prueba.jsp.
     * - id=X: Carga los datos de la prueba X en nueva-prueba.jsp para editarla.
     */
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

            // Validamos que la prueba exista y pertenezca al usuario en sesión
            Prueba prueba = pruebaDao.getById(idPrueba);
            if (prueba == null || prueba.getIdEvaluador() != evaluador.getIdEvaluador()) {
                response.sendRedirect(request.getContextPath() + "/error-acceso.jsp");
                return;
            }

            // Pasamos el objeto prueba para prellenar el formulario de edición
            request.setAttribute("pruebaEditar", prueba);
            request.getRequestDispatcher("nueva-prueba.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("/inicio").forward(request, response);
    }

    /**
     * Petición POST: Procesa las acciones "create", "update" y "delete".
     * Aplica el patrón PRG (Post/Redirect/Get) para evitar reenvíos al refrescar la página.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        Evaluador evaluador = (Evaluador) session.getAttribute("evaluador");

        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            // Eliminar prueba
            int id = Integer.parseInt(request.getParameter("id"));
            pruebaDao.delete(id);

        } else if ("create".equals(action) || "update".equals(action)) {
            String nombre = request.getParameter("nombre");
            String url = request.getParameter("url");
            String tarea = request.getParameter("tarea");

            int idPruebaExcluida = 0;
            if ("update".equals(action)) {
                try {
                    idPruebaExcluida = Integer.parseInt(request.getParameter("id"));
                } catch(Exception ignored) {}
            }

            // Validamos que no se duplique el nombre de la prueba para el mismo evaluador
            if (pruebaDao.existePrueba(evaluador.getIdEvaluador(), nombre, idPruebaExcluida)) {
                request.setAttribute("error", "Ya tienes una prueba con ese nombre. Por favor, elige otro.");
                Prueba p = new Prueba();
                p.setNombre(nombre);
                p.setUrlSistema(url);
                p.setDescripcion(tarea);
                if (idPruebaExcluida > 0) p.setIdPrueba(idPruebaExcluida);
                
                request.setAttribute("pruebaEditar", p);
                request.getRequestDispatcher("nueva-prueba.jsp").forward(request, response);
                return;
            }

            Prueba prueba = new Prueba();
            prueba.setIdEvaluador(evaluador.getIdEvaluador());
            prueba.setNombre(nombre);
            prueba.setUrlSistema(url);
            prueba.setDescripcion(tarea);

            if ("create".equals(action)) {
                pruebaDao.create(prueba);
            } else {
                prueba.setIdPrueba(idPruebaExcluida);
                pruebaDao.update(prueba);
            }
        }

        // Si fue una petición asíncrona (AJAX/Fetch), respondemos 200 OK
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // Patrón PRG (Post/Redirect/Get) para redirigir a inicio
        response.sendRedirect(request.getContextPath() + "/inicio");
    }
}
