<%-- 
    ===================================================================
    Layout Principal: Encabezado y Barra Lateral — UXVibe
    Descripción: Componente común incluido en todas las vistas privadas.
    Contiene la cabecera HTML, enlaces a Bootstrap/Icons/CSS, la barra
    lateral fija de navegación y el modal de confirmación de cierre de sesión.
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
    <title>UXVibe</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/uxvibe.css" rel="stylesheet">
</head>
<body>
<div class="uxv-layout">
    <div class="uxv-sidebar">
        <div class="logo">
            <img src="${pageContext.request.contextPath}/assets/images/logoUXVibe.png" alt="Logo_UXVibe" style="width: 38px; height: 38px;">
        </div>
        <a href="${pageContext.request.contextPath}/inicio"
           class="${pestanaActiva == 'inicio' ? 'activo' : ''}" title="Inicio">
            <i class="bi bi-house-door"></i>
        </a>
        <a href="${pageContext.request.contextPath}/perfil"
           class="${pestanaActiva == 'perfil' ? 'activo' : ''}" title="Perfil">
            <i class="bi bi-person"></i>
        </a>
        <div class="spacer"></div>
        <a href="#" title="Cerrar sesión" data-bs-toggle="modal" data-bs-target="#modalLogout">
            <i class="bi bi-box-arrow-right"></i>
        </a>
    </div>

    <!-- Modal Cierre de Sesión -->
    <div class="modal fade" id="modalLogout" tabindex="-1" aria-labelledby="modalLogoutLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow" style="border-radius: 8px;">
                <div class="modal-body p-4 text-center">
                    <div class="mb-3">
                        <i class="bi bi-exclamation-circle" style="font-size: 5rem; color: #C29B77;"></i>
                    </div>
                    <h4 class="fw-bold mb-3">Cierre de sesión</h4>
                    <p class="text-dark mb-4" style="font-size: 1.1rem;">Si deseas salir haz clic en Cerrar sesión o en Cancelar para continuar trabajando</p>
                    <div class="d-flex justify-content-center gap-3">
                        <button type="button" class="btn px-4 py-2" style="border-color: #6c757d; color: #495057; width: 140px;" data-bs-dismiss="modal">Cancelar</button>
                        <a href="${pageContext.request.contextPath}/logout" class="btn text-white px-4 py-2" style="background-color: #d9534f; border-color: #d9534f; width: 140px;">Cerrar Sesión</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="uxv-contenido">
