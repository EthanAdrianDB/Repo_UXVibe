package mx.edu.utez.uxvibe.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.uxvibe.model.Evaluador;
import mx.edu.utez.uxvibe.model.Prueba;
import mx.edu.utez.uxvibe.model.dao.ParticipanteDao;
import mx.edu.utez.uxvibe.model.dao.PruebaDao;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controlador para la pantalla principal (Dashboard / Inicio).
 * Consulta todas las pruebas activas del evaluador logueado, calcula los totales
 * y la distribución global de género de sus participantes para mostrarlos en inicio.jsp.
 */
@WebServlet(name = "InicioServlet", value = "/inicio")
public class InicioServlet extends HttpServlet {

    private final PruebaDao pruebaDao = new PruebaDao();
    private final ParticipanteDao participanteDao = new ParticipanteDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Evaluador evaluador = (Evaluador) session.getAttribute("evaluador");

        // 1. Obtenemos las pruebas creadas por este evaluador
        List<Prueba> pruebas = pruebaDao.getPorEvaluador(evaluador.getIdEvaluador());

        // 2. Sumamos el conteo global de participantes evaluados
        int totalParticipantes = 0;
        for (Prueba p : pruebas) {
            totalParticipantes += p.getTotalParticipantes();
        }

        // 3. Obtenemos estadísticas demográficas consolidadas
        Map<String, Double> distribucionSexo = participanteDao.distribucionPorSexoEvaluador(evaluador.getIdEvaluador());

        // 4. Pasamos las variables a la vista inicio.jsp
        request.setAttribute("pruebas", pruebas);
        request.setAttribute("totalPruebas", pruebas.size());
        request.setAttribute("totalParticipantes", totalParticipantes);
        request.setAttribute("distribucionSexo", distribucionSexo);
        request.setAttribute("pestanaActiva", "inicio");

        request.getRequestDispatcher("inicio.jsp").forward(request, response);
    }
}
