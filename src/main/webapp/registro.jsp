<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/images/logoux.png" type="image/png">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>UXVibe | Crea tu cuenta</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/uxvibe.css" rel="stylesheet">
    <style>
        body {
            margin: 0;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
            background-color: #ffffff;
        }
        .split {
            display: flex;
            min-height: 100vh;
        }
        .panel-form {
            flex: 1;
            display: flex;
            align-items: center;
            justify-content: center;
            background: #ffffff;
            padding: 40px 20px;
        }
        .panel-marca {
            flex: 1;
            background-color: #e6decb;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            padding: 40px;
            text-align: center;
        }
        .brand-header {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 12px;
            margin-bottom: 8px;
        }
        .brand-logo-img {
            width: 52px;
            height: 52px;
            object-fit: contain;
        }
        .brand-title {
            font-size: 42px;
            font-weight: 800;
            color: #0E0A07;
            letter-spacing: -0.5px;
            line-height: 1;
        }
        .brand-subtitle {
            font-size: 18px;
            color: #1a202c;
            font-weight: 500;
            max-width: 380px;
            margin: 0 auto 20px auto;
            line-height: 1.3;
        }
        .brand-illustration {
            max-width: 440px;
            width: 100%;
            height: auto;
            margin-top: 10px;
        }
        .form-box {
            width: 100%;
            max-width: 420px;
        }
        .form-box h2 {
            font-size: 26px;
            font-weight: 700;
            color: #000000;
            margin-bottom: 4px;
        }
        .form-box .sub-heading {
            color: #4b5563;
            font-size: 14px;
            margin-bottom: 24px;
        }
        .campo {
            margin-bottom: 14px;
        }
        .campo label {
            display: block;
            font-weight: 700;
            margin-bottom: 5px;
            font-size: 13px;
            color: #000000;
        }
        .input-icon-group {
            position: relative;
        }
        .input-icon-group i.icon-prefix {
            position: absolute;
            left: 12px;
            top: 50%;
            transform: translateY(-50%);
            color: #9ca3af;
            font-size: 16px;
            pointer-events: none;
        }
        .input-icon-group input {
            width: 100%;
            padding: 9px 12px;
            border: 1px solid #d0d5dd;
            border-radius: 6px;
            font-size: 14px;
            box-sizing: border-box;
            color: #1f2937;
            transition: border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
        }
        .input-icon-group.has-icon input {
            padding-left: 36px;
        }
        .input-icon-group input:focus {
            outline: none;
            border-color: #173E45;
            box-shadow: 0 0 0 3px rgba(23, 62, 69, 0.12);
        }
        .input-icon-group input::placeholder {
            color: #9ca3af;
            font-weight: 400;
        }
        .uxv-btn-submit {
            background-color: #173E45;
            color: #ffffff;
            border: none;
            padding: 10px;
            border-radius: 6px;
            font-weight: 600;
            font-size: 14px;
            width: 100%;
            cursor: pointer;
            margin-top: 8px;
            transition: background-color 0.2s ease;
        }
        .uxv-btn-submit:hover {
            background-color: #1d4e57;
            color: #ffffff;
        }
        .link-login {
            color: #3DA5B8;
            text-decoration: none;
            font-weight: 500;
        }
        .link-login:hover {
            text-decoration: underline;
        }
        @media (max-width: 860px) {
            .split { flex-direction: column; }
            .panel-marca { display: none; }
        }
    </style>
</head>
<body>
<div class="split">
    <!-- Left Registration Form Panel -->
    <div class="panel-form">
        <div class="form-box">
            <h2>Crea tu cuenta</h2>
            <p class="sub-heading">Únete y comienza a evaluar experiencias de usuario</p>

            <c:if test="${not empty error}">
                <div class="alert alert-danger py-2 small mb-3">${error}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/registro" method="post">
                <div class="campo">
                    <label>Nombre(s) <span class="text-danger">*</span></label>
                    <div class="input-icon-group has-icon">
                        <i class="bi bi-person icon-prefix"></i>
                        <input type="text" name="nombres" placeholder="Ingresa tu nombre(s)" value="${nombres}" required>
                    </div>
                </div>

                <div class="campo">
                    <label>Apellido paterno <span class="text-danger">*</span></label>
                    <div class="input-icon-group">
                        <input type="text" name="apellidoPaterno" placeholder="Ingresa tu apellido paterno" value="${apellidoPaterno}" required>
                    </div>
                </div>

                <div class="campo">
                    <label>Apellido materno <span class="text-danger">*</span></label>
                    <div class="input-icon-group">
                        <input type="text" name="apellidoMaterno" placeholder="Ingresa tu apellido materno" value="${apellidoMaterno}" required>
                    </div>
                </div>

                <div class="campo">
                    <label>Correo electrónico <span class="text-danger">*</span></label>
                    <div class="input-icon-group has-icon">
                        <i class="bi bi-envelope icon-prefix"></i>
                        <input type="email" name="correo" placeholder="Ingresa tu correo electrónico" value="${correo}" required>
                    </div>
                </div>

                <div class="campo">
                    <label>Confirmar correo electrónico <span class="text-danger">*</span></label>
                    <div class="input-icon-group has-icon">
                        <i class="bi bi-envelope icon-prefix"></i>
                        <input type="email" name="confirmarCorreo" placeholder="Confirma tu correo electrónico" required>
                    </div>
                </div>

                <div class="campo">
                    <label>
                        Contraseña <span class="text-danger">*</span> 
                        <i class="bi bi-info-circle ms-1 text-secondary" style="cursor:pointer;" title="Mínimo 8 caracteres, una mayúscula, una minúscula y un número"></i>
                    </label>
                    <div class="input-icon-group has-icon">
                        <i class="bi bi-lock icon-prefix"></i>
                        <input type="password" name="password" placeholder="Crea una contraseña"
                               title="Mínimo 8 caracteres, una mayúscula, una minúscula y un número" required>
                    </div>
                </div>

                <div class="campo">
                    <label>Confirmar contraseña <span class="text-danger">*</span></label>
                    <div class="input-icon-group has-icon">
                        <i class="bi bi-lock icon-prefix"></i>
                        <input type="password" name="confirmarPassword" placeholder="Confirma tu contraseña" required>
                    </div>
                </div>

                <button type="submit" class="uxv-btn-submit">Crear cuenta</button>

                <p class="text-center small mt-3 text-muted">
                    ¿Ya tienes cuenta? <a href="login.jsp" class="link-login">Iniciar sesión</a>
                </p>
            </form>
        </div>
    </div>

    <!-- Right Brand Panel -->
    <div class="panel-marca">
        <div class="brand-header">
            <img src="${pageContext.request.contextPath}/assets/images/logoux.png" alt="UX Logo" class="brand-logo-img">
            <span class="brand-title">UXVibe</span>
        </div>
        <p class="brand-subtitle" style="text-align: justify;">Plataforma para la evaluación de experiencia de usuario</p>
        <img src="${pageContext.request.contextPath}/assets/images/ux-illustration.svg" alt="Ilustración UXVibe" class="brand-illustration">
    </div>
</div>
</body>
</html>
