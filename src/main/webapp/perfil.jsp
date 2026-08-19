<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="layout/header.jsp" %>

<%
    boolean modoEdicion = "true".equals(request.getParameter("editar"));
    boolean modoContrasena = "contrasena".equals(request.getParameter("editar"));
%>

<div class="row justify-content-center">
    <div class="col-md-6">

        <c:if test="${param.actualizado == 'info'}">
            <div class="alert alert-success"><i class="bi bi-check-circle-fill"></i> Se actualizaron los datos.</div>
        </c:if>
        <c:if test="${param.actualizado == 'password'}">
            <div class="alert alert-success"><i class="bi bi-check-circle-fill"></i> Tu contraseña se actualizó correctamente.</div>
        </c:if>

        <div class="card shadow-sm">
            <div class="card-body p-4">
                <div class="text-center mb-4">
                    <h4 class="mt-2 mb-0">
                        ${sessionScope.evaluador.nombre}
                        ${sessionScope.evaluador.apellidoP}
                        ${sessionScope.evaluador.apellidoM != null ? sessionScope.evaluador.apellidoM : ''}
                    </h4>
                    <p class="text-muted small">Evaluador UX</p>
                </div>

                <% if (!modoEdicion && !modoContrasena) { %>

                    <!-- ===== MODO VISTA (solo lectura) ===== -->
                    <h6 class="text-primary border-bottom pb-2 bold">Información personal</h6>
                    <p class="mb-1 text-muted small">Nombre(s)</p>
                    <p>${sessionScope.evaluador.nombre}</p>
                    <p class="mb-1 text-muted small">Apellidos</p>
                    <p>${sessionScope.evaluador.apellidoP} ${sessionScope.evaluador.apellidoM != null ? sessionScope.evaluador.apellidoM : ''}</p>
                    <p class="mb-1 text-muted small">Correo electrónico</p>
                    <p>${sessionScope.evaluador.correo}</p>

                    <a href="${pageContext.request.contextPath}/perfil?editar=true" class="btn btn-outline-primary w-100 mb-4">Editar información</a>

                    <h6 class="text-primary border-bottom pb-2">Seguridad de la cuenta</h6>
                    <a href="${pageContext.request.contextPath}/perfil?editar=contrasena" class="btn btn-outline-primary w-100 mb-4">Cambiar contraseña</a>

                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger w-100"
                       onclick="return confirm('¿Cerrar sesión?');">Cerrar sesión
                    </a>

                <% } else if (modoEdicion) { %>

                    <!-- ===== MODO EDICIÓN DE INFORMACIÓN ===== -->
                    <h6 class="text-primary border-bottom pb-2">Editar información</h6>
                    <form action="${pageContext.request.contextPath}/perfil" method="post">
                        <input type="hidden" name="action" value="editarInfo">
                        <div class="mb-3">
                            <label class="form-label small text-muted mb-0">Nombre(s)</label>
                            <input type="text" class="form-control" name="nombre" value="${sessionScope.evaluador.nombre}" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label small text-muted mb-0">Apellido Paterno</label>
                            <input type="text" class="form-control" name="apellidoPaterno" value="${sessionScope.evaluador.apellidoP}" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label small text-muted mb-0">Apellido Materno</label>
                            <input type="text" class="form-control" name="apellidoMaterno" value="${sessionScope.evaluador.apellidoM}">
                        </div>
                        <div class="mb-3">
                            <label class="form-label small text-muted mb-0">Correo electrónico</label>
                            <input type="text" class="form-control" value="${sessionScope.evaluador.correo}" disabled>
                        </div>

                        <div class="d-flex gap-2">
                            <a href="${pageContext.request.contextPath}/perfil" class="btn btn-outline-secondary w-50">Cancelar</a>
                            <button type="submit" class="btn btn-primary w-50">Guardar cambios</button>
                        </div>
                    </form>

                <% } else { %>

                    <!-- ===== MODO CAMBIAR CONTRASEÑA ===== -->
                    <h6 class="text-primary border-bottom pb-2">Cambiar contraseña</h6>
                    <form action="${pageContext.request.contextPath}/perfil" method="post">
                        <input type="hidden" name="action" value="cambiarContrasena">
                        <div class="mb-2">
                            <label class="form-label small text-muted mb-0">Contraseña
                                <i class="bi bi-info-circle ms-1 text-secondary" style="cursor:pointer;" title="Mínimo 8 caracteres, una mayúscula, una minúscula y un número"></i>
                            </label>
                            <input type="password" class="form-control" name="password" placeholder="Ingresa tu nueva contraseña" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label small text-muted mb-0">Confirmar contraseña</label>
                            <input type="password" class="form-control" name="confirmarPassword" placeholder="Confirma tu contraseña" required>
                        </div>

                        <div class="d-flex gap-2">
                            <a href="${pageContext.request.contextPath}/perfil" class="btn btn-outline-secondary w-50">Cancelar</a>
                            <button type="submit" class="btn btn-dark w-50">Cambiar contraseña</button>
                        </div>
                    </form>

                <% } %>
            </div>
        </div>
    </div>
</div>

<%@ include file="layout/footer.jsp" %>
