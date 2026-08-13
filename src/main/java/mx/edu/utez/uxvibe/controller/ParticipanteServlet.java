package mx.edu.utez.uxvibe.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.uxvibe.model.Colaborador;
import mx.edu.utez.uxvibe.model.Prueba;
import mx.edu.utez.uxvibe.model.dao.ParticipanteDao;
import mx.edu.utez.uxvibe.model.dao.PruebaDao;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ParticipanteServlet", value = "/participantes")
public class ParticipanteServlet extends HttpServlet {

    private final ParticipanteDao participanteDao = new ParticipanteDao();
    private final PruebaDao pruebaDao = new PruebaDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idPrueba = Integer.parseInt(request.getParameter("idPrueba"));
        Prueba prueba = pruebaDao.getById(idPrueba);
        List<Colaborador> participantes = participanteDao.getPorPrueba(idPrueba);

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
            Colaborador p = new Colaborador();
            p.setIdPrueba(idPrueba);
            String nombre = request.getParameter("nombre");
            if (nombre != null && !nombre.trim().isEmpty()) {
                p.setNombre(nombre.trim());
            }
            String edadStr = request.getParameter("edad");
            p.setRangoEdad(edadStr != null && !edadStr.trim().isEmpty() ? edadStr.trim() : "18-25");
            String sexo = request.getParameter("sexo");
            p.setGenero(sexo != null && !sexo.trim().isEmpty() ? sexo.trim() : "Otro");
            p.setAvisoConsentimiento(1);
            participanteDao.create(p);
        }

        response.sendRedirect(request.getContextPath() + "/participantes?idPrueba=" + idPrueba);
    }
}
