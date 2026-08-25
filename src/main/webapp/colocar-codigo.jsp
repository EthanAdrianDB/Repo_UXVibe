<%-- 
    ===================================================================
    Verificación de Código de Seguridad — UXVibe
    Controlador: RecuperarServlet (/recuperar?action=checar)
    Descripción: Vista para ingresar el código numérico de verificación
    temporal en flujos de validación de dos pasos o recuperación.
    ===================================================================
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/images/logoux.png" type="image/png">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>UXVibe | Ingresa tu código</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/uxvibe.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style> body { background-color: #778D8B; } </style>
</head>
<body class="d-flex align-items-center justify-content-center min-vh-100 py-4">

<div class="container">
    <div class="row justify-content-center">
        <div class="col-12 col-md-8 col-lg-5">
            <div class="card shadow border-0 rounded-3">
                <div class="card-body p-5">
                    <div class="text-center mb-4">
                        <h2 class="fw-bold h4">Ingresa tu código</h2>
                        <p class="text-muted small">Revisa la consola del servidor: ahí "llega" el correo simulado</p>
                    </div>

                    <c:if test="${param.error == '1'}">
                        <div class="alert alert-danger py-2 small">
                            <i class="bi bi-exclamation-triangle-fill me-1"></i> Código inválido. Intenta de nuevo.
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/recuperar" method="post">
                        <input type="hidden" name="action" value="checar">
                        <div class="form-floating mb-3">
                            <input type="text" class="form-control" id="codigo" name="codigo" placeholder="código" required>
                            <label for="codigo">Código de 6 dígitos</label>
                        </div>
                        <button class="btn btn-dark w-100 py-2 fw-semibold" type="submit">Verificar código</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
