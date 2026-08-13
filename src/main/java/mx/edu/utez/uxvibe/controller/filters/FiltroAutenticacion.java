package mx.edu.utez.uxvibe.controller.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// El filtro se aplica a todas las URLs de la app
@WebFilter("/*")
public class FiltroAutenticacion extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);

        boolean loggedIn = (session != null && session.getAttribute("evaluador") != null);

        // Rutas públicas: login, registro, recuperación de contraseña,
        // y el cuestionario (lo llena el PARTICIPANTE, no el evaluador logueado)
        boolean rutaPublica =
                requestURI.endsWith("login.jsp") ||
                requestURI.endsWith("/login") ||
                requestURI.endsWith("registro.jsp") ||
                requestURI.endsWith("/registro") ||
                requestURI.endsWith("recuperar-contra.jsp") ||
                requestURI.endsWith("colocar-codigo.jsp") ||
                requestURI.endsWith("cambiar-contra.jsp") ||
                requestURI.endsWith("/recuperar") ||
                requestURI.contains("/cuestionario") ||
                requestURI.endsWith("terminos.jsp") ||
                requestURI.endsWith("index.jsp") ||
                requestURI.endsWith("/");

        boolean esRecurso = requestURI.contains("/assets/") || requestURI.contains("/layout/");

        boolean esLoginORegistro =
                requestURI.endsWith("login.jsp") ||
                requestURI.endsWith("/login") ||
                requestURI.endsWith("registro.jsp") ||
                requestURI.endsWith("/registro");

        if (loggedIn) {
            if (esLoginORegistro) {
                response.sendRedirect(request.getContextPath() + "/inicio");
            } else {
                chain.doFilter(request, response);
            }
        } else {
            if (rutaPublica || esRecurso) {
                chain.doFilter(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }
    }
}
