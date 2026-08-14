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

@WebServlet(name = "InicioServlet", value = "/inicio")
public class InicioServlet extends HttpServlet {

    private final PruebaDao pruebaDao = new PruebaDao();
    private final ParticipanteDao participanteDao = new ParticipanteDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Evaluador evaluador = (Evaluador) session.getAttribute("evaluador");

        List<Prueba> pruebas = pruebaDao.getPorEvaluador(evaluador.getIdEvaluador());

        int totalParticipantes = 0;
        for (Prueba p : pruebas) {
            totalParticipantes += p.getTotalParticipantes();
        }

        request.setAttribute("pruebas", pruebas);
        request.setAttribute("totalPruebas", pruebas.size());
        request.setAttribute("totalParticipantes", totalParticipantes);
        request.setAttribute("pestanaActiva", "inicio");

        request.getRequestDispatcher("inicio.jsp").forward(request, response);
    }
}
