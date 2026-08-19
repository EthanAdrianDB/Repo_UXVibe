<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/images/logoux.png" type="image/png">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>UXVibe | ${prueba.nombre}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/uxvibe.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-5" style="max-width:520px;">
    <div class="card shadow-sm">
        <div class="card-body p-4">
            <h3 class="text-center mb-1">Formulario de evaluación</h3>
            <p class="text-center text-muted small mb-4">
                Antes de comenzar "${prueba.nombre}", dinos tus datos demográficos
            </p>

            <form action="${pageContext.request.contextPath}/cuestionario" method="post">
                <input type="hidden" name="action" value="iniciar">
                <input type="hidden" name="idPrueba" value="${prueba.idPrueba}">

                <div class="mb-3">
                    <label class="form-label">Nombre(s)</label>
                    <input type="text" class="form-control" name="nombre" placeholder="Ingresa tu nombre(s)" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Apellido Paterno</label>
                    <input type="text" class="form-control" name="apellidoP" placeholder="Ingresa tu apellido paterno" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Apellido Materno (Opcional)</label>
                    <input type="text" class="form-control" name="apellidoM" placeholder="Ingresa tu apellido materno">
                </div>

                <div class="mb-3">
                    <label class="form-label">Edad</label>
                    <input type="number" class="form-control" name="edad" placeholder="Ingresa tu edad (ej. 25)" min="1" max="120" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Sexo</label>
                    <select class="form-select" name="sexo" required>
                        <option value="">Selecciona una opción</option>
                        <option value="0">Femenino</option>
                        <option value="1">Masculino</option>
                    </select>
                </div>

                <div class="mb-4 form-check">
                    <input type="checkbox" class="form-check-input" id="consentimiento" name="consentimiento" value="1" required checked>
                    <label class="form-check-label small text-muted" for="consentimiento">
                        Acepto el aviso de consentimiento para fines de investigación de usabilidad.
                    </label>
                </div>

                <button type="submit" class="btn btn-dark w-100">Comenzar prueba</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
