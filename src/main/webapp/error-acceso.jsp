<%-- 
    ===================================================================
    Pantalla de Error de Acceso / Permisos — UXVibe
    Descripción: Vista que se muestra cuando un usuario intenta acceder a
    un recurso con un ID inválido, no existente o que pertenece a otro evaluador.
    ===================================================================
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="layout/header.jsp" %>

<div class="container-fluid py-5 d-flex flex-column align-items-center justify-content-center" style="min-height: 70vh;">
    <div class="uxv-tarjeta p-5 text-center shadow-sm" style="max-width: 550px; border-radius: 12px;">
        <div class="mb-4 mx-auto d-flex align-items-center justify-content-center" style="width: 100px; height: 100px; border: 4px solid #C19B76; border-radius: 50%;">
            <i class="bi bi-shield-exclamation" style="font-size: 3.5rem; color: #C19B76;"></i>
        </div>
        <h3 class="fw-bold text-dark mb-3">Acceso Denegado</h3>
        <p class="text-muted fs-5 mb-4">
            El ID proporcionado no es válido, la información no existe o no tienes permisos para acceder a ella.
        </p>
        <a href="${pageContext.request.contextPath}/inicio" class="btn text-white px-4 py-2 fw-semibold" style="background-color: #0f3f4a;">
            <i class="bi bi-house-door me-2"></i> Volver al Inicio
        </a>
    </div>
</div>

<%@ include file="layout/footer.jsp" %>
