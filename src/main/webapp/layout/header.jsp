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
        <a href="${pageContext.request.contextPath}/logout" title="Cerrar sesión"
           onclick="return confirm('¿Cerrar sesión?');">
            <i class="bi bi-box-arrow-right"></i>
        </a>
    </div>

    <div class="uxv-contenido">
