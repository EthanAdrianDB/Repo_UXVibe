<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="layout/header.jsp" %>
<%@ include file="prueba-tabs.jspf" %>

<div class="container-fluid p-0">
    <!-- 4 Tarjetas Métricas Principales -->
    <div class="row g-3 mb-4">
        <div class="col-6 col-md-3">
            <div class="uxv-tarjeta text-center">
                <div class="valor text-dark">${prueba.totalParticipantes}</div>
                <div class="subtitulo text-muted fw-medium">Participantes</div>
            </div>
        </div>
        <div class="col-6 col-md-3">
            <div class="uxv-tarjeta text-center">
                <div class="valor text-dark">${not empty edadPromedio ? edadPromedio : 'N/A'}</div>
                <div class="subtitulo text-muted fw-medium">Edad promedio</div>
            </div>
        </div>
        <div class="col-6 col-md-3">
            <div class="uxv-tarjeta text-center">
                <div class="valor text-dark">${not empty satisfaccionPromedio ? satisfaccionPromedio : 'N/A'}</div>
                <div class="subtitulo text-muted fw-medium">Satisfacción promedio</div>
            </div>
        </div>
        <div class="col-6 col-md-3">
            <div class="uxv-tarjeta text-center">
                <div class="valor text-dark">${not empty recomendarian ? recomendarian : 'N/A'}</div>
                <div class="subtitulo text-muted fw-medium">Recomendarían</div>
            </div>
        </div>
    </div>

    <!-- Sección de Gráficos (Likert y Distribución por Sexo) -->
    <div class="row g-3 mb-4">
        <!-- Promedio por pregunta (Escala Likert) -->
        <div class="col-md-7">
            <div class="uxv-tarjeta h-100">
                <h6 class="fw-bold text-dark mb-3">Promedio por pregunta (Escala Likert)</h6>
                <c:choose>
                    <c:when test="${empty promedioPorPregunta}">
                        <div class="text-center py-4">
                            <i class="bi bi-bar-chart text-muted fs-1 mb-2 d-block"></i>
                            <span class="text-muted small">Todavía no hay suficientes datos para mostrar la escala Likert.</span>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${promedioPorPregunta}" var="entry">
                            <c:if test="${!entry.key.startsWith('SAM_')}">
                                <div class="d-flex align-items-center gap-3 mb-2">
                                    <span class="small fw-semibold text-muted" style="width:100px;">${entry.key}</span>
                                    <div class="flex-grow-1 bg-light rounded overflow-hidden" style="height:14px;">
                                        <div class="rounded" style="height:14px; width:${(entry.value / 5.0) * 100}%; background-color: var(--uxv-secundario);"></div>
                                    </div>
                                    <span class="small fw-bold" style="width:30px;">${entry.value}</span>
                                </div>
                            </c:if>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Distribución por sexo -->
        <div class="col-md-5">
            <div class="uxv-tarjeta h-100">
                <h6 class="fw-bold text-dark mb-3">Distribución por sexo</h6>
                <c:choose>
                    <c:when test="${empty distribucionSexo}">
                        <div class="text-center py-4">
                            <i class="bi bi-pie-chart text-muted fs-1 mb-2 d-block"></i>
                            <span class="text-muted small">Sin información demográfica registrada aún.</span>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${distribucionSexo}" var="entry">
                            <div class="d-flex align-items-center gap-3 mb-2">
                                <span class="small fw-semibold text-muted" style="width:80px;">${entry.key}</span>
                                <div class="flex-grow-1 bg-light rounded overflow-hidden" style="height:14px;">
                                    <div class="bg-dark rounded" style="height:14px; width:${entry.value}%;"></div>
                                </div>
                                <span class="small fw-bold" style="width:40px;">${entry.value}%</span>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Sección Escala SAM (Valencia, Activación, Dominancia) -->
    <h6 class="fw-bold text-dark mb-3">Evaluación Emocional (Escala SAM)</h6>
    <div class="row g-3">
        <!-- Valencia -->
        <div class="col-md-4">
            <div class="uxv-tarjeta text-center py-3">
                <h6 class="fw-bold text-dark m-0">Valencia</h6>
                <p class="small text-muted mb-3">¿Qué tan agradable fue la experiencia?</p>
                <div class="d-flex justify-content-center gap-2 mb-3">
                    <i class="bi bi-emoji-smile fs-3 text-secondary"></i>
                    <i class="bi bi-emoji-neutral fs-3 text-secondary"></i>
                    <i class="bi bi-emoji-frown fs-3 text-secondary"></i>
                </div>
                <div class="subtitulo text-muted">Promedio</div>
                <div class="fs-4 fw-bold text-dark">${promedioPorPregunta['SAM_1'] != null ? promedioPorPregunta['SAM_1'] : '0.0'}</div>
            </div>
        </div>

        <!-- Activación -->
        <div class="col-md-4">
            <div class="uxv-tarjeta text-center py-3">
                <h6 class="fw-bold text-dark m-0">Activación</h6>
                <p class="small text-muted mb-3">¿Qué tan estimulado o activo te sentiste?</p>
                <div class="d-flex justify-content-center gap-2 mb-3">
                    <i class="bi bi-lightning-charge fs-3 text-secondary"></i>
                    <i class="bi bi-activity fs-3 text-secondary"></i>
                    <i class="bi bi-dash-circle fs-3 text-secondary"></i>
                </div>
                <div class="subtitulo text-muted">Promedio</div>
                <div class="fs-4 fw-bold text-dark">${promedioPorPregunta['SAM_2'] != null ? promedioPorPregunta['SAM_2'] : '0.0'}</div>
            </div>
        </div>

        <!-- Dominancia -->
        <div class="col-md-4">
            <div class="uxv-tarjeta text-center py-3">
                <h6 class="fw-bold text-dark m-0">Dominancia</h6>
                <p class="small text-muted mb-3">¿Qué tanto sentiste que tenías control?</p>
                <div class="d-flex justify-content-center gap-2 mb-3">
                    <i class="bi bi-sliders fs-3 text-secondary"></i>
                    <i class="bi bi-gear fs-3 text-secondary"></i>
                    <i class="bi bi-controller fs-3 text-secondary"></i>
                </div>
                <div class="subtitulo text-muted">Promedio</div>
                <div class="fs-4 fw-bold text-dark">${promedioPorPregunta['SAM_3'] != null ? promedioPorPregunta['SAM_3'] : '0.0'}</div>
            </div>
        </div>
    </div>
</div>

<%@ include file="layout/footer.jsp" %>
