package mx.edu.utez.uxvibe.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.uxvibe.model.Colaborador;
import mx.edu.utez.uxvibe.model.Prueba;
import mx.edu.utez.uxvibe.model.RespuestaCuestionario;
import mx.edu.utez.uxvibe.model.dao.ParticipanteDao;
import mx.edu.utez.uxvibe.model.dao.PruebaDao;
import mx.edu.utez.uxvibe.model.dao.RespuestaDao;

import java.io.IOException;

/**
 * Flujo público del cuestionario para el colaborador/participante.
 * 
 * 1) GET /cuestionario?idPrueba=X -> Muestra datos demográficos
 * 2) POST /cuestionario (action=iniciar) -> Guarda Colaborador + SesionEvaluacion
 * 3) GET /cuestionario -> Muestra la encuesta de satisfacción y comentarios libres
 * 4) POST /cuestionario (action=responder) -> Guarda RespuestaCuestionario y muestra gracias
 */
@WebServlet(name = "CuestionarioServlet", value = "/cuestionario")
public class CuestionarioServlet extends HttpServlet {

    private final PruebaDao pruebaDao = new PruebaDao();
    private final ParticipanteDao participanteDao = new ParticipanteDao();
    private final RespuestaDao respuestaDao = new RespuestaDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        Integer idColaborador = (Integer) session.getAttribute("cuestionario_idColaborador");

        if (idColaborador == null) {
            String idPruebaStr = request.getParameter("idPrueba");
            if (idPruebaStr != null) {
                int idPrueba = Integer.parseInt(idPruebaStr);
                Prueba prueba = pruebaDao.getById(idPrueba);
                request.setAttribute("prueba", prueba);
            }
            request.getRequestDispatcher("cuestionario-inicio.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("cuestionario.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        HttpSession session = request.getSession(true);

        if ("iniciar".equals(action)) {
            int idPrueba = Integer.parseInt(request.getParameter("idPrueba"));
            String rangoEdad = request.getParameter("rangoEdad");
            String genero = request.getParameter("genero");

            if (rangoEdad == null || rangoEdad.isEmpty()) {
                rangoEdad = request.getParameter("edad"); // Fallback si viene como campo de texto edad
            }

            Colaborador colaborador = new Colaborador();
            colaborador.setIdPrueba(idPrueba);
            String nombre = request.getParameter("nombre");
            if (nombre != null && !nombre.trim().isEmpty()) {
                colaborador.setNombre(nombre.trim());
            }
            colaborador.setRangoEdad(rangoEdad != null ? rangoEdad : "18-25");
            colaborador.setGenero(genero != null ? genero : "Otro");
            colaborador.setAvisoConsentimiento(1);

            participanteDao.create(colaborador);

            session.setAttribute("cuestionario_idPrueba", idPrueba);
            session.setAttribute("cuestionario_idColaborador", colaborador.getIdColaborador());
            session.setAttribute("cuestionario_idSesion", colaborador.getIdSesion());

            response.sendRedirect(request.getContextPath() + "/cuestionario");

        } else if ("responder".equals(action)) {
            Integer idSesion = (Integer) session.getAttribute("cuestionario_idSesion");
            if (idSesion == null || idSesion == 0) {
                idSesion = (Integer) session.getAttribute("cuestionario_idColaborador");
            }

            if (idSesion != null) {
                String escalaSatisfaccionStr = request.getParameter("escalaSatisfaccion");
                int escalaSatisfaccion = (escalaSatisfaccionStr != null) ? Integer.parseInt(escalaSatisfaccionStr) : 5;
                String comentarios = request.getParameter("comentariosLibres");

                RespuestaCuestionario respuesta = new RespuestaCuestionario();
                respuesta.setIdSesion(idSesion);
                respuesta.setEscalasatisfaccion(escalaSatisfaccion);
                respuesta.setComentariosLibres(comentarios);

                respuestaDao.create(respuesta);

                session.removeAttribute("cuestionario_idPrueba");
                session.removeAttribute("cuestionario_idColaborador");
                session.removeAttribute("cuestionario_idSesion");

                response.sendRedirect(request.getContextPath() + "/inicio");
            } else {
                response.sendRedirect(request.getContextPath() + "/cuestionario");
            }
        }
    }
}
