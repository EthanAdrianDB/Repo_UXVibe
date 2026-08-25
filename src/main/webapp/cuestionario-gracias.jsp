<%-- 
    ===================================================================
    Pantalla de Agradecimiento / Fin de la Evaluación — UXVibe
    Descripción: Vista que se le muestra al participante después de que
    sus respuestas y datos han sido registrados satisfactoriamente en la BD.
    ===================================================================
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/images/logoux.png" type="image/png">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>UXVibe | ¡Gracias!</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/uxvibe.css" rel="stylesheet">
</head>
<body class="bg-light d-flex align-items-center justify-content-center vh-100">
<div class="text-center p-5 bg-white rounded shadow-sm border border-success" style="max-width: 500px;">
    <i class="bi bi-check-circle-fill text-success" style="font-size: 5rem;"></i>
    <h2 class="mt-3 fw-bold text-dark">Evaluación completada</h2>
    <p class="text-muted fs-5 mt-2">¡Gracias por tu participación!</p>
    <p class="text-muted small">Tus respuestas se guardaron correctamente. Ya puedes cerrar esta ventana.</p>
</div>
</body>
</html>
