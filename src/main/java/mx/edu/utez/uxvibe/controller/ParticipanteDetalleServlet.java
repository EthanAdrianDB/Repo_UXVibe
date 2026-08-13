package mx.edu.utez.uxvibe.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.uxvibe.model.Colaborador;
import mx.edu.utez.uxvibe.model.dao.ParticipanteDao;
import mx.edu.utez.uxvibe.model.dao.RespuestaDao;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "ParticipanteDetalleServlet", value = "/participante-detalle")
public class ParticipanteDetalleServlet extends HttpServlet {

    private final ParticipanteDao participanteDao = new ParticipanteDao();
    private final RespuestaDao respuestaDao = new RespuestaDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Colaborador participante = participanteDao.getById(id);

        if (participante != null) {
            Map<String, Integer> respuestasLikert =
                    respuestaDao.respuestasLikertDeParticipante(id, participante.getIdPrueba());
            String comentarios = respuestaDao.comentariosDeParticipante(id, participante.getIdPrueba());

            request.setAttribute("participante", participante);
            request.setAttribute("respuestasLikert", respuestasLikert);
            request.setAttribute("comentarios", comentarios);
        }

        request.getRequestDispatcher("participante-detalle.jsp").forward(request, response);
    }
}
