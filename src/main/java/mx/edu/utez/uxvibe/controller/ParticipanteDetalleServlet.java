package mx.edu.utez.uxvibe.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.uxvibe.model.Participante;
import mx.edu.utez.uxvibe.model.Respuesta;
import mx.edu.utez.uxvibe.model.dao.ParticipanteDao;
import mx.edu.utez.uxvibe.model.dao.RespuestaDao;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ParticipanteDetalleServlet", value = "/participante-detalle")
public class ParticipanteDetalleServlet extends HttpServlet {

    private final ParticipanteDao participanteDao = new ParticipanteDao();
    private final RespuestaDao respuestaDao = new RespuestaDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Participante participante = participanteDao.getById(id);

        if (participante != null) {
            List<Respuesta> respuestas = respuestaDao.getPorParticipante(id);
            Respuesta respuesta = respuestas.isEmpty() ? null : respuestas.get(0);

            boolean tieneAudio = new mx.edu.utez.uxvibe.model.dao.ArchivoAudioDao().hasAudio(id, participante.getIdPrueba());

            request.setAttribute("participante", participante);
            request.setAttribute("respuesta", respuesta);
            request.setAttribute("tieneAudio", tieneAudio);
        }

        request.getRequestDispatcher("participante-detalle.jsp").forward(request, response);
    }
}
