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

/**
 * Controlador para la reproducción y visualización de archivos de audio grabados.
 * Implementa soporte para cabeceras HTTP "Range" (HTTP 206 Partial Content),
 * lo que permite a los reproductores HTML5 adelantar o retroceder el audio de manera fluida.
 */
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

        // Modo reproducción: Transmisión directa del archivo binario
        if ("play".equals(action)) {
            int idParticipante = 0;
            try {
                idParticipante = Integer.parseInt(request.getParameter("idParticipante"));
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            // Obtenemos los bytes del audio desde la base de datos Oracle (BLOB)
            byte[] audioBytes = new mx.edu.utez.uxvibe.model.dao.ArchivoAudioDao().getAudioBytes(idParticipante, idPrueba);
            
            if (audioBytes != null && audioBytes.length > 0) {
                int fileLength = audioBytes.length;
                String rangeHeader = request.getHeader("Range");

                response.setContentType("audio/webm");
                response.setHeader("Accept-Ranges", "bytes");

                // Si el navegador pide un fragmento específico del audio (Range: bytes=start-end)
                if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
                    String rangeValue = rangeHeader.substring(6).trim();
                    int dashPos = rangeValue.indexOf('-');
                    int start = 0;
                    int end = fileLength - 1;

                    try {
                        if (dashPos == 0) {
                            int suffix = Integer.parseInt(rangeValue.substring(1));
                            start = Math.max(0, fileLength - suffix);
                        } else if (dashPos == rangeValue.length() - 1) {
                            start = Integer.parseInt(rangeValue.substring(0, dashPos));
                        } else if (dashPos > 0) {
                            start = Integer.parseInt(rangeValue.substring(0, dashPos));
                            end = Integer.parseInt(rangeValue.substring(dashPos + 1));
                        }
                    } catch (NumberFormatException ignored) {}

                    if (start >= fileLength) {
                        response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                        response.setHeader("Content-Range", "bytes */" + fileLength);
                        return;
                    }

                    if (end >= fileLength) {
                        end = fileLength - 1;
                    }

                    int contentLength = (end - start) + 1;

                    // Respuesta parcial 206 para el reproductor
                    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                    response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
                    response.setContentLength(contentLength);

                    java.io.OutputStream os = response.getOutputStream();
                    os.write(audioBytes, start, contentLength);
                    os.flush();
                } else {
                    // Si el navegador pide el archivo completo
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentLength(fileLength);

                    java.io.OutputStream os = response.getOutputStream();
                    os.write(audioBytes, 0, fileLength);
                    os.flush();
                }
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            return;
        }

        // Modo vista: Carga la lista de participantes con audio en audio.jsp
        Prueba prueba = pruebaDao.getById(idPrueba);

        request.setAttribute("prueba", prueba);
        request.setAttribute("pestanaActiva", "audio");
        request.setAttribute("participantes", participanteDao.getPorPrueba(idPrueba));

        request.getRequestDispatcher("audio.jsp").forward(request, response);
    }
}
