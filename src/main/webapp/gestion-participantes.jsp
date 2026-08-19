<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="layout/header.jsp" %>
<%@ include file="prueba-tabs.jspf" %>

<div class="container-fluid p-0">
    <!-- Barra superior de búsqueda y acción -->
    <div class="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-3">
        <div class="uxv-search-group flex-grow-1">
            <i class="bi bi-search"></i>
            <input type="text" id="inputBuscarParticipante" class="form-control" placeholder="Buscar participante..." onkeyup="filtrarParticipantes()">
        </div>
        <!-- Botón eliminado por solicitud -->
    </div>

    <!-- Tabla o Estado Vacío -->
    <c:choose>
        <c:when test="${empty participantes}">
            <div class="uxv-empty-box my-4">
                <i class="bi bi-people"></i>
                <h5 class="fw-bold text-dark">No hay participantes evaluados aún</h5>
            </div>
        </c:when>
        <c:otherwise>
            <div class="uxv-tarjeta p-0 overflow-hidden mb-3">
                <div class="table-responsive">
                    <table class="table align-middle mb-0" id="tablaParticipantes">
                        <thead class="bg-light text-muted border-bottom">
                            <tr class="text-uppercase small fw-bold">
                                <th style="width: 50px;" class="ps-4">#</th>
                                <th>Nombre</th>
                                <th>Edad</th>
                                <th>Sexo</th>
                                <th>Fecha de realización</th>
                                <th class="text-end pe-4">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${participantes}" var="p" varStatus="i">
                                <tr>
                                    <td class="ps-4 text-muted fw-bold">${i.index + 1}</td>
                                    <td class="fw-semibold text-dark">
                                        ${not empty p.nombre ? p.nombre.concat(' ').concat(p.apellidoP) : 'Participante #'.concat(p.idParticipante)}
                                    </td>
                                    <td>${p.edad > 0 ? p.edad : 'N/A'}</td>
                                    <td>${p.sexo == 0 ? 'Femenino' : 'Masculino'}</td>
                                    <td class="text-muted">${not empty p.fechaRealizacion ? p.fechaRealizacion : 'N/A'}</td>
                                    <td class="text-end pe-4">
                                        <div class="d-inline-flex gap-2">
                                            <!-- Ver detalle -->
                                            <a class="btn btn-sm btn-light border text-dark" title="Ver respuestas y detalle"
                                               href="${pageContext.request.contextPath}/participante-detalle?id=${p.idParticipante}">
                                                <i class="bi bi-eye"></i>
                                            </a>
                                            <!-- Eliminar -->
                                            <form action="${pageContext.request.contextPath}/participantes" method="post" class="d-inline" id="formDelete_${p.idParticipante}">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="id" value="${p.idParticipante}">
                                                <input type="hidden" name="idPrueba" value="${prueba.idPrueba}">
                                                <button type="button" class="btn btn-sm btn-light border text-danger" title="Eliminar" onclick="abrirModalEliminar('formDelete_${p.idParticipante}')">
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

                <!-- Pie de Tabla / Paginación -->
                <div class="d-flex justify-content-between align-items-center p-3 bg-white border-top">
                    <span class="text-muted small">1 - ${participantes.size()} de ${participantes.size()}</span>
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
function filtrarParticipantes() {
    let input = document.getElementById("inputBuscarParticipante").value.toLowerCase();
    let rows = document.querySelectorAll("#tablaParticipantes tbody tr");
    rows.forEach(row => {
        let texto = row.innerText.toLowerCase();
        row.style.display = texto.includes(input) ? "" : "none";
    });
}

let formAEliminar = null;
let modalEliminarInstance = null;

function abrirModalEliminar(formId) {
    formAEliminar = document.getElementById(formId);
    if (!modalEliminarInstance) {
        modalEliminarInstance = new bootstrap.Modal(document.getElementById('modalEliminarParticipante'));
    }
    modalEliminarInstance.show();
}

function ejecutarEliminacion() {
    if (formAEliminar) {
        formAEliminar.submit();
    }
}
</script>

<!-- Modal de Confirmación de Eliminación -->
<div class="modal fade" id="modalEliminarParticipante" tabindex="-1" aria-labelledby="modalEliminarLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg" style="border-radius: 12px;">
            <div class="modal-body p-5 text-center">
                <div class="mb-4 mx-auto d-flex align-items-center justify-content-center" style="width: 90px; height: 90px; border: 4px solid #C19B76; border-radius: 50%;">
                    <i class="bi bi-exclamation" style="font-size: 4rem; color: #C19B76;"></i>
                </div>
                <h4 class="fw-bold mb-3 text-dark" id="modalEliminarLabel">Eliminación de participante</h4>
                <p class="text-dark mb-4 fs-6">
                    ¿Está seguro de que desea eliminar a este<br>participante?
                </p>
                <div class="d-flex justify-content-center gap-3">
                    <button type="button" class="btn btn-outline-secondary px-4 py-2 fw-semibold" style="width: 140px; border-color: #6c757d;" data-bs-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn px-4 py-2 fw-semibold text-white" style="width: 140px; background-color: #d9534f; border-color: #d9534f;" onclick="ejecutarEliminacion()">Eliminar</button>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="layout/footer.jsp" %>
