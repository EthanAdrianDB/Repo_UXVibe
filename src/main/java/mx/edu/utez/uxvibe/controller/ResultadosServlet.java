package mx.edu.utez.uxvibe.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.uxvibe.model.Prueba;
import mx.edu.utez.uxvibe.model.dao.ParticipanteDao;
import mx.edu.utez.uxvibe.model.dao.PruebaDao;
import mx.edu.utez.uxvibe.model.dao.RespuestaDao;

import java.io.IOException;

@WebServlet(name = "ResultadosServlet", value = "/resultados")
public class ResultadosServlet extends HttpServlet {

    private final PruebaDao pruebaDao = new PruebaDao();
    private final ParticipanteDao participanteDao = new ParticipanteDao();
    private final RespuestaDao respuestaDao = new RespuestaDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idPrueba = Integer.parseInt(request.getParameter("idPrueba"));
        Prueba prueba = pruebaDao.getById(idPrueba);

        request.setAttribute("prueba", prueba);
        request.setAttribute("pestanaActiva", "resultados");
        
        double edadPromedio = participanteDao.edadPromedio(idPrueba);
        request.setAttribute("edadPromedio", edadPromedio > 0 ? edadPromedio : null);
        request.setAttribute("distribucionSexo", participanteDao.distribucionPorSexo(idPrueba));
        
        java.util.Map<String, Double> promedios = respuestaDao.promedioPorPregunta(idPrueba);
        request.setAttribute("promedioPorPregunta", promedios);

        if (!promedios.isEmpty()) {
            Double sat = promedios.get("Pregunta 13");
            request.setAttribute("satisfaccionPromedio", sat != null ? sat + " / 5" : null);

            Double rec = promedios.get("Pregunta 14");
            request.setAttribute("recomendarian", rec != null ? Math.round((rec / 5.0) * 100) + "%" : null);
        }

        request.getRequestDispatcher("resultados.jsp").forward(request, response);
    }
}
