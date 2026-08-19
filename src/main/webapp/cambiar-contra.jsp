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
    <div style="padding-bottom: 15px">
        <img src="${pageContext.request.contextPath}/assets/images/logo_correo.png" alt="Logo_UXVibe" style="width: 70px; height: 70px;">
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
            </label>
            <div class="input-group">
                <span class="input-group-text border-right-none"><i class="bi bi-lock"></i></span>
                <input type="password" id="inputPassword" class="form-control" name="p1" placeholder="Crea una contraseña" required>
            </div>
            <div id="passwordChecklist" class="mt-2 p-2 bg-light rounded border border-light d-none text-start" style="font-size: 0.85rem;">
                <div class="fw-semibold mb-1 text-dark">Tu contraseña debe contener:</div>
                <ul class="list-unstyled mb-0 ps-1">
                    <li id="req-length" class="text-muted mb-1"><i class="bi bi-circle me-1"></i> Mínimo 8 caracteres</li>
                    <li id="req-upper" class="text-muted mb-1"><i class="bi bi-circle me-1"></i> Una letra mayúscula</li>
                    <li id="req-lower" class="text-muted mb-1"><i class="bi bi-circle me-1"></i> Una letra minúscula</li>
                    <li id="req-number" class="text-muted"><i class="bi bi-circle me-1"></i> Un número</li>
                </ul>
            </div>
        </div>

        <div class="mb-4">
            <label class="form-label fw-bold small text-dark mb-1">
                Confirmar contraseña <span class="text-danger">*</span>
            </label>
            <div class="input-group">
                <span class="input-group-text border-right-none"><i class="bi bi-lock"></i></span>
                <input type="password" id="inputConfirmPassword" class="form-control" name="p2" placeholder="Confirma tu contraseña" required>
            </div>
            <div id="passwordMatchError" class="text-danger mt-1 d-none text-start" style="font-size: 0.85rem;">
                Las contraseñas no coinciden.
            </div>
        </div>

        <button type="submit" class="btn btn-teal w-100 mb-3">Reestablecer contraseña</button>

        <div class="text-center">
            <a href="login.jsp" class="link-teal">Volver al inicio de sesión</a>
        </div>
    </form>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        const passwordInput = document.getElementById('inputPassword');
        const confirmPasswordInput = document.getElementById('inputConfirmPassword');
        const checklist = document.getElementById('passwordChecklist');
        const matchError = document.getElementById('passwordMatchError');
        const form = document.querySelector('form');
        
        const reqs = {
            length: { regex: /.{8,}/, el: document.getElementById('req-length') },
            upper: { regex: /[A-Z]/, el: document.getElementById('req-upper') },
            lower: { regex: /[a-z]/, el: document.getElementById('req-lower') },
            number: { regex: /[0-9]/, el: document.getElementById('req-number') }
        };

        function updateChecklist() {
            const val = passwordInput.value;
            let allValid = true;

            for (const key in reqs) {
                const isValid = reqs[key].regex.test(val);
                const icon = reqs[key].el.querySelector('i');
                
                if (isValid) {
                    reqs[key].el.classList.remove('text-muted');
                    reqs[key].el.classList.add('text-success');
                    icon.classList.remove('bi-circle');
                    icon.classList.add('bi-check-circle-fill');
                } else {
                    reqs[key].el.classList.add('text-muted');
                    reqs[key].el.classList.remove('text-success');
                    icon.classList.add('bi-circle');
                    icon.classList.remove('bi-check-circle-fill');
                    allValid = false;
                }
            }
            return allValid;
        }

        passwordInput.addEventListener('focus', () => {
            checklist.classList.remove('d-none');
        });

        passwordInput.addEventListener('input', updateChecklist);

        function checkMatch() {
            if (confirmPasswordInput.value.length > 0 && confirmPasswordInput.value !== passwordInput.value) {
                matchError.classList.remove('d-none');
                return false;
            } else {
                matchError.classList.add('d-none');
                return true;
            }
        }

        confirmPasswordInput.addEventListener('input', checkMatch);
        passwordInput.addEventListener('input', checkMatch);

        form.addEventListener('submit', function(e) {
            if (!updateChecklist() || !checkMatch()) {
                e.preventDefault();
                checklist.classList.remove('d-none');
                if (!checkMatch()) matchError.classList.remove('d-none');
            }
        });
    });
</script>
</body>
</html>
