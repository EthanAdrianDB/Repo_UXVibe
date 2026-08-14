package mx.edu.utez.uxvibe.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.uxvibe.model.Participante;
import mx.edu.utez.uxvibe.model.Prueba;
import mx.edu.utez.uxvibe.model.dao.ParticipanteDao;
import mx.edu.utez.uxvibe.model.dao.PruebaDao;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import mx.edu.utez.uxvibe.model.ArchivoAudio;
import mx.edu.utez.uxvibe.model.dao.ArchivoAudioDao;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ParticipanteServlet", value = "/participantes")
@MultipartConfig(maxFileSize = 1024 * 1024 * 50) // 50MB max file size
public class ParticipanteServlet extends HttpServlet {

    private final ParticipanteDao participanteDao = new ParticipanteDao();
    private final PruebaDao pruebaDao = new PruebaDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idPrueba = Integer.parseInt(request.getParameter("idPrueba"));
        Prueba prueba = pruebaDao.getById(idPrueba);
        List<Participante> participantes = participanteDao.getPorPrueba(idPrueba);

        request.setAttribute("prueba", prueba);
        request.setAttribute("participantes", participantes);
        request.setAttribute("pestanaActiva", "participantes");

        request.getRequestDispatcher("gestion-participantes.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        int idPrueba = Integer.parseInt(request.getParameter("idPrueba"));

        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            participanteDao.delete(id);

        } else if ("create".equals(action)) {
            Participante p = new Participante();
            p.setIdPrueba(idPrueba);
            String nombre = request.getParameter("nombre");
            p.setNombre(nombre != null ? nombre.trim() : "");
            
            String apellidoP = request.getParameter("apellidoP");
            p.setApellidoP(apellidoP != null ? apellidoP.trim() : "");
            
            String apellidoM = request.getParameter("apellidoM");
            p.setApellidoM(apellidoM != null ? apellidoM.trim() : "");
            
            String sexoStr = request.getParameter("sexo");
            p.setSexo(parseSexo(sexoStr));
            
            String edadStr = request.getParameter("edad");
            p.setEdad(edadStr != null && !edadStr.trim().isEmpty() ? Integer.parseInt(edadStr.trim()) : 0);
            if (participanteDao.create(p)) {
                // Guardar la respuesta asociada
                mx.edu.utez.uxvibe.model.Respuesta r = new mx.edu.utez.uxvibe.model.Respuesta();
                r.setIdParticipante(p.getIdParticipante());
                r.setIdPrueba(idPrueba);
                
                // SAM
                r.setSam1(parseParam(request.getParameter("sam_valencia")));
                r.setSam2(parseParam(request.getParameter("sam_activacion")));
                r.setSam3(parseParam(request.getParameter("sam_dominio")));
                
                // Likert
                r.setR1(parseParam(request.getParameter("ux_q1")));
                r.setR2(parseParam(request.getParameter("ux_q2")));
                r.setR3(parseParam(request.getParameter("ux_q3")));
                r.setR4(parseParam(request.getParameter("ux_q4")));
                r.setR5(parseParam(request.getParameter("ux_q5")));
                r.setR6(parseParam(request.getParameter("ux_q6")));
                r.setR7(parseParam(request.getParameter("ux_q7")));
                r.setR8(parseParam(request.getParameter("ux_q8")));
                r.setR9(parseParam(request.getParameter("ux_q9")));
                r.setR10(parseParam(request.getParameter("ux_q10")));
                r.setR11(parseParam(request.getParameter("ux_q11")));
                r.setR12(parseParam(request.getParameter("ux_q12")));
                r.setR13(parseParam(request.getParameter("ux_q13")));
                r.setR14(parseParam(request.getParameter("ux_q14")));
                r.setR15(parseParam(request.getParameter("ux_q15")));
                
                // Frecuencia
                r.setFrecuenciaEstadoAnimo1(parseFrecuencia(request.getParameter("estado_estresado")));
                r.setFrecuenciaEstadoAnimo2(parseFrecuencia(request.getParameter("estado_relajado")));
                
                new mx.edu.utez.uxvibe.model.dao.RespuestaDao().create(r);

                // Save audio
                Part audioPart = request.getPart("audio_file");
                if (audioPart != null && audioPart.getSize() > 0) {
                    ArchivoAudio audio = new ArchivoAudio();
                    audio.setIdParticipante(p.getIdParticipante());
                    audio.setIdPrueba(idPrueba);
                    audio.setAudio(audioPart.getInputStream());
                    new ArchivoAudioDao().create(audio);
                }
            }
        }

        // Si es una petición fetch (AJAX), podemos devolver un simple OK. 
        // Si no, redireccionamos. EvaluacionInvestigador usa fetch.
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With")) || request.getContentType() != null && request.getContentType().startsWith("multipart/form-data")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/participantes?idPrueba=" + idPrueba);
    }
    
    private int parseParam(String val) {
        if (val == null || val.trim().isEmpty()) return 3; // valor neutro por defecto
        try { return Integer.parseInt(val); } catch (Exception e) { return 3; }
    }
    
    private int parseFrecuencia(String val) {
        if (val == null) return 3;
        switch (val) {
            case "Nunca": return 1;
            case "De vez en cuando": return 2;
            case "Cerca de la mitad del tiempo": return 3;
            case "La mayor parte del tiempo": return 4;
            case "Siempre": return 5;
            default: return 3;
        }
    }
    
    /** Convierte el valor de sexo a 0 (Femenino) o 1 (Masculino). 
     *  Acepta "0","1","Femenino","Masculino" o cualquier cosa → 0 por defecto. */
    private int parseSexo(String val) {
        if (val == null || val.trim().isEmpty()) return 0;
        val = val.trim();
        if ("1".equals(val) || "Masculino".equalsIgnoreCase(val)) return 1;
        if ("0".equals(val) || "Femenino".equalsIgnoreCase(val)) return 0;
        // Si es cualquier otra cosa, intentar como número
        try { return Integer.parseInt(val) == 1 ? 1 : 0; } catch (Exception e) { return 0; }
    }
}
