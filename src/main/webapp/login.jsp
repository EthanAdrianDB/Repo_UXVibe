<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>UXVibe | Iniciar sesión</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/uxvibe.css" rel="stylesheet">
    <style>
        body { margin:0; font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; background: #fff; }
        .split { display:flex; min-height:100vh; }
        .panel-marca {
            flex:1; background-color: #173E45; color:#fff;
            display:flex; flex-direction:column; align-items:center; justify-content:center; padding:40px; text-align:center;
        }
        .brand-header {
            display: flex; align-items: center; justify-content: center; gap: 12px; margin-bottom: 8px;
        }
        .brand-logo-img { width: 52px; height: 52px; object-fit: contain; filter: brightness(0) invert(1); }
        .brand-title { font-size: 42px; font-weight: 800; color: #ffffff; letter-spacing: -0.5px; line-height: 1; }
        .brand-subtitle { font-size: 18px; color: #e2e8f0; font-weight: 400; max-width: 380px; margin: 0 auto 20px auto; line-height: 1.3; }
        .brand-illustration { max-width: 440px; width: 100%; height: auto; margin-top: 10px; opacity: 0.9; }

        .panel-form { flex:1; display:flex; align-items:center; justify-content:center; background:#fff; padding: 40px 20px; }
        .form-box { width:100%; max-width:400px; }
        .form-box h2 { font-size: 26px; font-weight: 700; color: #000; margin-bottom: 4px; }
        .form-box .sub-heading { color: #4b5563; font-size: 14px; margin-bottom: 24px; }
        .campo { margin-bottom:16px; }
        .campo label { display:block; font-weight:700; margin-bottom:6px; font-size:13px; color: #000; }
        .input-icon-group { position: relative; }
        .input-icon-group i.icon-prefix {
            position: absolute; left: 12px; top: 50%; transform: translateY(-50%);
            color: #9ca3af; font-size: 16px; pointer-events: none;
        }
        .input-icon-group input {
            width:100%; padding:10px 12px 10px 36px; border:1px solid #d0d5dd;
            border-radius:6px; font-size:14px; box-sizing:border-box; color: #1f2937;
        }
        .input-icon-group input:focus { outline:none; border-color: #173E45; box-shadow: 0 0 0 3px rgba(23, 62, 69, 0.12); }
        .uxv-btn-submit {
            background-color: #173E45; color: #ffffff; border: none; padding: 11px;
            border-radius: 6px; font-weight: 600; font-size: 14px; width: 100%; cursor: pointer; transition: background-color 0.2s ease;
        }
        .uxv-btn-submit:hover { background-color: #1d4e57; color: #fff; }
        .link-teal { color: #3DA5B8; text-decoration: none; font-weight: 500; }
        .link-teal:hover { text-decoration: underline; }
        @media (max-width:860px){ .panel-marca{ display:none; } }
    </style>
</head>
<body>
<div class="split">
    <!-- Left Dark Teal Brand Panel (Matching Image 3) -->
    <div class="panel-marca">
        <div class="brand-header">
            <img src="${pageContext.request.contextPath}/assets/images/logo-ux.svg" alt="UX Logo" class="brand-logo-img">
            <span class="brand-title">UXVibe</span>
        </div>
        <p class="brand-subtitle">Plataforma para la evaluación de experiencia de usuario</p>
        <img src="${pageContext.request.contextPath}/assets/images/ux-illustration.svg" alt="Ilustración UXVibe" class="brand-illustration">
    </div>

    <!-- Right Login Form Panel -->
    <div class="panel-form">
        <div class="form-box">
            <h2>Iniciar sesión</h2>
            <p class="sub-heading">Ingresa tu información para acceder</p>

            <c:if test="${not empty error}">
                <div class="alert alert-danger py-2 small mb-3">${error}</div>
            </c:if>
            <c:if test="${not empty sessionScope.mensaje}">
                <div class="alert alert-info py-2 small mb-3">${sessionScope.mensaje}</div>
                <% session.removeAttribute("mensaje"); %>
            </c:if>
            <c:if test="${param.registroExitoso == 'true'}">
                <div class="alert alert-success py-2 small mb-3">Cuenta creada con éxito. Ya puedes iniciar sesión.</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="post">
                <div class="campo">
                    <label for="correo">Correo electrónico:</label>
                    <div class="input-icon-group">
                        <i class="bi bi-envelope icon-prefix"></i>
                        <input type="email" id="correo" name="correo" placeholder="Ingresa tu correo electrónico" required>
                    </div>
                </div>

                <div class="campo">
                    <label for="password">Contraseña:</label>
                    <div class="input-icon-group">
                        <i class="bi bi-lock icon-prefix"></i>
                        <input type="password" id="password" name="password" placeholder="Ingresa tu contraseña" required>
                    </div>
                </div>

                <p class="text-end mb-4">
                    <a href="recuperar-contra.jsp" class="small link-teal">¿Olvidaste tu contraseña?</a>
                </p>

                <button type="submit" class="uxv-btn-submit">Iniciar sesión</button>

                <p class="text-center small mt-3 text-muted">
                    ¿No tienes cuenta? <a href="registro.jsp" class="link-teal">Regístrate</a>
                </p>
            </form>
        </div>
    </div>
</div>
</body>
</html>
