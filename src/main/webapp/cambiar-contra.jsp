<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/images/logoux.png" type="image/png">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>UXVibe | Crear nueva contraseña</title>
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
        .input-group-text { background: #ffffff; color: #aaa; }
        .input-group-text.border-left-none { border-left: none; }
        .input-group-text.border-right-none { border-right: none; }
        .form-control { border-left: none; border-right: none; }
        .form-control:focus { box-shadow: none; border-color: #dee2e6; }
    </style>
</head>
<body>

<div class="card-box">
    <div>
        <img src="${pageContext.request.contextPath}/assets/images/logo_correo.png" alt="Logo_UXVibe" style="width: 38px; height: 38px;">
    </div>

    <h3 class="fw-bold mb-4">Crear nueva contraseña</h3>

    <c:if test="${not empty error}">
        <div class="alert alert-danger py-2 small mb-3">${error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/recuperar" method="post" class="text-start">
        <input type="hidden" name="action" value="actualizar">
        <input type="hidden" name="token" value="${param.token != null ? param.token : token}">

        <div class="mb-3">
            <label class="form-label fw-bold small text-dark mb-1">
                Contraseña <span class="text-danger">*</span>
                <i class="bi bi-info-circle text-muted ms-1" title="Mínimo 8 caracteres, una mayúscula, una minúscula y un número"></i>
            </label>
            <div class="input-group">
                <span class="input-group-text border-right-none"><i class="bi bi-lock"></i></span>
                <input type="password" class="form-control" name="p1" placeholder="Crea una contraseña" required>
                <span class="input-group-text border-left-none"><i class="bi bi-info-circle"></i></span>
            </div>
        </div>

        <div class="mb-4">
            <label class="form-label fw-bold small text-dark mb-1">
                Confirmar contraseña: <span class="text-danger">*</span>
                <i class="bi bi-info-circle text-muted ms-1" title="Confirma tu contraseña"></i>
            </label>
            <div class="input-group">
                <span class="input-group-text border-right-none"><i class="bi bi-lock"></i></span>
                <input type="password" class="form-control" name="p2" placeholder="Confirma tu contraseña" required>
                <span class="input-group-text border-left-none"><i class="bi bi-info-circle"></i></span>
            </div>
        </div>

        <button type="submit" class="btn btn-teal w-100 mb-3">Reestablecer contraseña</button>

        <div class="text-center">
            <a href="login.jsp" class="link-teal">Volver al inicio de sesión</a>
        </div>
    </form>
</div>

</body>
</html>
