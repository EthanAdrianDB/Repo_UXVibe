<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/images/logoux.png" type="image/png">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>UXVibe | Recuperar contraseña</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        body { background-color: #7B8E89; min-height: 100vh; display: flex; align-items: center; justify-content: center; margin: 0; font-family: system-ui, -apple-system, sans-serif; }
        .card-box { background: #ffffff; border-radius: 12px; width: 100%; max-width: 440px; padding: 40px 35px; box-shadow: 0 10px 25px rgba(0,0,0,0.15); text-align: center; }
        .logo-circle { width: 70px; height: 70px; background-color: #2D3139; color: #ffffff; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 28px; font-weight: bold; margin: 0 auto 20px auto; }
        .btn-teal { background-color: #173E45; color: #ffffff; font-weight: 600; border: none; padding: 12px; border-radius: 6px; }
        .btn-teal:hover { background-color: #112F35; color: #ffffff; }
        .link-teal { color: #178096; text-decoration: none; font-size: 14px; }
        .link-teal:hover { text-decoration: underline; }
        .input-group-text { background: #ffffff; border-right: none; color: #aaa; }
        .form-control { border-left: none; }
        .form-control:focus { box-shadow: none; border-color: #dee2e6; }
    </style>
</head>
<body>

<div class="card-box">
    <div style="padding-bottom: 15px">
        <img src="${pageContext.request.contextPath}/assets/images/logo_correo.png" alt="Logo_UXVibe" style="width: 60px; height: 60px;">
    </div>

    <h3 class="fw-bold mb-2">Recuperar contraseña</h3>
    <p class="text-muted small mb-4">Ingresa tu correo electrónico y te enviaremos un enlace para restablecer tu contraseña</p>

    <c:if test="${not empty error}">
        <div class="alert alert-danger py-2 small mb-3">${error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/recuperar" method="post" class="text-start">
        <input type="hidden" name="action" value="solicitar">
        
        <div class="mb-4">
            <label class="form-label fw-bold small text-dark mb-1">Correo electrónico:</label>
            <div class="input-group">
                <span class="input-group-text"><i class="bi bi-envelope"></i></span>
                <input type="email" class="form-control" name="correo" placeholder="Ingresa tu correo electrónico" required>
            </div>
        </div>

        <button type="submit" class="btn btn-teal w-100 mb-3">Enviar enlace</button>

        <div class="text-center">
            <a href="login.jsp" class="link-teal">Volver al inicio de sesión</a>
        </div>
    </form>
</div>

</body>
</html>
