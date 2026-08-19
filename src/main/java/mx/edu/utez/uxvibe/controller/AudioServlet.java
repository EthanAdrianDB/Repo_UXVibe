package mx.edu.utez.uxvibe.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.uxvibe.model.Prueba;
import mx.edu.utez.uxvibe.model.dao.ParticipanteDao;
import mx.edu.utez.uxvibe.model.dao.PruebaDao;

import java.io.IOException;

@WebServlet(name = "AudioServlet", value = "/audio")
public class AudioServlet extends HttpServlet {

    private final PruebaDao pruebaDao = new PruebaDao();
    private final ParticipanteDao participanteDao = new ParticipanteDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String idPruebaStr = request.getParameter("idPrueba");
        int idPrueba = 0;
        try {
            if (idPruebaStr != null) idPrueba = Integer.parseInt(idPruebaStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/inicio");
            return;
        }

        if ("play".equals(action)) {
            int idParticipante = 0;
            try {
                idParticipante = Integer.parseInt(request.getParameter("idParticipante"));
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            java.io.InputStream is = new mx.edu.utez.uxvibe.model.dao.ArchivoAudioDao().getAudioStream(idParticipante, idPrueba);
            
            if (is != null) {
                response.setContentType("audio/webm"); // Default type used by MediaRecorder
                java.io.OutputStream os = response.getOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                is.close();
                os.flush();
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            return;
        }

        Prueba prueba = pruebaDao.getById(idPrueba);

        request.setAttribute("prueba", prueba);
        request.setAttribute("pestanaActiva", "audio");
        request.setAttribute("participantes", participanteDao.getPorPrueba(idPrueba));

        request.getRequestDispatcher("audio.jsp").forward(request, response);
    }
}
