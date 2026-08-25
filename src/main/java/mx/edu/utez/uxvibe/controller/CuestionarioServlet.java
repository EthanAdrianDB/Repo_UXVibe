package mx.edu.utez.uxvibe.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.uxvibe.model.Participante;
import mx.edu.utez.uxvibe.model.Prueba;
import mx.edu.utez.uxvibe.model.Respuesta;
import mx.edu.utez.uxvibe.model.dao.ParticipanteDao;
import mx.edu.utez.uxvibe.model.dao.PruebaDao;
import mx.edu.utez.uxvibe.model.dao.RespuestaDao;

import java.io.IOException;

/**
 * Controlador para la interfaz pública del cuestionario que contestan los participantes.
 * Maneja el flujo en 2 pasos:
 * 1. "iniciar": Registro de datos demográficos del participante (nombre, edad, sexo).
 * 2. "responder": Captura de escalas SAM (1-9), preguntas de usabilidad Likert (1-5) y estados de ánimo.
 */
@WebServlet(name = "CuestionarioServlet", value = "/cuestionario")
public class CuestionarioServlet extends HttpServlet {

    private final PruebaDao pruebaDao = new PruebaDao();
    private final ParticipanteDao participanteDao = new ParticipanteDao();
    private final RespuestaDao respuestaDao = new RespuestaDao();

    /**
     * Petición GET:
     * - Si el participante aún no se ha registrado, muestra cuestionario-inicio.jsp.
     * - Si ya inició sesión temporal en la prueba, muestra el cuestionario activo (cuestionario.jsp).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        Integer idParticipante = (Integer) session.getAttribute("cuestionario_idParticipante");

        if (idParticipante == null) {
            String idPruebaStr = request.getParameter("idPrueba");
            if (idPruebaStr != null) {
                try {
                    int idPrueba = Integer.parseInt(idPruebaStr);
                    Prueba prueba = pruebaDao.getById(idPrueba);
                    request.setAttribute("prueba", prueba);
                } catch (NumberFormatException e) {
                    // Si el ID es inválido, simplemente no se carga la prueba
                }
            }
            request.getRequestDispatcher("cuestionario-inicio.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("cuestionario.jsp").forward(request, response);
    }

    /**
     * Petición POST:
     * - action="iniciar": Crea el participante y guarda su ID en la sesión temporal.
     * - action="responder": Lee todas las respuestas numéricas, las guarda en Oracle y redirige a la pantalla de agradecimiento.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        HttpSession session = request.getSession(true);

        if ("iniciar".equals(action)) {
            // Paso 1: Captura de datos personales del participante
            int idPrueba = Integer.parseInt(request.getParameter("idPrueba"));
            String nombre = request.getParameter("nombre");
            String apellidoM = request.getParameter("apellidoM");
            String apellidoP = request.getParameter("apellidoP");
            int sexo = parseSexo(request.getParameter("sexo"));
            int edad = parseIntSafe(request.getParameter("edad"), 0);

            Participante participante = new Participante();
            participante.setIdPrueba(idPrueba);
            participante.setNombre(nombre != null ? nombre.trim() : "");
            participante.setApellidoM(apellidoM != null ? apellidoM.trim() : "");
            participante.setApellidoP(apellidoP != null ? apellidoP.trim() : "");
            participante.setSexo(sexo);
            participante.setEdad(edad);

            participanteDao.create(participante);

            // Guardamos en la sesión temporal para vincular sus respuestas en el siguiente paso
            session.setAttribute("cuestionario_idPrueba", idPrueba);
            session.setAttribute("cuestionario_idParticipante", participante.getIdParticipante());

            response.sendRedirect(request.getContextPath() + "/cuestionario");

        } else if ("responder".equals(action)) {
            // Paso 2: Guardar las respuestas del cuestionario
            Integer idParticipante = (Integer) session.getAttribute("cuestionario_idParticipante");
            Integer idPrueba = (Integer) session.getAttribute("cuestionario_idPrueba");

            if (idParticipante != null && idPrueba != null) {
                Respuesta respuesta = new Respuesta();
                respuesta.setIdParticipante(idParticipante);
                respuesta.setIdPrueba(idPrueba);
                
                // Modelo SAM: Valencia, Activación y Dominancia (1 al 9)
                respuesta.setSam1(parseIntSafe(request.getParameter("sam1"), 1));
                respuesta.setSam2(parseIntSafe(request.getParameter("sam2"), 1));
                respuesta.setSam3(parseIntSafe(request.getParameter("sam3"), 1));
                
                // Reactivos de Usabilidad R1 a R15 (1 al 5)
                respuesta.setR1(parseIntSafe(request.getParameter("r1"), 1));
                respuesta.setR2(parseIntSafe(request.getParameter("r2"), 1));
                respuesta.setR3(parseIntSafe(request.getParameter("r3"), 1));
                respuesta.setR4(parseIntSafe(request.getParameter("r4"), 1));
                respuesta.setR5(parseIntSafe(request.getParameter("r5"), 1));
                respuesta.setR6(parseIntSafe(request.getParameter("r6"), 1));
                respuesta.setR7(parseIntSafe(request.getParameter("r7"), 1));
                respuesta.setR8(parseIntSafe(request.getParameter("r8"), 1));
                respuesta.setR9(parseIntSafe(request.getParameter("r9"), 1));
                respuesta.setR10(parseIntSafe(request.getParameter("r10"), 1));
                respuesta.setR11(parseIntSafe(request.getParameter("r11"), 1));
                respuesta.setR12(parseIntSafe(request.getParameter("r12"), 1));
                respuesta.setR13(parseIntSafe(request.getParameter("r13"), 1));
                respuesta.setR14(parseIntSafe(request.getParameter("r14"), 1));
                respuesta.setR15(parseIntSafe(request.getParameter("r15"), 1));
                
                // Frecuencias de estado de ánimo
                respuesta.setFrecuenciaEstadoAnimo1(parseIntSafe(request.getParameter("frecuenciaEstadoAnimo1"), 1));
                respuesta.setFrecuenciaEstadoAnimo2(parseIntSafe(request.getParameter("frecuenciaEstadoAnimo2"), 1));

                respuestaDao.create(respuesta);

                // Limpiamos las variables de sesión del participante tras finalizar
                session.removeAttribute("cuestionario_idPrueba");
                session.removeAttribute("cuestionario_idParticipante");

                response.sendRedirect(request.getContextPath() + "/cuestionario-gracias.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/cuestionario");
            }
        }
    }
    
    /**
     * Parsea un String a entero de manera segura sin que truene con NumberFormatException.
     */
    private int parseIntSafe(String val, int defaultVal) {
        if (val == null || val.trim().isEmpty()) return defaultVal;
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
    
    /**
     * Normaliza el valor de sexo: 1 para Masculino, 0 para Femenino.
     */
    private int parseSexo(String val) {
        if (val == null || val.trim().isEmpty()) return 0;
        val = val.trim();
        if ("1".equals(val) || "Masculino".equalsIgnoreCase(val)) return 1;
        if ("0".equals(val) || "Femenino".equalsIgnoreCase(val)) return 0;
        try { return Integer.parseInt(val) == 1 ? 1 : 0; } catch (Exception e) { return 0; }
    }
}
