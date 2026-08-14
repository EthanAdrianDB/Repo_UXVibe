<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="layout/header.jsp" %>

<div class="container-fluid py-2">
    <!-- Encabezado de Bienvenida -->
    <h2 class="fw-bold mb-4">Bienvenido, ${sessionScope.evaluador.nombre != null ? sessionScope.evaluador.nombre : 'Investigador'}</h2>

    <!-- Tarjetas Métricas Superiores -->
    <div class="row g-3 mb-4">
        <div class="col-md-6 col-lg-5">
            <div class="uxv-tarjeta d-flex align-items-center gap-3">
                <div class="bg-light rounded p-3 text-dark fs-2">
                    <i class="bi bi-file-earmark-text"></i>
                </div>
                <div>
                    <div class="valor text-dark">${totalPruebas != null ? totalPruebas : 0}</div>
                    <div class="titulo text-dark">Pruebas registradas</div>
                    <div class="subtitulo">Total de pruebas creadas</div>
                </div>
            </div>
        </div>
        <div class="col-md-6 col-lg-5">
            <div class="uxv-tarjeta d-flex align-items-center gap-3">
                <div class="bg-light rounded p-3 text-dark fs-2">
                    <i class="bi bi-people"></i>
                </div>
                <div>
                    <div class="valor text-dark">${totalParticipantes != null ? totalParticipantes : 0}</div>
                    <div class="titulo text-dark">Participantes evaluados</div>
                    <div class="subtitulo">Total de participantes en todas las pruebas</div>
                </div>
            </div>
        </div>
    </div>

    <!-- Sección Pruebas -->
    <div class="mb-3">
        <h4 class="fw-bold text-dark mb-3">Pruebas</h4>
        
        <!-- Controls: Buscador y Botón Crear -->
        <div class="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-3">
            <div class="uxv-search-group flex-grow-1">
                <i class="bi bi-search"></i>
                <input type="text" id="inputBuscarPrueba" class="form-control" placeholder="Buscar por prueba..." onkeyup="filtrarPruebas()">
            </div>
            
            <a href="${pageContext.request.contextPath}/prueba?action=nueva" class="btn uxv-btn-crear px-3 py-2 d-inline-flex align-items-center gap-2">
                <i class="bi bi-plus-lg"></i> Crear prueba
            </a>
        </div>
    </div>

    <!-- Tabla o Estado Vacío -->
    <c:choose>
        <c:when test="${empty pruebas}">
            <div class="uxv-empty-box my-4">
                <i class="bi bi-inbox"></i>
                <h5 class="fw-bold text-dark">No hay pruebas registradas aún</h5>
                <p class="text-muted small mb-3">Crea tu primera prueba de usabilidad para comenzar a evaluar participantes.</p>
                <a href="${pageContext.request.contextPath}/prueba?action=nueva" class="btn uxv-btn-crear px-4 py-2">
                    <i class="bi bi-plus-lg me-1"></i> Crear prueba
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="uxv-tarjeta p-0 overflow-hidden mb-3">
                <div class="table-responsive">
                    <table class="table align-middle mb-0" id="tablaPruebas">
                        <thead class="bg-light text-muted border-bottom">
                            <tr class="text-uppercase small fw-bold">
                                <th style="width: 50px;" class="ps-4">#</th>
                                <th>Nombre</th>
                                <th>URL evaluada</th>
                                <th>Fecha de creación</th>
                                <th>Participantes</th>
                                <th class="text-end pe-4">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${pruebas}" var="prueba" varStatus="i">
                                <tr>
                                    <td class="ps-4 text-muted fw-bold">${i.index + 1}</td>
                                    <td class="fw-semibold text-dark">${prueba.nombre}</td>
                                    <td>
                                        <a href="${prueba.urlSistema}" target="_blank" class="text-decoration-none text-muted">
                                            ${prueba.urlSistema}
                                        </a>
                                    </td>
                                    <td class="text-muted"><jsp:useBean id="now" class="java.util.Date"/><%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %><fmt:formatDate value="${now}" pattern="dd/MM/yyyy" /></td>
                                    <td class="fw-semibold">${prueba.totalParticipantes}</td>
                                    <td class="text-end pe-4">
                                        <div class="d-inline-flex gap-2">
                                            <!-- Ejecutar prueba (Abre Modal de Términos) -->
                                            <button type="button" class="btn btn-sm btn-light border text-dark" title="Ejecutar prueba"
                                               data-tarea="<c:out value="${prueba.descripcion}"/>"
                                               data-url="<c:out value="${prueba.urlSistema}"/>"
                                               onclick="openTerminosModal(${prueba.idPrueba}, this)">
                                                <i class="bi bi-play-fill"></i>
                                            </button>
                                            <!-- Ver resultados -->
                                            <a class="btn btn-sm btn-light border text-dark" title="Ver resultados"
                                               href="${pageContext.request.contextPath}/resultados?idPrueba=${prueba.idPrueba}">
                                                <i class="bi bi-eye"></i>
                                            </a>
                                            <!-- Editar prueba -->
                                            <a class="btn btn-sm btn-light border text-dark" title="Editar prueba"
                                               href="${pageContext.request.contextPath}/prueba?id=${prueba.idPrueba}">
                                                <i class="bi bi-pencil"></i>
                                            </a>
                                            <!-- Eliminar prueba -->
                                            <form action="${pageContext.request.contextPath}/prueba" method="post" class="d-inline"
                                                  onsubmit="return confirm('¿Deseas eliminar esta prueba?');">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="id" value="${prueba.idPrueba}">
                                                <button type="submit" class="btn btn-sm btn-light border text-danger" title="Eliminar prueba">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- Pie de tabla / Paginación -->
                <div class="d-flex justify-content-between align-items-center p-3 bg-white border-top">
                    <span class="text-muted small">1 - ${pruebas.size()} de ${pruebas.size()}</span>
                    <div class="uxv-pagination">
                        <a href="#" class="page-btn text-muted"><i class="bi bi-chevron-left"></i></a>
                        <a href="#" class="page-btn active">1</a>
                        <a href="#" class="page-btn text-muted"><i class="bi bi-chevron-right"></i></a>
                    </div>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script>
function filtrarPruebas() {
    let input = document.getElementById("inputBuscarPrueba").value.toLowerCase();
    let rows = document.querySelectorAll("#tablaPruebas tbody tr");
    rows.forEach(row => {
        let texto = row.innerText.toLowerCase();
        row.style.display = texto.includes(input) ? "" : "none";
    });
}

let currentPruebaId = null;
let currentTareaText = '';
let currentUrlDestino = '';

function openTerminosModal(idPrueba, btnElement) {
    currentPruebaId = idPrueba;
    currentTareaText = btnElement.getAttribute('data-tarea') || 'No hay descripción para esta tarea.';
    currentUrlDestino = btnElement.getAttribute('data-url') || '';
    
    const modalTerminos = new bootstrap.Modal(document.getElementById('modalTerminos'));
    const checkTerminos = document.getElementById('checkAceptoTerminos');
    const btnAceptar = document.getElementById('btnAceptarTerminos');
    
    if (checkTerminos) checkTerminos.checked = false;
    if (btnAceptar) {
        btnAceptar.classList.add('disabled');
    }
    modalTerminos.show();
}

function redirigirEvaluacion(e) {
    e.preventDefault();
    if (currentPruebaId) {
        // Abrir la URL evaluada en una nueva pestaña
        if (currentUrlDestino) {
            window.open(currentUrlDestino, '_blank');
        }
        // Redirigir esta pestaña a la página de evaluación
        window.location.href = "${pageContext.request.contextPath}/evaluacion-investigador.jsp?idPrueba=" + currentPruebaId;
    }
}

document.addEventListener('DOMContentLoaded', function() {
    const checkTerminos = document.getElementById('checkAceptoTerminos');
    if(checkTerminos) {
        checkTerminos.addEventListener('change', function() {
            const btn = document.getElementById('btnAceptarTerminos');
            if(this.checked) {
                btn.classList.remove('disabled');
            } else {
                btn.classList.add('disabled');
            }
        });
    }

    const btnAceptar = document.getElementById('btnAceptarTerminos');
    if(btnAceptar) {
        btnAceptar.addEventListener('click', function(e) {
            e.preventDefault();
            if(!this.classList.contains('disabled')) {
                const modalTerminosEl = document.getElementById('modalTerminos');
                const modalTerminosInstance = bootstrap.Modal.getInstance(modalTerminosEl);
                if (modalTerminosInstance) {
                    modalTerminosInstance.hide();
                }
                
                // Set text and open Tarea modal after a short delay for smooth transition
                document.getElementById('modalTareaContent').innerText = currentTareaText;
                setTimeout(() => {
                    const modalTarea = new bootstrap.Modal(document.getElementById('modalTarea'));
                    modalTarea.show();
                }, 400);
            }
        });
    }
});
</script>

<!-- Modal de Términos y Condiciones -->
<div class="modal fade" id="modalTerminos" tabindex="-1" aria-labelledby="modalTerminosLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content border-0 shadow" style="border-radius: 4px;">
            <div class="modal-body p-5">
                <h4 class="text-center fw-bold mb-4">TÉRMINOS Y CONDICIONES</h4>
                <div class="border border-dark p-4 mb-4 bg-white" style="height: 200px; overflow-y: auto; font-size: 0.9rem;">
                    Al utilizar nuestros servicios de grabación, edición o procesamiento de audio, aceptas cumplir con los siguientes términos y condiciones.
                    <br><br>
                    <strong>1. Objeto del Servicio</strong><br>
                    UX ofrece servicios de grabación en estudio, masterización online, plataforma de almacenamiento y edición de audio. El usuario es responsable de proporcionar el material base o asistir a las sesiones en las condiciones acordadas. y blabla blabla. (En lo que Derick nos da los terminos este que sea base).
                </div>
                <div class="form-check mb-5 d-flex align-items-center gap-2">
                    <input class="form-check-input mt-0" type="checkbox" id="checkAceptoTerminos" style="width: 20px; height: 20px; background-color: #0f3f4a; border-color: #0f3f4a;">
                    <label class="form-check-label ms-2" for="checkAceptoTerminos" style="font-size: 0.95rem;">
                        Acepto los términos y condiciones.
                    </label>
                </div>
                <div class="d-flex justify-content-center gap-3">
                    <button type="button" class="btn btn-outline-secondary px-5 py-2" data-bs-dismiss="modal">No acepto</button>
                    <a href="#" id="btnAceptarTerminos" class="btn text-white px-5 py-2 disabled" style="background-color: #0f3f4a;">Acepto</a>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Modal de Tarea -->
<div class="modal fade" id="modalTarea" tabindex="-1" aria-labelledby="modalTareaLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content border-0 shadow" style="border-radius: 4px;">
            <div class="modal-body p-5">
                <h4 class="text-center fw-bold mb-4">TAREA A REALIZAR</h4>
                <div class="border border-dark p-4 mb-4 bg-white" id="modalTareaContent" style="height: 200px; overflow-y: auto; font-size: 0.9rem;">
                    <!-- La tarea se inyecta por JS -->
                </div>
                <div class="d-flex justify-content-center gap-3 mt-4">
                    <button type="button" class="btn btn-outline-secondary px-5 py-2" data-bs-dismiss="modal">Cancelar</button>
                    <button type="button" id="btnEntendidoTarea" class="btn text-white px-5 py-2" style="background-color: #0f3f4a;" onclick="redirigirEvaluacion(event)">Entendido</button>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="layout/footer.jsp" %>
