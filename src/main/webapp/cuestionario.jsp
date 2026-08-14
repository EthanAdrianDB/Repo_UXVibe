<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>UXVibe | Cuestionario de Evaluación</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/uxvibe.css" rel="stylesheet">
    <style>
        .opcion-escala {
            width:40px; height:40px; border-radius:50%; border:2px solid #CAD3D2;
            display:flex; align-items:center; justify-content:center; font-weight:bold; cursor:pointer;
            transition: all 0.2s ease;
        }
        input[type=radio] { display:none; }
        input[type=radio]:checked + .opcion-escala { border-color:#173E45; background:#173E45; color: white; transform: scale(1.1); }
    </style>
</head>
<body class="bg-light">
<div class="container py-5" style="max-width:800px;">
    <div class="card shadow-sm">
        <div class="card-body p-4">
            <h4 class="text-center mb-1">CUESTIONARIO DE EVALUACIÓN</h4>
            <p class="text-center text-muted small border-bottom pb-3">Por favor responde a todas las preguntas</p>

            <form action="${pageContext.request.contextPath}/cuestionario" method="post" id="formPregunta">
                <input type="hidden" name="action" value="responder">

                <!-- 15 Preguntas tipo Likert -->
                <h5 class="mt-4 mb-3 text-primary border-bottom pb-2">Sección 1: Preguntas Generales</h5>
                <p class="text-muted small mb-4">Califica del 1 al 5 (donde 1 es Totalmente en desacuerdo y 5 es Totalmente de acuerdo)</p>
                <c:forEach begin="1" end="15" var="q">
                    <div class="mb-4">
                        <label class="form-label fw-bold">Pregunta R${q}</label>
                        <p class="small text-muted mb-2">Por favor indica tu nivel de acuerdo con esta afirmación.</p>
                        <div class="d-flex justify-content-start gap-3 flex-wrap">
                            <c:forEach begin="1" end="5" var="v">
                                <label>
                                    <input type="radio" name="r${q}" value="${v}" required>
                                    <div class="opcion-escala">${v}</div>
                                </label>
                            </c:forEach>
                        </div>
                    </div>
                </c:forEach>

                <!-- 3 Preguntas SAM -->
                <h5 class="mt-5 mb-3 text-primary border-bottom pb-2">Sección 2: Evaluación SAM</h5>
                <p class="text-muted small mb-4">Califica del 1 al 9 (Escala SAM)</p>
                <c:forEach begin="1" end="3" var="s">
                    <div class="mb-4">
                        <label class="form-label fw-bold">Escala SAM ${s}</label>
                        <div class="d-flex justify-content-start gap-2 flex-wrap">
                            <c:forEach begin="1" end="9" var="v">
                                <label>
                                    <input type="radio" name="sam${s}" value="${v}" required>
                                    <div class="opcion-escala" style="width:35px; height:35px;">${v}</div>
                                </label>
                            </c:forEach>
                        </div>
                    </div>
                </c:forEach>

                <!-- 2 Frecuencias de estado de ánimo -->
                <h5 class="mt-5 mb-3 text-primary border-bottom pb-2">Sección 3: Estado de Ánimo</h5>
                <c:forEach begin="1" end="2" var="f">
                    <div class="mb-4">
                        <label class="form-label fw-bold">Frecuencia de Estado de Ánimo ${f}</label>
                        <p class="small text-muted mb-2">Califica del 1 al 5 la frecuencia.</p>
                        <div class="d-flex justify-content-start gap-3 flex-wrap">
                            <c:forEach begin="1" end="5" var="v">
                                <label>
                                    <input type="radio" name="frecuenciaEstadoAnimo${f}" value="${v}" required>
                                    <div class="opcion-escala">${v}</div>
                                </label>
                            </c:forEach>
                        </div>
                    </div>
                </c:forEach>

                <!-- Botón que abre el modal de confirmación -->
                <button type="button" class="btn text-white w-100 py-3 mt-4" style="background-color: #3b8285;" data-bs-toggle="modal" data-bs-target="#modalConfirmarGuardar">
                    Enviar Cuestionario
                </button>
            </form>
        </div>
    </div>
</div>

<!-- Modal de Confirmación Guardar -->
<div class="modal fade" id="modalConfirmarGuardar" tabindex="-1" aria-labelledby="modalConfirmarGuardarLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow" style="border-radius: 4px;">
            <div class="modal-body p-5 text-center">
                <h5 class="fw-bold mb-5 mt-2">¿Seguro que quieres enviar tus respuestas?</h5>
                <div class="d-flex justify-content-center gap-3">
                    <button type="button" class="btn btn-outline-secondary px-4 py-2" style="width: 130px;" data-bs-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn text-white px-4 py-2" style="background-color: #1a4a5b; width: 130px;" onclick="document.getElementById('formPregunta').submit();">Enviar</button>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
