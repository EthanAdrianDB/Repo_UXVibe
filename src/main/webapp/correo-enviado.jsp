<%-- 
    ===================================================================
    Confirmación de Correo Enviado — UXVibe
    Descripción: Vista informativa que notifica al usuario que se le ha
    enviado el enlace de recuperación de contraseña a su cuenta de correo.
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
    <title>UXVibe | Correo enviado</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        body { background-color: #7B8E89; min-height: 100vh; display: flex; align-items: center; justify-content: center; margin: 0; font-family: system-ui, -apple-system, sans-serif; }
        .card-box { background: #ffffff; border-radius: 12px; width: 100%; max-width: 440px; padding: 40px 35px; box-shadow: 0 10px 25px rgba(0,0,0,0.15); text-align: center; }
        .logo-circle { width: 70px; height: 70px; background-color: #2D3139; color: #ffffff; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 28px; font-weight: bold; margin: 0 auto 20px auto; }
        .box-reenviar { background-color: #E8E2D5; border-radius: 6px; padding: 15px; margin-bottom: 20px; }
        .link-teal { color: #178096; text-decoration: none; font-size: 14px; }
        .link-teal:hover { text-decoration: underline; }
    </style>
</head>
<body>

<div class="card-box">
    <div style="padding-bottom: 15px">
        <img src="${pageContext.request.contextPath}/assets/images/logo_correo.png" alt="Logo_UXVibe" style="width: 70px; height: 70px;">
    </div>

    <h3 class="fw-bold mb-3">¡Correo enviado!</h3>
    <p class="text-muted small mb-3">
        Hemos enviado un enlace de recuperación a tu correo: <br>
        <strong>${param.correo != null ? param.correo : 'tu correo registrado'}</strong>
    </p>
    <p class="text-muted small mb-4, box-reenviar">
        Revisa tu bandeja de entrada y sigue las instrucciones
    </p>


    <div>
        <a href="login.jsp" class="link-teal">Volver al inicio de sesión</a>
    </div>
</div>

</body>
</html>
