<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="layout/header.jsp" %>

<div class="container-fluid max-w-900 py-2">
    <!-- Header de navegación de regreso -->
    <div class="d-flex align-items-center gap-3 mb-4">
        <a href="${pageContext.request.contextPath}/inicio" class="text-dark fs-3 text-decoration-none">
            <i class="bi bi-arrow-left"></i>
        </a>
        <h2 class="fw-bold m-0">
            <c:choose>
                <c:when test="${not empty pruebaEditar}">Editar prueba</c:when>
                <c:otherwise>Nueva prueba</c:otherwise>
            </c:choose>
        </h2>
    </div>

    <!-- Formulario centrado/limpio -->
    <div class="row">
        <div class="col-md-9 col-lg-8">
            <form action="${pageContext.request.contextPath}/prueba" method="post" id="formNuevaPrueba">
                <input type="hidden" name="action" value="${not empty pruebaEditar ? 'update' : 'create'}">
                <input type="hidden" name="id" value="${not empty pruebaEditar ? pruebaEditar.idPrueba : 0}">

                <div class="mb-3">
                    <label for="nombre" class="form-label fw-medium">Nombre de la prueba: <span class="text-danger">*</span></label>
                    <input type="text" class="form-control form-control-lg uxv-input" id="nombre" name="nombre" 
                           value="${pruebaEditar.nombreEstudio}" placeholder="Ej. Evaluación de pruebaUX" required>
                </div>

                <div class="mb-3">
                    <label for="url" class="form-label fw-medium">URL: <span class="text-danger">*</span></label>
                    <input type="url" class="form-control form-control-lg uxv-input" id="url" name="url" 
                           value="${pruebaEditar.urlDestino}" placeholder="Ej. https://ejemplo.com" required>
                </div>

                <div class="mb-4">
                    <label for="tarea" class="form-label fw-medium">Tarea: <span class="text-danger">*</span></label>
                    <textarea class="form-control form-control-lg uxv-input" id="tarea" name="tarea" rows="4" 
                              placeholder="Describir brevemente la tarea de la prueba..." required>${pruebaEditar.tareaDescripcion}</textarea>
                </div>

                <div class="d-flex justify-content-end gap-3 pt-2">
                    <a href="${pageContext.request.contextPath}/inicio" class="btn uxv-btn-cancelar px-4 py-2">Cancelar</a>
                    <button type="submit" class="btn uxv-btn-guardar px-4 py-2">Guardar</button>
                </div>
            </form>
        </div>
    </div>
</div>

<%@ include file="layout/footer.jsp" %>
