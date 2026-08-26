package mx.edu.utez.uxvibe.controller.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filtro de seguridad que intercepta todas las peticiones HTTP (/*).
 * Verifica si el usuario tiene una sesión activa de evaluador antes de dejarlo entrar a pantallas privadas.
 * Si no está logueado y quiere acceder a algo privado, lo manda al login.
 */
@WebFilter("/*")
public class FiltroAutenticacion extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);

        // Revisamos si existe la sesión y si tiene guardado el objeto 'evaluador'
        boolean loggedIn = (session != null && session.getAttribute("evaluador") != null);

        // Lista de rutas que NO requieren login (públicas):
        // Incluye login, registro, recuperación de contraseña y el cuestionario público que responde el participante
        boolean rutaPublica =
                requestURI.endsWith("login.jsp") ||
                requestURI.endsWith("/login") ||
                requestURI.endsWith("registro.jsp") ||
                requestURI.endsWith("/registro") ||
                requestURI.endsWith("recuperar-contra.jsp") ||
                requestURI.endsWith("colocar-codigo.jsp") ||
                requestURI.endsWith("cambiar-contra.jsp") ||
                requestURI.endsWith("correo-enviado.jsp") ||
                requestURI.endsWith("contra-actualizada.jsp") ||
                requestURI.endsWith("/recuperar") ||
                requestURI.contains("/cuestionario") ||
                requestURI.endsWith("terminos.jsp") ||
                requestURI.endsWith("index.jsp") ||
                requestURI.endsWith("/");

        // Recursos estáticos (CSS, JS, imágenes) que siempre deben cargar libremente
        boolean esRecurso = requestURI.contains("/assets/") || requestURI.contains("/layout/");

        boolean esLoginORegistro =
                requestURI.endsWith("login.jsp") ||
                requestURI.endsWith("/login") ||
                requestURI.endsWith("registro.jsp") ||
                requestURI.endsWith("/registro");

        if (loggedIn) {
            // Si ya está logueado e intenta ir a login o registro, lo redirigimos directo a su inicio
            if (esLoginORegistro) {
                response.sendRedirect(request.getContextPath() + "/inicio");
            } else {
                chain.doFilter(request, response);
            }
        } else {
            // Si NO está logueado pero es una ruta pública o recurso estático, lo dejamos pasar
            if (rutaPublica || esRecurso) {
                chain.doFilter(request, response);
            } else {
                // Si intentó entrar a una ruta protegida sin sesión, lo mandamos al login
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }
    }
}
