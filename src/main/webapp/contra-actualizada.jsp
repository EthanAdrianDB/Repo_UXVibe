<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>UXVibe | Contraseña actualizada</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        body { background-color: #7B8E89; min-height: 100vh; display: flex; align-items: center; justify-content: center; margin: 0; font-family: system-ui, -apple-system, sans-serif; }
        .card-box { background: #ffffff; border-radius: 12px; width: 100%; max-width: 440px; padding: 40px 35px; box-shadow: 0 10px 25px rgba(0,0,0,0.15); text-align: center; }
        .logo-circle { width: 70px; height: 70px; background-color: #2D3139; color: #ffffff; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 28px; font-weight: bold; margin: 0 auto 20px auto; }
        .btn-teal { background-color: #173E45; color: #ffffff; font-weight: 600; border: none; padding: 12px; border-radius: 6px; text-decoration: none; display: block; width: 100%; }
        .btn-teal:hover { background-color: #112F35; color: #ffffff; }
    </style>
</head>
<body>

<div class="card-box">
    <div class="logo-circle">
        <i class="bi bi-person-fill"></i>
    </div>

    <h3 class="fw-bold mb-3">¡Contraseña actualizada!</h3>
    <p class="text-muted small mb-4">
        Tu contraseña ha sido restablecida<br>correctamente.
    </p>

    <a href="login.jsp" class="btn btn-teal">Ir al inicio de sesión</a>
</div>

</body>
</html>
