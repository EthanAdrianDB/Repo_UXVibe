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
            ${not empty participante.nombre ? participante.nombre.concat(' ').concat(participante.apellidoP) : 'Participante #'.concat(participante.idParticipante)}
        </h3>
    </div>

    <!-- Contenido en 2 columnas -->
    <div class="row g-4">
        <!-- Columna Izquierda: Datos del Participante y Audio -->
        <div class="col-md-5">
            <div class="uxv-tarjeta mb-4">
                <h5 class="fw-bold text-dark mb-3">
                    ${not empty participante.nombre ? participante.nombre.concat(' ').concat(participante.apellidoP) : 'Participante #'.concat(participante.idParticipante)}
                </h5>

                <div class="mb-3">
                    <span class="text-muted small d-block">Edad</span>
                    <span class="fw-semibold text-dark">${not empty participante.edad && participante.edad > 0 ? participante.edad : 'No especificada'}</span>
                </div>

                <div class="mb-3">
                    <span class="text-muted small d-block">Sexo</span>
                    <span class="fw-semibold text-dark">
                        ${participante.sexo == 0 ? 'Femenino' : 'Masculino'}
                    </span>
                </div>

                <div class="mb-3">
                    <span class="text-muted small d-block">Fecha de realización</span>
                    <span class="fw-semibold text-dark">${not empty participante.fechaRealizacion ? participante.fechaRealizacion : 'No especificada'}</span>
                </div>

                <div class="mb-3">
                    <span class="text-muted small d-block">Duración de la prueba</span>
                    <span class="fw-semibold text-dark">${not empty participante.duracionFormateada && participante.duracionFormateada != '0:00' ? participante.duracionFormateada : 'No especificada'}</span>
                </div>

                <!-- Reproductor de Audio -->
                <div class="mt-4 pt-3 border-top">
                    <span class="text-muted small fw-medium d-block mb-2">Audio de la sesión</span>
                    <c:choose>
                        <c:when test="${tieneAudio}">
                            <audio controls class="w-100 rounded" style="background-color: #f1f3f4;">
                                <source src="${pageContext.request.contextPath}/audio?action=play&idPrueba=${participante.idPrueba}&idParticipante=${participante.idParticipante}" type="audio/webm">
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
                <h6 class="fw-bold text-dark mb-3">Respuestas del Cuestionario</h6>

                <c:choose>
                    <c:when test="${empty respuesta}">
                        <div class="text-center py-4 text-muted small">
                            <i class="bi bi-card-checklist fs-1 d-block mb-2"></i>
                            No hay respuestas registradas para este participante.
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="d-flex flex-column gap-3">
                            <div class="d-flex justify-content-between align-items-center border-bottom pb-2">
                                <span class="small text-muted flex-grow-1 me-3">Pregunta 1</span>
                                <span class="badge bg-dark px-2 py-1 fw-bold">${respuesta.r1} / 5</span>
                            </div>
                            <div class="d-flex justify-content-between align-items-center border-bottom pb-2">
                                <span class="small text-muted flex-grow-1 me-3">Pregunta 2</span>
                                <span class="badge bg-dark px-2 py-1 fw-bold">${respuesta.r2} / 5</span>
                            </div>
                            <div class="d-flex justify-content-between align-items-center border-bottom pb-2">
                                <span class="small text-muted flex-grow-1 me-3">Pregunta 3</span>
                                <span class="badge bg-dark px-2 py-1 fw-bold">${respuesta.r3} / 5</span>
                            </div>
                            <div class="d-flex justify-content-between align-items-center border-bottom pb-2">
                                <span class="small text-muted flex-grow-1 me-3">Pregunta 4</span>
                                <span class="badge bg-dark px-2 py-1 fw-bold">${respuesta.r4} / 5</span>
                            </div>
                            <div class="d-flex justify-content-between align-items-center border-bottom pb-2">
                                <span class="small text-muted flex-grow-1 me-3">Pregunta 5</span>
                                <span class="badge bg-dark px-2 py-1 fw-bold">${respuesta.r5} / 5</span>
                            </div>
                            <div class="d-flex justify-content-between align-items-center border-bottom pb-2">
                                <span class="small text-muted flex-grow-1 me-3">Pregunta 6</span>
                                <span class="badge bg-dark px-2 py-1 fw-bold">${respuesta.r6} / 5</span>
                            </div>
                            <div class="d-flex justify-content-between align-items-center border-bottom pb-2">
                                <span class="small text-muted flex-grow-1 me-3">Pregunta 7</span>
                                <span class="badge bg-dark px-2 py-1 fw-bold">${respuesta.r7} / 5</span>
                            </div>
                            <div class="d-flex justify-content-between align-items-center border-bottom pb-2">
                                <span class="small text-muted flex-grow-1 me-3">Pregunta 8</span>
                                <span class="badge bg-dark px-2 py-1 fw-bold">${respuesta.r8} / 5</span>
                            </div>
                            <div class="d-flex justify-content-between align-items-center border-bottom pb-2">
                                <span class="small text-muted flex-grow-1 me-3">Pregunta 9</span>
                                <span class="badge bg-dark px-2 py-1 fw-bold">${respuesta.r9} / 5</span>
                            </div>
                            <div class="d-flex justify-content-between align-items-center border-bottom pb-2">
                                <span class="small text-muted flex-grow-1 me-3">Pregunta 10</span>
                                <span class="badge bg-dark px-2 py-1 fw-bold">${respuesta.r10} / 5</span>
                            </div>
                            <div class="d-flex justify-content-between align-items-center border-bottom pb-2">
                                <span class="small text-muted flex-grow-1 me-3">Pregunta 11</span>
                                <span class="badge bg-dark px-2 py-1 fw-bold">${respuesta.r11} / 5</span>
                            </div>
                            <div class="d-flex justify-content-between align-items-center border-bottom pb-2">
                                <span class="small text-muted flex-grow-1 me-3">Pregunta 12</span>
                                <span class="badge bg-dark px-2 py-1 fw-bold">${respuesta.r12} / 5</span>
                            </div>
                            <div class="d-flex justify-content-between align-items-center border-bottom pb-2">
                                <span class="small text-muted flex-grow-1 me-3">Pregunta 13</span>
                                <span class="badge bg-dark px-2 py-1 fw-bold">${respuesta.r13} / 5</span>
                            </div>
                            <div class="d-flex justify-content-between align-items-center border-bottom pb-2">
                                <span class="small text-muted flex-grow-1 me-3">Pregunta 14</span>
                                <span class="badge bg-dark px-2 py-1 fw-bold">${respuesta.r14} / 5</span>
                            </div>
                            <div class="d-flex justify-content-between align-items-center">
                                <span class="small text-muted flex-grow-1 me-3">Pregunta 15</span>
                                <span class="badge bg-dark px-2 py-1 fw-bold">${respuesta.r15} / 5</span>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Resultado SAM -->
            <c:if test="${not empty respuesta}">
            <div class="uxv-tarjeta">
                <h6 class="fw-bold text-dark mb-3">Resultado SAM</h6>
                <div class="row text-center g-2">
                    <div class="col-4">
                        <div class="p-2 border rounded bg-light">
                            <i class="bi bi-emoji-smile fs-3 text-secondary d-block"></i>
                            <span class="small fw-semibold text-muted">Valencia (SAM 1)</span>
                            <div class="fw-bold text-dark">${respuesta.sam1}</div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="p-2 border rounded bg-light">
                            <i class="bi bi-lightning-charge fs-3 text-secondary d-block"></i>
                            <span class="small fw-semibold text-muted">Activación (SAM 2)</span>
                            <div class="fw-bold text-dark">${respuesta.sam2}</div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="p-2 border rounded bg-light">
                            <i class="bi bi-sliders fs-3 text-secondary d-block"></i>
                            <span class="small fw-semibold text-muted">Dominancia (SAM 3)</span>
                            <div class="fw-bold text-dark">${respuesta.sam3}</div>
                        </div>
                    </div>
                </div>
            </div>
            </c:if>
        </div>
    </div>
</div>

<%@ include file="layout/footer.jsp" %>
