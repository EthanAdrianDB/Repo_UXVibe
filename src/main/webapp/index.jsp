<%-- 
    Punto de entrada de la aplicación web.
    Redirige automáticamente al inicio de sesión (login.jsp).
--%>
<%
    response.sendRedirect(request.getContextPath() + "/login.jsp");
%>
