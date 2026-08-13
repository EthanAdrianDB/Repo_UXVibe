<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>UXVibe | Cuestionario de Satisfacción</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/uxvibe.css" rel="stylesheet">
    <style>
        .opcion-escala {
            width:56px; height:56px; border-radius:50%; border:2px solid #CAD3D2;
            display:flex; align-items:center; justify-content:center; font-weight:bold; cursor:pointer;
            transition: all 0.2s ease;
        }
        input[type=radio] { display:none; }
        input[type=radio]:checked + .opcion-escala { border-color:#173E45; background:#173E45; color: white; transform: scale(1.1); }
    </style>
</head>
<body class="bg-light">
<div class="container py-5" style="max-width:600px;">
    <div class="card shadow-sm">
        <div class="card-body p-4">
            <h4 class="text-center mb-1">CUESTIONARIO DE EVALUACIÓN</h4>
            <p class="text-center text-muted small border-bottom pb-3">Por favor califica tu experiencia general</p>

            <form action="${pageContext.request.contextPath}/cuestionario" method="post" id="formPregunta">
                <input type="hidden" name="action" value="responder">

                <h6 class="text-center mt-4">Escala de Satisfacción (1 al 5)</h6>
                <p class="text-center text-muted small">Donde 1 es muy insatisfecho y 5 es muy satisfecho</p>

                <div class="d-flex justify-content-center gap-3 my-4 flex-wrap">
                    <c:forEach begin="1" end="5" var="v">
                        <label>
                            <input type="radio" name="escalaSatisfaccion" value="${v}" ${v == 5 ? 'checked' : ''} required>
                            <div class="opcion-escala">${v}</div>
                        </label>
                    </c:forEach>
                </div>

                <div class="mb-4">
                    <label class="form-label">Comentarios libres u observaciones</label>
                    <textarea class="form-control" name="comentariosLibres" rows="4" placeholder="Escribe aquí tus impresiones sobre la plataforma..."></textarea>
                </div>

                <!-- Botón que abre el modal de confirmación -->
                <button type="button" class="btn text-white w-100 py-2" style="background-color: #3b8285;" data-bs-toggle="modal" data-bs-target="#modalConfirmarGuardar">
                    Guardar
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
                <h5 class="fw-bold mb-5 mt-2">¿Seguro que quieres guardar?</h5>
                <div class="d-flex justify-content-center gap-3">
                    <button type="button" class="btn btn-outline-secondary px-4 py-2" style="width: 130px;" data-bs-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn text-white px-4 py-2" style="background-color: #1a4a5b; width: 130px;" onclick="document.getElementById('formPregunta').submit();">Guardar</button>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
