package mx.edu.utez.uxvibe.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.uxvibe.model.Investigador;
import mx.edu.utez.uxvibe.model.dao.InvestigadorDao;
import mx.edu.utez.uxvibe.utils.PasswordUtil;

import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet(name = "RegisterServlet", value = "/registro")
public class RegisterServlet extends HttpServlet {

    private final InvestigadorDao investigadorDao = new InvestigadorDao();

    // Mínimo 8 caracteres, una mayúscula, una minúscula y un número
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("registro.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String nombres = request.getParameter("nombres");
        String apellidoPaterno = request.getParameter("apellidoPaterno");
        String apellidoMaterno = request.getParameter("apellidoMaterno");
        String correo = request.getParameter("correo");
        String confirmarCorreo = request.getParameter("confirmarCorreo");
        String password = request.getParameter("password");
        String confirmarPassword = request.getParameter("confirmarPassword");

        request.setAttribute("nombres", nombres);
        request.setAttribute("apellidoPaterno", apellidoPaterno);
        request.setAttribute("apellidoMaterno", apellidoMaterno);
        request.setAttribute("correo", correo);

        String error = validar(nombres, apellidoPaterno, apellidoMaterno, correo, confirmarCorreo, password, confirmarPassword);
        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        String correoNormalizado = correo.trim().toLowerCase();
        if (investigadorDao.correoExiste(correoNormalizado)) {
            request.setAttribute("error", "Ya existe una cuenta registrada con ese correo.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        String hash = PasswordUtil.hashPassword(password, "");

        String nombreCompleto = (nombres.trim() + " " + apellidoPaterno.trim() + " " + apellidoMaterno.trim()).trim();
        Investigador nuevo = new Investigador(0, nombreCompleto, correoNormalizado, hash);

        investigadorDao.create(nuevo);
        response.sendRedirect(request.getContextPath() + "/login?registroExitoso=true");
    }

    private String validar(String nombres, String apellidoPaterno, String apellidoMaterno,
                            String correo, String confirmarCorreo,
                            String password, String confirmarPassword) {
        if (esVacio(nombres) || esVacio(apellidoPaterno) || esVacio(apellidoMaterno)
                || esVacio(correo) || esVacio(confirmarCorreo)
                || esVacio(password) || esVacio(confirmarPassword)) {
            return "Todos los campos son obligatorios.";
        }
        if (!correo.trim().equalsIgnoreCase(confirmarCorreo.trim())) {
            return "Los correos electrónicos no coinciden.";
        }
        if (!password.equals(confirmarPassword)) {
            return "Las contraseñas no coinciden.";
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número.";
        }
        return null;
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
