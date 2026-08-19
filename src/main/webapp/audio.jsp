<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="layout/header.jsp" %>
<%@ include file="prueba-tabs.jspf" %>

<div class="container-fluid p-0">
    <!-- Search Bar -->
    <div class="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-3">
        <div class="uxv-search-group" style="max-width: 320px; position: relative;">
            <i class="bi bi-search" style="position: absolute; left: 12px; top: 50%; transform: translateY(-50%); color: #9ca3af; font-size: 14px;"></i>
            <input type="text" id="inputBuscarParticipante" class="form-control ps-5" placeholder="Buscar participante..." onkeyup="filtrarParticipantes()" style="font-size: 14px; border-radius: 6px;">
        </div>
    </div>

    <c:choose>
        <c:when test="${empty participantes}">
            <div class="uxv-empty-box my-4 p-5 text-center bg-white rounded border">
                <i class="bi bi-info-circle-fill fs-1 text-muted mb-3"></i>
                <h5 class="fw-bold text-dark">No hay participantes registrados en esta prueba</h5>
                <p class="text-muted small mb-0">Cuando los participantes completen la evaluación, sus grabaciones de audio aparecerán aquí.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row g-4">
                <!-- Participants Table Column -->
                <div class="col-12 transition-all duration-300" id="tablaContainer">
                    <div class="uxv-tarjeta p-0 overflow-hidden mb-3 bg-white border rounded shadow-sm">
                        <div class="table-responsive">
                            <table class="table align-middle mb-0" id="tablaParticipantes">
                                <thead class="bg-light border-bottom">
                                    <tr class="small fw-bold text-dark">
                                        <th style="width: 60px;" class="ps-4">#</th>
                                        <th>Participante</th>
                                        <th>Duración</th>
                                        <th class="text-center pe-4" style="width: 100px;">Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${participantes}" var="p" varStatus="i">
                                        <tr id="row-participante-${p.idParticipante}" class="participante-row">
                                            <td class="ps-4 text-muted fw-bold">${i.index + 1}</td>
                                            <td class="fw-semibold text-dark">
                                                ${not empty p.nombre ? p.nombre.concat(' ').concat(p.apellidoP) : 'Participante #'.concat(p.idParticipante)}
                                            </td>
                                            <td class="text-muted font-monospace">
                                                <span class="audio-duration-display" data-audio-src="${pageContext.request.contextPath}/audio?action=play&idPrueba=${prueba.idPrueba}&idParticipante=${p.idParticipante}">
                                                    ${not empty p.duracionFormateada && p.duracionFormateada != '0:00' ? p.duracionFormateada : '--:--'}
                                                </span>
                                            </td>
                                            <td class="text-center pe-4">
                                                <button type="button" class="btn btn-sm text-dark p-1 rounded-circle play-row-btn" 
                                                        onclick="reproducirAudio('${not empty p.nombre ? p.nombre.concat(' ').concat(p.apellidoP) : 'Participante #'.concat(p.idParticipante)}', '${pageContext.request.contextPath}/audio?action=play&idPrueba=${prueba.idPrueba}&idParticipante=${p.idParticipante}', ${p.idParticipante})" 
                                                        title="Reproducir audio">
                                                    <i class="bi bi-caret-right-fill fs-5"></i>
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <!-- Table Footer / Pagination -->
                        <div class="d-flex justify-content-between align-items-center p-3 bg-white border-top">
                            <span class="text-muted small">1 - ${participantes.size() > 10 ? 10 : participantes.size()} de ${participantes.size()}</span>
                            <div class="uxv-pagination d-flex align-items-center gap-1">
                                <a href="#" class="page-btn active">1</a>
                                <c:if test="${participantes.size() > 10}">
                                    <a href="#" class="page-btn text-muted">2</a>
                                </c:if>
                                <c:if test="${participantes.size() > 20}">
                                    <a href="#" class="page-btn text-muted">3</a>
                                </c:if>
                                <a href="#" class="page-btn text-muted"><i class="bi bi-chevron-right"></i></a>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Audio Player Panel Column (Appears on Play click, matching Image 2) -->
                <div class="col-lg-4 col-md-5 d-none" id="reproductorContainer">
                    <div class="uxv-tarjeta p-4 bg-white border rounded shadow-sm position-sticky" style="top: 20px;">
                        <div class="d-flex justify-content-between align-items-start mb-2">
                            <h6 class="fw-bold text-dark m-0" style="font-size: 16px;">Reproductor</h6>
                            <button type="button" class="btn-close small opacity-50" onclick="cerrarReproductor()" aria-label="Cerrar reproductor"></button>
                        </div>
                        
                        <p class="fw-semibold text-dark mb-4" id="reproductorNombre" style="font-size: 14px;">Ana Martínez</p>

                        <!-- Simulated or Real Audio Player Element -->
                        <audio id="audioElement" style="display:none;"></audio>

                        <!-- Waveform Visualizer Graphic -->
                        <div class="d-flex align-items-center justify-content-center gap-1 my-3 px-2 py-3 bg-light rounded" id="waveformContainer" style="height: 75px; overflow: hidden;">
                            <!-- Dynamic Bars created by JS -->
                        </div>

                        <!-- Time & Progress Controls -->
                        <div class="d-flex justify-content-between text-muted fw-mono small mt-2 mb-1" style="font-size: 0.8rem;">
                            <span id="reproductorTiempoActual" class="text-dark font-monospace">00:00</span>
                            <span id="reproductorDuracion" class="text-muted font-monospace">--:--</span>
                        </div>

                        <!-- Progress Slider -->
                        <input type="range" class="form-range uxv-audio-slider" id="progressSlider" min="0" max="100" value="0" oninput="seekAudio(this.value)">

                        <hr class="text-muted opacity-25 my-4">

                        <!-- Main Playback Action Buttons -->
                        <div class="d-flex justify-content-center align-items-center gap-4 mb-2">
                            <!-- Skip 5 Seconds Back -->
                            <button type="button" class="btn btn-link text-dark p-0 position-relative text-decoration-none d-flex align-items-center justify-content-center" 
                                    onclick="skipSeconds(-5)" title="Retroceder 5 segundos" style="width: 36px; height: 36px;">
                                <i class="bi bi-arrow-counterclockwise fs-4"></i>
                                <span style="font-size: 0.55rem; font-weight: 800; position: absolute; top: 11px;">5</span>
                            </button>

                            <!-- Play / Pause Button -->
                            <button type="button" id="btnTogglePlay" class="btn p-0 d-flex align-items-center justify-content-center rounded-circle shadow-sm" 
                                    onclick="togglePlay()" 
                                    style="background-color: #173E45; color: white; width: 48px; height: 48px; border: none; transition: transform 0.15s ease;">
                                <i class="bi bi-play-fill fs-3" id="iconPlayPause" style="margin-left: 2px;"></i>
                            </button>

                            <!-- Skip 5 Seconds Forward -->
                            <button type="button" class="btn btn-link text-dark p-0 position-relative text-decoration-none d-flex align-items-center justify-content-center" 
                                    onclick="skipSeconds(5)" title="Adelantar 5 segundos" style="width: 36px; height: 36px;">
                                <i class="bi bi-arrow-clockwise fs-4"></i>
                                <span style="font-size: 0.55rem; font-weight: 800; position: absolute; top: 11px;">5</span>
                            </button>
                        </div>
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

let isPlaying = false;
let totalSeconds = 0;
let audioEl = null;

document.addEventListener("DOMContentLoaded", () => {
    // Dynamic fetching of durations
    document.querySelectorAll('.audio-duration-display').forEach(el => {
        let src = el.getAttribute('data-audio-src');
        if (src && el.innerText.trim() === '--:--') {
            let tempAudio = new Audio();
            tempAudio.preload = "metadata";
            tempAudio.addEventListener('loadedmetadata', () => {
                if (isFinite(tempAudio.duration)) {
                    el.innerText = formatSeconds(tempAudio.duration);
                }
            });
            tempAudio.src = src;
        }
    });

    audioEl = document.getElementById('audioElement');

    if (audioEl) {
        audioEl.addEventListener('loadedmetadata', () => {
            if(isFinite(audioEl.duration)) {
                totalSeconds = Math.floor(audioEl.duration);
                document.getElementById('reproductorDuracion').innerText = formatSeconds(totalSeconds);
            }
        });

        audioEl.addEventListener('timeupdate', () => {
            const current = Math.floor(audioEl.currentTime);
            document.getElementById('reproductorTiempoActual').innerText = formatSeconds(current);
            
            if (totalSeconds > 0) {
                const progress = current / totalSeconds;
                document.getElementById('progressSlider').value = progress * 100;
                actualizarWaveformColores(progress);
            }

            if (isPlaying) {
                const bars = document.querySelectorAll('#waveformContainer .waveform-bar');
                if (bars.length > 0) {
                    const randIndex = Math.floor(Math.random() * bars.length);
                    const bar = bars[randIndex];
                    if (bar) {
                        bar.style.height = (Math.random() * 60 + 20) + '%';
                    }
                }
            }
        });

        audioEl.addEventListener('ended', () => {
            pausarAudio();
            document.getElementById('reproductorTiempoActual').innerText = '00:00';
            document.getElementById('progressSlider').value = 0;
            actualizarWaveformColores(0);
            
            // Reset waveform heights to original state
            const bars = document.querySelectorAll('#waveformContainer .waveform-bar');
            bars.forEach((bar, i) => {
                const numBars = bars.length;
                const distanceToCenter = Math.abs((i - (numBars / 2)) / (numBars / 2));
                const baseHeight = (1 - distanceToCenter) * 65;
                bar.style.height = Math.max(12, Math.min(100, Math.random() * 45 + baseHeight)) + '%';
            });
        });
        
        audioEl.addEventListener('play', () => {
            isPlaying = true;
            const icon = document.getElementById('iconPlayPause');
            if(icon) icon.className = 'bi bi-pause-fill fs-3';
        });

        audioEl.addEventListener('pause', () => {
            isPlaying = false;
            const icon = document.getElementById('iconPlayPause');
            if(icon) icon.className = 'bi bi-play-fill fs-3';
        });
    }
});

function formatSeconds(secs) {
    if (isNaN(secs)) return '00:00';
    const m = Math.floor(secs / 60);
    const s = Math.floor(secs % 60);
    return (m < 10 ? '0' + m : m) + ':' + (s < 10 ? '0' + s : s);
}

function generarWaveform() {
    const container = document.getElementById('waveformContainer');
    container.innerHTML = ''; 
    
    const numBars = 45;
    for(let i = 0; i < numBars; i++) {
        const bar = document.createElement('div');
        const distanceToCenter = Math.abs((i - (numBars / 2)) / (numBars / 2));
        const baseHeight = (1 - distanceToCenter) * 65;
        const height = Math.max(12, Math.min(100, Math.random() * 45 + baseHeight)); 
        
        bar.className = 'waveform-bar';
        bar.dataset.index = i;
        bar.style.width = '3px';
        bar.style.height = height + '%';
        bar.style.backgroundColor = '#2d3748';
        bar.style.borderRadius = '3px';
        bar.style.transition = 'height 0.15s ease, background-color 0.15s ease';
        container.appendChild(bar);
    }
}

function actualizarWaveformColores(progress) {
    const bars = document.querySelectorAll('#waveformContainer .waveform-bar');
    if (!bars.length) return;
    const activeIndex = Math.floor(progress * bars.length);

    bars.forEach((bar, index) => {
        if (index <= activeIndex) {
            bar.style.backgroundColor = '#173E45'; // Highlighted active audio wave color
            bar.style.opacity = '1';
        } else {
            bar.style.backgroundColor = '#a0aec0'; // Unplayed wave color
            bar.style.opacity = '0.6';
        }
    });
}

function reproducirAudio(nombre, audioSrc, idParticipante) {
    const tabla = document.getElementById('tablaContainer');
    const reproductor = document.getElementById('reproductorContainer');
    
    document.querySelectorAll('.participante-row').forEach(r => r.classList.remove('table-active'));
    const selectedRow = document.getElementById('row-participante-' + idParticipante);
    if (selectedRow) selectedRow.classList.add('table-active');

    if (tabla.classList.contains('col-12')) {
        tabla.classList.remove('col-12');
        tabla.classList.add('col-lg-8', 'col-md-7');
        reproductor.classList.remove('d-none');
    }
    
    if (!audioEl) audioEl = document.getElementById('audioElement');
    pausarAudio();

    document.getElementById('reproductorNombre').innerText = nombre || 'Participante';
    document.getElementById('reproductorDuracion').innerText = '00:00'; // Se actualiza en loadedmetadata
    document.getElementById('reproductorTiempoActual').innerText = '00:00';
    document.getElementById('progressSlider').value = 0;

    if (audioSrc && audioSrc.trim() !== '') {
        audioEl.src = audioSrc;
        audioEl.load();
    } else {
        audioEl.removeAttribute('src');
    }

    generarWaveform();
    actualizarWaveformColores(0);

    if(audioEl.src) {
        audioEl.play().catch(() => {});
    }
}

function cerrarReproductor() {
    pausarAudio();
    const tabla = document.getElementById('tablaContainer');
    const reproductor = document.getElementById('reproductorContainer');
    
    tabla.classList.remove('col-lg-8', 'col-md-7');
    tabla.classList.add('col-12');
    reproductor.classList.add('d-none');

    document.querySelectorAll('.participante-row').forEach(r => r.classList.remove('table-active'));
}

function togglePlay() {
    if (!audioEl) audioEl = document.getElementById('audioElement');
    if (isPlaying) {
        pausarAudio();
    } else {
        iniciarAudio();
    }
}

function iniciarAudio() {
    if (!audioEl) audioEl = document.getElementById('audioElement');
    if (audioEl && audioEl.src) {
        audioEl.play().catch(() => {});
    }
}

function pausarAudio() {
    if (!audioEl) audioEl = document.getElementById('audioElement');
    if (audioEl && audioEl.src) {
        audioEl.pause();
    }
}

function seekAudio(val) {
    if (!audioEl) audioEl = document.getElementById('audioElement');
    const pct = parseFloat(val) / 100;
    actualizarWaveformColores(pct);

    if (audioEl && audioEl.duration) {
        audioEl.currentTime = (pct * audioEl.duration);
    }
}

function skipSeconds(sec) {
    if (!audioEl) audioEl = document.getElementById('audioElement');
    if (audioEl && audioEl.duration) {
        audioEl.currentTime = Math.max(0, Math.min(audioEl.duration, audioEl.currentTime + sec));
    }
}
</script>

<style>
    .transition-all { transition: all 0.3s ease-in-out; }
    .play-row-btn:hover { background-color: #e2e8f0; color: #173E45 !important; }
    .uxv-audio-slider { accent-color: #173E45; }
    .waveform-bar { display: inline-block; transform-origin: center; }
</style>

<%@ include file="layout/footer.jsp" %>
