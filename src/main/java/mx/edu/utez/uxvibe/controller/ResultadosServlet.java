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
        
        // request.setAttribute("edadPromedio", participanteDao.edadPromedio(idPrueba)); // No existe edad en el nuevo modelo
        request.setAttribute("distribucionSexo", participanteDao.distribucionPorSexo(idPrueba));
        
        // Las respuestas ahora son 20 métricas, las dejamos como TODO para fase 3 o vista
        request.setAttribute("promedioPorPregunta", respuestaDao.promedioPorPregunta(idPrueba));

        request.getRequestDispatcher("resultados.jsp").forward(request, response);
    }
}
