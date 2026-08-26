<%-- 
    ===================================================================
    Dashboard Principal del Evaluador — UXVibe
    Controlador: InicioServlet (/inicio)
    Descripción: Vista principal que muestra tarjetas de métricas globales
    (total de pruebas, total de participantes, distribución de género),
    el listado de pruebas creadas con paginación, filtros de búsqueda
    y modales para crear/editar/eliminar pruebas.
    ===================================================================
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:useBean id="now" class="java.util.Date" />
<%@ include file="layout/header.jsp" %>

                <div class="container-fluid py-2">
                    <!-- Encabezado de Bienvenida -->
                    <h2 class="fw-bold mb-4">
                        Bienvenido,
                        ${sessionScope.evaluador.nombre}
                        ${sessionScope.evaluador.apellidoP}
                        ${sessionScope.evaluador.apellidoM != null ? sessionScope.evaluador.apellidoM : ''}
                    </h2>

                    <!-- Tarjetas Métricas Superiores -->
                    <div class="row g-3 mb-4">
                        <div class="col-12 col-md-4">
                            <div class="uxv-tarjeta d-flex align-items-center gap-3 h-100">
                                <div class="bg-light rounded p-3 text-dark fs-2 d-flex align-items-center justify-content-center"
                                    style="width: 58px; height: 58px;">
                                    <i class="bi bi-file-earmark-text"></i>
                                </div>
                                <div>
                                    <div class="valor text-dark">${totalPruebas != null ? totalPruebas : 0}</div>
                                    <div class="titulo text-dark">Pruebas registradas</div>
                                    <div class="subtitulo text-truncate" style="max-width: 180px;">Total de pruebas
                                        creadas</div>
                                </div>
                            </div>
                        </div>
                        <div class="col-12 col-md-4">
                            <div class="uxv-tarjeta d-flex align-items-center gap-3 h-100">
                                <div class="bg-light rounded p-3 text-dark fs-2 d-flex align-items-center justify-content-center"
                                    style="width: 58px; height: 58px;">
                                    <i class="bi bi-people"></i>
                                </div>
                                <div>
                                    <div class="valor text-dark">${totalParticipantes != null ? totalParticipantes : 0}
                                    </div>
                                    <div class="titulo text-dark">Participantes evaluados</div>
                                    <div class="subtitulo text-truncate" style="max-width: 180px;">Total de
                                        participantes en todas las pruebas</div>
                                </div>
                            </div>
                        </div>
                        <div class="col-12 col-md-4">
                            <div class="uxv-tarjeta d-flex flex-column justify-content-center h-100">
                                <div class="fw-bold text-dark mb-2" style="font-size: 0.95rem;">Distribución por sexo
                                </div>

                                <div class="d-flex align-items-center justify-content-between gap-2 mb-2">
                                    <span class="small text-muted" style="min-width: 68px;">Femenino</span>
                                    <div class="flex-grow-1 rounded-pill overflow-hidden"
                                        style="height: 8px; background-color: #EFE7DA;">
                                        <div class="rounded-pill"
                                            style="height: 8px; width: ${distribucionSexo['Femenino'] != null ? distribucionSexo['Femenino'] : 0}%; background-color: #C29B77;">
                                        </div>
                                    </div>
                                    <span class="small fw-bold text-dark text-end" style="min-width: 52px;">
                                        ${distribucionSexo['Femenino'] != null ? distribucionSexo['Femenino'] : 0.0} %
                                    </span>
                                </div>

                                <div class="d-flex align-items-center justify-content-between gap-2">
                                    <span class="small text-muted" style="min-width: 68px;">Masculino</span>
                                    <div class="flex-grow-1 rounded-pill overflow-hidden"
                                        style="height: 8px; background-color: #EFE7DA;">
                                        <div class="rounded-pill"
                                            style="height: 8px; width: ${distribucionSexo['Masculino'] != null ? distribucionSexo['Masculino'] : 0}%; background-color: #C29B77;">
                                        </div>
                                    </div>
                                    <span class="small fw-bold text-dark text-end" style="min-width: 52px;">
                                        ${distribucionSexo['Masculino'] != null ? distribucionSexo['Masculino'] : 0.0} %
                                    </span>
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
                                <input type="text" id="inputBuscarPrueba" class="form-control"
                                    placeholder="Buscar por prueba..." onkeyup="filtrarPruebas()">
                            </div>

                            <a href="${pageContext.request.contextPath}/prueba?action=nueva"
                                class="btn uxv-btn-crear px-3 py-2 d-inline-flex align-items-center gap-2">
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
                                <p class="text-muted small mb-3">Crea tu primera prueba de usabilidad para comenzar a
                                    evaluar participantes.</p>
                                <a href="${pageContext.request.contextPath}/prueba?action=nueva"
                                    class="btn uxv-btn-crear px-4 py-2">
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
                                                <th>Fecha de creacion</th>
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
                                                        <a href="${prueba.urlSistema}" target="_blank"
                                                            class="text-decoration-none text-muted">
                                                            ${prueba.urlSistema}
                                                        </a>
                                                    </td>
                                                    <td class="text-muted">
                                                        <fmt:formatDate value="${now}" pattern="dd/MM/yyyy" />
                                                    </td>
                                                    <td class="fw-semibold">${prueba.totalParticipantes}</td>
                                                    <td class="text-end pe-4">
                                                        <div class="d-inline-flex gap-2">
                                                            <!-- Ejecutar prueba (Abre Modal de Términos) -->
                                                            <button type="button"
                                                                class="btn btn-sm btn-light border text-dark"
                                                                title="Ejecutar prueba" data-tarea="<c:out value="
                                                                ${prueba.descripcion}" />"
                                                            data-url="
                                                            <c:out value="${prueba.urlSistema}" />"
                                                            onclick="openTerminosModal(${prueba.idPrueba}, this)">
                                                            <i class="bi bi-play-fill"></i>
                                                            </button>
                                                            <!-- Ver resultados -->
                                                            <a class="btn btn-sm btn-light border text-dark"
                                                                title="Ver resultados"
                                                                href="${pageContext.request.contextPath}/resultados?idPrueba=${prueba.idPrueba}">
                                                                <i class="bi bi-eye"></i>
                                                            </a>
                                                            <!-- Editar prueba -->
                                                            <a class="btn btn-sm btn-light border text-dark"
                                                                title="Editar prueba"
                                                                href="${pageContext.request.contextPath}/prueba?id=${prueba.idPrueba}">
                                                                <i class="bi bi-pencil"></i>
                                                            </a>
                                                            <!-- Eliminar prueba (CRUD 3: Fetch - 1 Sola Tabla) -->
                                                            <button type="button"
                                                                class="btn btn-sm btn-light border text-danger"
                                                                title="Eliminar prueba (Fetch)"
                                                                onclick="abrirModalEliminarPrueba(${prueba.idPrueba}, this)">
                                                                <i class="bi bi-trash"></i>
                                                            </button>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>

                                <!-- Pie de tabla / Paginación -->
                                <div class="d-flex justify-content-between align-items-center p-3 bg-white border-top">
                                    <span class="text-muted small pagination-info">1 - ${pruebas.size()} de ${pruebas.size()}</span>
                                    <div class="uxv-pagination" id="paginationControls">
                                        <!-- Paginación dinámica -->
                                    </div>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <script>
                    function paginarPruebas(pageSize = 6) {
                        const table = document.getElementById('tablaPruebas');
                        if (!table) return;
                        const tbody = table.querySelector('tbody');
                        const rows = Array.from(tbody.querySelectorAll('tr'));
                        
                        const visibleRows = rows.filter(row => row.style.display !== 'none');
                        const totalPages = Math.ceil(visibleRows.length / pageSize) || 1;
                        let currentPage = 1;

                        function renderPage(page) {
                            currentPage = page;
                            const start = (page - 1) * pageSize;
                            const end = start + pageSize;

                            visibleRows.forEach((row, index) => {
                                if (index >= start && index < end) {
                                    row.classList.remove('d-none');
                                } else {
                                    row.classList.add('d-none');
                                }
                            });

                            renderPaginationControls();
                        }

                        function renderPaginationControls() {
                            const paginationContainer = document.getElementById('paginationControls');
                            const infoContainer = document.querySelector('.pagination-info');
                            if (!paginationContainer || !infoContainer) return;

                            paginationContainer.innerHTML = '';
                            
                            const currentStart = visibleRows.length === 0 ? 0 : ((currentPage - 1) * pageSize) + 1;
                            const currentEnd = Math.min(currentPage * pageSize, visibleRows.length);
                            infoContainer.innerText = currentStart + " - " + currentEnd + " de " + visibleRows.length;

                            if (totalPages <= 1) return;

                            const prevBtn = document.createElement('a');
                            prevBtn.href = '#';
                            prevBtn.className = 'page-btn text-muted ' + (currentPage === 1 ? 'disabled' : '');
                            prevBtn.innerHTML = '<i class="bi bi-chevron-left"></i>';
                            prevBtn.onclick = (e) => { e.preventDefault(); if (currentPage > 1) renderPage(currentPage - 1); };
                            paginationContainer.appendChild(prevBtn);

                            for (let i = 1; i <= totalPages; i++) {
                                const numBtn = document.createElement('a');
                                numBtn.href = '#';
                                numBtn.className = 'page-btn ' + (i === currentPage ? 'active' : 'text-muted');
                                numBtn.innerText = i;
                                numBtn.onclick = (e) => { e.preventDefault(); renderPage(i); };
                                paginationContainer.appendChild(numBtn);
                            }

                            const nextBtn = document.createElement('a');
                            nextBtn.href = '#';
                            nextBtn.className = 'page-btn text-muted ' + (currentPage === totalPages ? 'disabled' : '');
                            nextBtn.innerHTML = '<i class="bi bi-chevron-right"></i>';
                            nextBtn.onclick = (e) => { e.preventDefault(); if (currentPage < totalPages) renderPage(currentPage + 1); };
                            paginationContainer.appendChild(nextBtn);
                        }

                        renderPage(1);
                    }

                    document.addEventListener("DOMContentLoaded", function() {
                        paginarPruebas();
                    });

                    function filtrarPruebas() {
                        let input = document.getElementById("inputBuscarPrueba").value.toLowerCase();
                        let rows = document.querySelectorAll("#tablaPruebas tbody tr");
                        rows.forEach(row => {
                            // La columna Nombre es el segundo td (índice 1)
                            let nombreTd = row.querySelectorAll("td")[1];
                            let texto = nombreTd ? nombreTd.innerText.toLowerCase() : "";
                            row.classList.remove('d-none');
                            row.style.display = texto.includes(input) ? "" : "none";
                        });
                        paginarPruebas();
                    }

                    let pruebaAEliminarId = null;
                    let pruebaAEliminarBtn = null;
                    let modalEliminarPruebaInstance = null;

                    function abrirModalEliminarPrueba(idPrueba, btnElement) {
                        pruebaAEliminarId = idPrueba;
                        pruebaAEliminarBtn = btnElement;
                        
                        if (!modalEliminarPruebaInstance) {
                            modalEliminarPruebaInstance = new bootstrap.Modal(document.getElementById('modalEliminarPrueba'));
                        }
                        modalEliminarPruebaInstance.show();
                    }

                    async function ejecutarEliminacionPrueba() {
                        if (!pruebaAEliminarId || !pruebaAEliminarBtn) return;

                        const params = new URLSearchParams();
                        params.append('action', 'delete');
                        params.append('id', pruebaAEliminarId);

                        try {
                            const response = await fetch('${pageContext.request.contextPath}/prueba', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded',
                                    'X-Requested-With': 'XMLHttpRequest'
                                },
                                body: params
                            });

                            if (response.ok) {
                                const tr = pruebaAEliminarBtn.closest('tr');
                                if (tr) {
                                    tr.style.transition = 'all 0.3s ease';
                                    tr.style.opacity = '0';
                                    setTimeout(() => tr.remove(), 300);
                                }
                                if (modalEliminarPruebaInstance) {
                                    modalEliminarPruebaInstance.hide();
                                }
                            } else {
                                alert('Error al eliminar la prueba.');
                            }
                        } catch (e) {
                            console.error(e);
                            alert('Error de conexión.');
                        }
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
                            // Ya NO se abre la URL aqui. Se abrira en la pagina de evaluacion al dar Siguiente despues de grabar.
                            window.location.href = "${pageContext.request.contextPath}/evaluacion-investigador.jsp?idPrueba=" + currentPruebaId + "&url=" + encodeURIComponent(currentUrlDestino);
                        }
                    }

                    document.addEventListener('DOMContentLoaded', function () {
                        const checkTerminos = document.getElementById('checkAceptoTerminos');
                        if (checkTerminos) {
                            checkTerminos.addEventListener('change', function () {
                                const btn = document.getElementById('btnAceptarTerminos');
                                if (this.checked) {
                                    btn.classList.remove('disabled');
                                } else {
                                    btn.classList.add('disabled');
                                }
                            });
                        }

                        const btnAceptar = document.getElementById('btnAceptarTerminos');
                        if (btnAceptar) {
                            btnAceptar.addEventListener('click', function (e) {
                                e.preventDefault();
                                if (!this.classList.contains('disabled')) {
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

                <!-- Modal de Confirmación de Eliminación de Prueba -->
                <div class="modal fade" id="modalEliminarPrueba" tabindex="-1" aria-labelledby="modalEliminarPruebaLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content border-0 shadow-lg" style="border-radius: 12px;">
                            <div class="modal-body p-5 text-center">
                                <div class="mb-4 mx-auto d-flex align-items-center justify-content-center" style="width: 90px; height: 90px; border: 4px solid #C19B76; border-radius: 50%;">
                                    <i class="bi bi-exclamation" style="font-size: 4rem; color: #C19B76;"></i>
                                </div>
                                <h4 class="fw-bold mb-3 text-dark" id="modalEliminarPruebaLabel">Eliminación de prueba</h4>
                                <p class="text-dark mb-4 fs-6">
                                    ¿Está seguro de que desea eliminar esta<br>prueba?
                                </p>
                                <div class="d-flex justify-content-center gap-3">
                                    <button type="button" class="btn btn-outline-secondary px-4 py-2 fw-semibold" style="width: 140px; border-color: #6c757d;" data-bs-dismiss="modal">Cancelar</button>
                                    <button type="button" class="btn px-4 py-2 fw-semibold text-white" style="width: 140px; background-color: #d9534f; border-color: #d9534f;" onclick="ejecutarEliminacionPrueba()">Eliminar</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Modal de Términos y Condiciones -->
                <div class="modal fade" id="modalTerminos" tabindex="-1" aria-labelledby="modalTerminosLabel"
                    aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered modal-lg">
                        <div class="modal-content border-0 shadow" style="border-radius: 4px;">
                            <div class="modal-body p-5">
                                <h4 class="text-center fw-bold mb-4">TÉRMINOS Y CONDICIONES</h4>
                                <div class="border border-dark p-4 mb-4 bg-white"
                                    style="height: 200px; overflow-y: auto; font-size: 0.9rem;">
                                    Al participar en esta prueba de usabilidad, aceptas los siguientes términos y condiciones relacionados con la recopilación y tratamiento de tus datos.
                                    <br><br>
                                    <strong>1. Objeto de la Evaluación</strong><br>
                                    UXVibe tiene como propósito recopilar información sobre la experiencia de usuario (UX) al interactuar con sistemas web. Esto incluye la grabación de audio de tus impresiones durante la prueba y tus respuestas a los cuestionarios.
                                    <br><br>
                                    <strong>2. Privacidad y Confidencialidad</strong><br>
                                    Toda la información recopilada, incluyendo grabaciones de voz y datos personales, será tratada con estricta confidencialidad. Se utilizará exclusivamente para fines de investigación y mejora del sistema evaluado, y no será compartida con terceros ajenos al proyecto.
                                    <br><br>
                                    <strong>3. Participación Voluntaria</strong><br>
                                    Tu participación es completamente voluntaria. Tienes el derecho de detener la prueba y retirar tu consentimiento en cualquier momento sin ninguna consecuencia.
                                    <br><br>
                                    <strong>4. Consentimiento de Grabación de Audio</strong><br>
                                    Al aceptar estos términos, otorgas tu consentimiento explícito para que tu voz sea grabada durante la sesión con el fin de analizar tus comentarios e impresiones en tiempo real.
                                </div>
                                <div class="form-check mb-5 d-flex align-items-center gap-2">
                                    <input class="form-check-input mt-0" type="checkbox" id="checkAceptoTerminos"
                                        style="width: 20px; height: 20px; cursor: pointer;">
                                    <label class="form-check-label ms-2" for="checkAceptoTerminos"
                                        style="font-size: 0.95rem; cursor: pointer;">
                                        Acepto los términos y condiciones.
                                    </label>
                                </div>
                                <div class="d-flex justify-content-center gap-3">
                                    <button type="button" class="btn btn-outline-secondary px-5 py-2"
                                        data-bs-dismiss="modal">No acepto</button>
                                    <a href="#" id="btnAceptarTerminos" class="btn text-white px-5 py-2 disabled"
                                        style="background-color: #0f3f4a;">Acepto</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Modal de Tarea -->
                <div class="modal fade" id="modalTarea" tabindex="-1" aria-labelledby="modalTareaLabel"
                    aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered modal-lg">
                        <div class="modal-content border-0 shadow" style="border-radius: 4px;">
                            <div class="modal-body p-5">
                                <h4 class="text-center fw-bold mb-4">TAREA A REALIZAR</h4>
                                <div class="border border-dark p-4 mb-4 bg-white" id="modalTareaContent"
                                    style="height: 200px; overflow-y: auto; font-size: 0.9rem;">
                                    <!-- La tarea se inyecta por JS -->
                                </div>
                                <div class="d-flex justify-content-center gap-3 mt-4">
                                    <button type="button" class="btn btn-outline-secondary px-5 py-2"
                                        data-bs-dismiss="modal">Cancelar</button>
                                    <button type="button" id="btnEntendidoTarea" class="btn text-white px-5 py-2"
                                        style="background-color: #0f3f4a;"
                                        onclick="redirigirEvaluacion(event)">Entendido</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <%@ include file="layout/footer.jsp" %>