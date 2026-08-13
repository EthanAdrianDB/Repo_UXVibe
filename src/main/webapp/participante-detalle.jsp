<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="layout/header.jsp" %>

<div class="container-fluid p-0">
    <!-- Breadcrumb / Header de Retorno -->
    <div class="d-flex align-items-center gap-2 mb-4">
        <a href="${pageContext.request.contextPath}/participantes?idPrueba=${participante.idPrueba}" class="text-dark fs-3 text-decoration-none">
            <i class="bi bi-arrow-left"></i>
        </a>
        <h3 class="fw-bold m-0 text-muted">Participantes</h3>
        <span class="fs-4 text-muted"><i class="bi bi-chevron-right"></i></span>
        <h3 class="fw-bold m-0 text-dark">
            ${not empty participante.nombre ? participante.nombre : 'Participante #'.concat(participante.idColaborador)}
        </h3>
    </div>

    <!-- Contenido en 2 columnas -->
    <div class="row g-4">
        <!-- Columna Izquierda: Datos del Participante y Audio -->
        <div class="col-md-5">
            <div class="uxv-tarjeta mb-4">
                <h5 class="fw-bold text-dark mb-3">
                    ${not empty participante.nombre ? participante.nombre : 'Participante #'.concat(participante.idColaborador)}
                </h5>

                <div class="mb-3">
                    <span class="text-muted small d-block">Edad</span>
                    <span class="fw-semibold text-dark">${participante.rangoEdad != null ? participante.rangoEdad : '-'}</span>
                </div>

                <div class="mb-3">
                    <span class="text-muted small d-block">Sexo</span>
                    <span class="fw-semibold text-dark">${participante.genero != null ? participante.genero : '-'}</span>
                </div>

                <div class="mb-3">
                    <span class="text-muted small d-block">Fecha de realización</span>
                    <span class="fw-semibold text-dark">${participante.fechaRealizacion != null ? participante.fechaRealizacion : '-'}</span>
                </div>

                <div class="mb-3">
                    <span class="text-muted small d-block">Duración de la prueba</span>
                    <span class="fw-semibold text-dark">${duracionFormateada != null ? duracionFormateada : '1m 23s'}</span>
                </div>

                <!-- Reproductor de Audio -->
                <div class="mt-4 pt-3 border-top">
                    <span class="text-muted small fw-medium d-block mb-2">Audio de la sesión</span>
                    <c:choose>
                        <c:when test="${not empty idSesion}">
                            <audio controls class="w-100 rounded" style="background-color: #f1f3f4;">
                                <source src="${pageContext.request.contextPath}/obtenerAudio?idSesion=${idSesion}" type="audio/webm">
                                Tu navegador no soporta el reproductor de audio.
                            </audio>
                        </c:when>
                        <c:otherwise>
                            <div class="p-3 bg-light rounded text-center text-muted small">
                                <i class="bi bi-mic-mute d-block fs-3 mb-1"></i>
                                Sin grabación de audio registrada para este participante.
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <!-- Columna Derecha: Respuestas Likert y Resultado SAM -->
        <div class="col-md-7">
            <!-- Respuestas escala Likert -->
            <div class="uxv-tarjeta mb-4">
                <h6 class="fw-bold text-dark mb-3">Respuestas escala Likert</h6>

                <c:choose>
                    <c:when test="${empty respuestasLikert}">
                        <div class="text-center py-4 text-muted small">
                            <i class="bi bi-card-checklist fs-1 d-block mb-2"></i>
                            No hay respuestas registradas para este participante.
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="d-flex flex-column gap-3">
                            <c:forEach items="${respuestasLikert}" var="entry" varStatus="i">
                                <div class="d-flex justify-content-between align-items-center border-bottom pb-2">
                                    <span class="small text-muted flex-grow-1 me-3">${i.index + 1}. ${entry.key}</span>
                                    <span class="badge bg-dark px-2 py-1 fw-bold">${entry.value} / 5</span>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>

                <c:if test="${not empty comentarios}">
                    <div class="mt-4 pt-3 border-top">
                        <span class="text-muted small fw-medium d-block mb-1">Comentarios adicionales:</span>
                        <p class="fst-italic bg-light p-3 rounded mb-0 text-dark small">"${comentarios}"</p>
                    </div>
                </c:if>
            </div>

            <!-- Resultado SAM -->
            <div class="uxv-tarjeta">
                <h6 class="fw-bold text-dark mb-3">Resultado SAM</h6>
                <div class="row text-center g-2">
                    <div class="col-4">
                        <div class="p-2 border rounded bg-light">
                            <i class="bi bi-emoji-smile fs-3 text-secondary d-block"></i>
                            <span class="small fw-semibold text-muted">Valencia</span>
                            <div class="fw-bold text-dark">${samValencia != null ? samValencia : '4.2'}</div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="p-2 border rounded bg-light">
                            <i class="bi bi-lightning-charge fs-3 text-secondary d-block"></i>
                            <span class="small fw-semibold text-muted">Activación</span>
                            <div class="fw-bold text-dark">${samActivacion != null ? samActivacion : '4.2'}</div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="p-2 border rounded bg-light">
                            <i class="bi bi-sliders fs-3 text-secondary d-block"></i>
                            <span class="small fw-semibold text-muted">Dominancia</span>
                            <div class="fw-bold text-dark">${samDominancia != null ? samDominancia : '4.2'}</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="layout/footer.jsp" %>
