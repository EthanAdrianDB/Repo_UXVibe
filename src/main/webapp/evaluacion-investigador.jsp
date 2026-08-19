<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/images/logoux.png" type="image/png">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>UXVibe | Evaluación en Curso</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/uxvibe.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <!-- SweetAlert2 para alertas elegantes -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        .step-container {
            display: none;
            animation: fadeIn 0.5s ease;
        }
        .step-container.active {
            display: block;
        }
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }
        .record-btn {
            width: 100px;
            height: 100px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            background-color: #f8f9fa;
            border: 4px solid #dee2e6;
            color: #dc3545;
            font-size: 2.5rem;
            cursor: pointer;
            transition: all 0.3s ease;
            margin: 0 auto;
        }
        .record-btn:hover {
            background-color: #ffe6e6;
            border-color: #ffcccc;
        }
        .record-btn.recording {
            animation: pulse 1.5s infinite;
            background-color: #dc3545;
            color: white;
            border-color: #dc3545;
        }
        @keyframes pulse {
            0% { box-shadow: 0 0 0 0 rgba(220, 53, 69, 0.7); }
            70% { box-shadow: 0 0 0 20px rgba(220, 53, 69, 0); }
            100% { box-shadow: 0 0 0 0 rgba(220, 53, 69, 0); }
        }
        .sam-placeholder {
            background-color: #f8f9fa;
            border: 2px dashed #dee2e6;
            border-radius: 8px;
            padding: 40px;
            text-align: center;
            color: #6c757d;
        }
    </style>
</head>
<body class="bg-light">

<!-- Navbar simple -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark px-4" style="background-color: #0f3f4a !important;">
    <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/inicio">
        <i class="bi bi-mic"></i> UXVibe
    </a>
    <span class="text-white opacity-75 ms-auto">Evaluación en curso</span>
</nav>

<div class="container py-5" style="max-width: 800px;">
    
    <div class="card shadow-sm border-0 rounded">
        <div class="card-body p-5">
            
            <!-- FORMULARIO GENERAL -->
            <form action="${pageContext.request.contextPath}/participantes" method="post" id="formFinalizar">
                <input type="hidden" name="action" value="create">
                <input type="hidden" name="idPrueba" value="${not empty param.idPrueba ? param.idPrueba : '0'}">
                <input type="hidden" id="hiddenUrlDestino" value="${not empty prueba.urlSistema ? prueba.urlSistema : param.url}">
                
            <!-- PASO 1: Grabar Audio -->
            <div id="step1" class="step-container active text-center">
                <h4 class="fw-bold mb-4">Paso 1: Grabación de Audio</h4>
                <p class="text-muted mb-5">Por favor, presiona el botón para iniciar la grabación de las impresiones del participante.</p>
                
                <div id="btnRecord" class="record-btn mb-4" onclick="toggleRecording()">
                    <i id="iconRecord" class="bi bi-mic-fill"></i>
                </div>
                <p id="recordStatus" class="fw-bold text-danger d-none mb-5">Grabando...</p>
                
                <hr class="my-4 text-muted">
                
                <div class="d-flex justify-content-end">
                    <button type="button" id="btnSiguienteAudio" class="btn text-white px-4 py-2 disabled" style="background-color: #1a4a5b;" onclick="nextStep(2)">
                        Siguiente <i class="bi bi-arrow-right ms-2"></i>
                    </button>
                </div>
            </div>
            
            <!-- PASO 2: SAM -->
            <div id="step2" class="step-container text-center">
                <h4 class="fw-bold mb-4">Paso 2: SAM (Self-Assessment Manikin)</h4>
                <p class="text-muted mb-4">Selecciona el nivel de emoción o sentimiento del participante en una escala del 1 al 9.</p>
                
                <div class="mb-4 text-start">
                    <label class="form-label fw-bold">Valencia: ¿Cómo te sientes después de haber interactuado con la página/sistema Web?</label>
                    <div class="text-center mt-3 mb-2">
                        <img src="${pageContext.request.contextPath}/assets/images/img.png" class="img-fluid" alt="Escala Valencia" style="max-height: 120px;">
                    </div>
                    <div class="d-flex justify-content-between align-items-center mt-2 px-3 py-3 bg-white border rounded">
                        <span class="small text-muted fw-bold me-2 text-center" style="width: 80px;">1<br>(Muy mal)</span>
                        <div class="d-flex gap-1 flex-wrap justify-content-center flex-grow-1">
                            <% for(int i=1; i<=9; i++) { %>
                                <div class="form-check form-check-inline m-0 text-center" style="width: 30px;">
                                    <input class="form-check-input float-none mx-auto d-block mb-1" type="radio" name="sam_valencia" id="sam_val_<%= i %>" value="<%= i %>" required>
                                    <label class="form-check-label small" for="sam_val_<%= i %>"><%= i %></label>
                                </div>
                            <% } %>
                        </div>
                        <span class="small text-muted fw-bold ms-2 text-center" style="width: 80px;">9<br>(Muy bien)</span>
                    </div>
                </div>

                <div class="mb-4 text-start">
                    <label class="form-label fw-bold">Activación: ¿Qué tan impactante fue tu experiencia?</label>
                    <div class="text-center mt-3 mb-2">
                        <img src="${pageContext.request.contextPath}/assets/images/img_1.png" class="img-fluid" alt="Escala Activación" style="max-height: 120px;">
                    </div>
                    <div class="d-flex justify-content-between align-items-center mt-2 px-3 py-3 bg-white border rounded">
                        <span class="small text-muted fw-bold me-2 text-center" style="width: 80px;">1<br>(Muy calmado)</span>
                        <div class="d-flex gap-1 flex-wrap justify-content-center flex-grow-1">
                            <% for(int i=1; i<=9; i++) { %>
                                <div class="form-check form-check-inline m-0 text-center" style="width: 30px;">
                                    <input class="form-check-input float-none mx-auto d-block mb-1" type="radio" name="sam_activacion" id="sam_act_<%= i %>" value="<%= i %>" required>
                                    <label class="form-check-label small" for="sam_act_<%= i %>"><%= i %></label>
                                </div>
                            <% } %>
                        </div>
                        <span class="small text-muted fw-bold ms-2 text-center" style="width: 80px;">9<br>(Muy alterado)</span>
                    </div>
                </div>

                <div class="mb-5 text-start">
                    <label class="form-label fw-bold">Dominio: ¿Qué tanto dominio tuviste sobre tus emociones y sentimientos?</label>
                    <div class="text-center mt-3 mb-2">
                        <img src="${pageContext.request.contextPath}/assets/images/img_2.png" class="img-fluid" alt="Escala Dominio" style="max-height: 120px;">
                    </div>
                    <div class="d-flex justify-content-between align-items-center mt-2 px-3 py-3 bg-white border rounded">
                        <span class="small text-muted fw-bold me-2 text-center" style="width: 90px;">1<br>(Muy influenciado)</span>
                        <div class="d-flex gap-1 flex-wrap justify-content-center flex-grow-1">
                            <% for(int i=1; i<=9; i++) { %>
                                <div class="form-check form-check-inline m-0 text-center" style="width: 30px;">
                                    <input class="form-check-input float-none mx-auto d-block mb-1" type="radio" name="sam_dominio" id="sam_dom_<%= i %>" value="<%= i %>" required>
                                    <label class="form-check-label small" for="sam_dom_<%= i %>"><%= i %></label>
                                </div>
                            <% } %>
                        </div>
                        <span class="small text-muted fw-bold ms-2 text-center" style="width: 90px;">9<br>(Muy dominante)</span>
                    </div>
                </div>
                
                <hr class="my-4 text-muted">
                
                <div class="d-flex justify-content-between">
                    <button type="button" class="btn btn-outline-secondary px-4 py-2" onclick="nextStep(1)">
                        <i class="bi bi-arrow-left me-2"></i> Atrás
                    </button>
                    <button type="button" class="btn text-white px-4 py-2" style="background-color: #1a4a5b;" onclick="nextStep(3)">
                        Siguiente <i class="bi bi-arrow-right ms-2"></i>
                    </button>
                </div>
            </div>
            
            <!-- PASO 3: Preguntas -->
            <div id="step3" class="step-container">
                <h4 class="fw-bold mb-4 text-center">Paso 3: Cuestionario Final</h4>
                <p class="text-muted text-center mb-4">Por favor, evalúa las siguientes afirmaciones respecto a tu experiencia (1 = Totalmente en desacuerdo, 5 = Totalmente de acuerdo).</p>
                
                <div style="max-height: 50vh; overflow-y: auto; padding-right: 10px;" class="mb-4">
                    <!-- UX Questions 1 to 15 -->
                    <% 
                        String[] uxQuestions = {
                            "1. Me resultó fácil aprender a navegar por esta página/sistema web.",
                            "2. La estructura del menú y los enlaces es intuitiva y sé dónde encontrar la información.",
                            "3. Creo que el sistema web es innecesariamente complejo o difícil de entender.",
                            "4. Las funciones y herramientas de la página cubren completamente mis necesidades.",
                            "5. Este sistema web me permite realizar mis tareas de forma más rápida y eficiente.",
                            "6. La información, textos y contenidos que ofrece la plataforma son claros y valiosos.",
                            "7. El diseño visual de la página web es atractivo, limpio y moderno.",
                            "8. El tamaño de la letra, los contrastes y los colores facilitan una lectura cómoda.",
                            "9. La interfaz se siente saturada, desordenada o visualmente confusa.",
                            "10. La página web carga rápidamente y las secciones responden sin retrasos.",
                            "11. El sistema funciona correctamente en mi navegador y no experimenté errores técnicos.",
                            "12. La plataforma se adapta bien y es fácil de usar si accedo desde el teléfono móvil.",
                            "13. Me siento satisfecho con mi experiencia general utilizando este sitio/sistema web.",
                            "14. Recomendaría este sitio o sistema web a otros colegas o usuarios.",
                            "15. Si tuviera otra alternativa que haga lo mismo, preferiría no usar esta web."
                        };
                        for(int i=0; i<uxQuestions.length; i++) {
                    %>
                    <div class="mb-4 p-3 bg-white border rounded">
                        <label class="form-label fw-bold mb-3"><%= uxQuestions[i] %></label>
                        <div class="d-flex justify-content-between px-3">
                            <% for(int j=1; j<=5; j++) { %>
                            <div class="form-check form-check-inline m-0 text-center">
                                <input class="form-check-input float-none mb-1 mx-auto d-block" type="radio" name="ux_q<%= (i+1) %>" id="ux_q<%= (i+1) %>_v<%= j %>" value="<%= j %>" required>
                                <label class="form-check-label small" for="ux_q<%= (i+1) %>_v<%= j %>"><%= j %></label>
                            </div>
                            <% } %>
                        </div>
                    </div>
                    <% } %>

                    <h5 class="fw-bold mt-5 mb-3">Frecuencia de Estados de Ánimo</h5>
                    
                    <div class="mb-4 p-3 bg-white border rounded">
                        <label class="form-label fw-bold mb-3">5. En una semana típica ¿con que frecuencia se siente estresado/a?</label>
                        <select class="form-select" name="estado_estresado" required>
                            <option value="">Selecciona una opción</option>
                            <option value="Nunca">Nunca</option>
                            <option value="De vez en cuando">De vez en cuando</option>
                            <option value="Cerca de la mitad del tiempo">Cerca de la mitad del tiempo</option>
                            <option value="La mayor parte del tiempo">La mayor parte del tiempo</option>
                            <option value="Siempre">Siempre</option>
                        </select>
                    </div>

                    <div class="mb-4 p-3 bg-white border rounded">
                        <label class="form-label fw-bold mb-3">6. En una semana típica ¿con que frecuencia se siente Relajado/a?</label>
                        <select class="form-select" name="estado_relajado" required>
                            <option value="">Selecciona una opción</option>
                            <option value="Nunca">Nunca</option>
                            <option value="De vez en cuando">De vez en cuando</option>
                            <option value="Cerca de la mitad del tiempo">Cerca de la mitad del tiempo</option>
                            <option value="La mayor parte del tiempo">La mayor parte del tiempo</option>
                            <option value="Siempre">Siempre</option>
                        </select>
                    </div>
                </div>
                
                <hr class="my-4 text-muted">
                
                <div class="d-flex justify-content-between">
                    <button type="button" class="btn btn-outline-secondary px-4 py-2" onclick="nextStep(2)">
                        <i class="bi bi-arrow-left me-2"></i> Atrás
                    </button>
                    <button type="button" class="btn text-white px-4 py-2" style="background-color: #1a4a5b;" onclick="nextStep(4)">
                        Siguiente <i class="bi bi-arrow-right ms-2"></i>
                    </button>
                </div>
            </div>
            
            <!-- PASO 4: Datos del Participante -->
            <div id="step4" class="step-container">
                <h4 class="fw-bold mb-4 text-center">Paso 4: Datos del Participante</h4>
                <p class="text-muted text-center mb-5">Ingresa los datos del participante para guardar y finalizar la evaluación.</p>
                
                    <div class="mb-3">
                        <label class="form-label fw-medium">Nombre(s): <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" name="nombre" placeholder="Ej. Juan" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-medium">Apellido Paterno: <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" name="apellidoP" placeholder="Ej. Pérez" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-medium">Apellido Materno: <span class="text-muted">(Opcional)</span></label>
                        <input type="text" class="form-control" name="apellidoM" placeholder="Ej. Gómez">
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-medium">Edad: <span class="text-danger">*</span></label>
                        <input type="number" class="form-control" name="edad" placeholder="Ej. 25" min="1" max="120" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-medium">Sexo / Género: <span class="text-danger">*</span></label>
                        <select class="form-select" name="sexo" required>
                            <option value="">Selecciona una opción</option>
                            <option value="0">Femenino</option>
                            <option value="1">Masculino</option>
                        </select>
                    </div>

                    <hr class="my-4 text-muted">
                    
                    <div class="d-flex justify-content-between">
                        <button type="button" class="btn btn-outline-secondary px-4 py-2" onclick="nextStep(3)">
                            <i class="bi bi-arrow-left me-2"></i> Atrás
                        </button>
                        <button type="submit" id="btnFinalizar" class="btn text-white px-4 py-2" style="background-color: #3b8285;">
                            <i class="bi bi-check-circle me-2"></i> Finalizar y Guardar
                        </button>
                    </div>
            </div>
            
            </form>
            
        </div>
    </div>
</div>

<script>
    let isRecording = false;
    let mediaRecorder;
    let audioChunks = [];
    let audioBlob = null;
    let streamRef = null;
    
    function toggleRecording() {
        const btn = document.getElementById('btnRecord');
        const icon = document.getElementById('iconRecord');
        const status = document.getElementById('recordStatus');
        const btnSiguiente = document.getElementById('btnSiguienteAudio');
        
        isRecording = !isRecording;
        
        if (isRecording) {
            btn.classList.add('recording');
            icon.classList.replace('bi-mic-fill', 'bi-stop-fill');
            status.classList.remove('d-none');
            
            if (btnSiguiente) {
                btnSiguiente.classList.remove('disabled');
            }
            
            startRecording();
        } else {
            btn.classList.remove('recording');
            icon.classList.replace('bi-stop-fill', 'bi-mic-fill');
            status.classList.add('d-none');
            
            stopRecording();
        }
    }

    async function startRecording() {
        audioChunks = [];
        
        try {
            const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
            streamRef = stream;
            mediaRecorder = new MediaRecorder(stream);
            
            mediaRecorder.ondataavailable = (event) => {
                if (event.data.size > 0) {
                    audioChunks.push(event.data);
                }
            };

            mediaRecorder.onstop = () => {
                audioBlob = new Blob(audioChunks, { type: 'audio/webm' });
                if (streamRef) {
                    streamRef.getTracks().forEach(track => track.stop());
                }
            };

            mediaRecorder.start();
            console.log("Recording started...");
        } catch (err) {
            console.error("Error accessing microphone:", err);
            alert("No se pudo acceder al micrófono. Por favor, concede los permisos.");
        }
    }

    function stopRecording() {
        if (mediaRecorder && mediaRecorder.state !== "inactive") {
            mediaRecorder.stop();
            console.log("Recording stopped.");
        }
    }

    let urlAbierta = false;

    function nextStep(stepNumber) {
        // Enforce validation before proceeding forwards
        const currentActive = document.querySelector('.step-container.active');
        if (currentActive) {
            const currentStepId = currentActive.id; // 'step1', 'step2', 'step3', 'step4'
            const targetStepId = 'step' + stepNumber;
            // Only validate if we are moving forward
            if (stepNumber > parseInt(currentStepId.replace('step', ''))) {
                const inputs = currentActive.querySelectorAll('input[required], select[required]');
                let allValid = true;
                
                // HTML5 reportValidity natively checks inputs even in hidden tabs? No, they are visible now
                // We'll use manual check for radio groups and normal inputs
                inputs.forEach(input => {
                    if (!input.checkValidity()) {
                        input.reportValidity();
                        allValid = false;
                    }
                });
                
                if (!allValid) return; // Stop if not valid
            }
        }

        if (stepNumber === 2 && !urlAbierta) {
            const url = document.getElementById('hiddenUrlDestino').value;
            if (url && url.trim() !== '') {
                window.open(url.trim(), '_blank');
                urlAbierta = true;
            }
        }

        document.querySelectorAll('.step-container').forEach(el => {
            el.classList.remove('active');
        });
        
        document.getElementById('step' + stepNumber).classList.add('active');
    }

    // Intercept form submission
    document.getElementById('formFinalizar').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const btnFinalizar = document.getElementById('btnFinalizar');
        btnFinalizar.disabled = true;
        btnFinalizar.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span> Guardando...';

        // Ensure recording is stopped before submitting
        if (isRecording) {
            toggleRecording(); // This calls stopRecording()
            // Wait a brief moment for the onstop event to fire and create the Blob
            await new Promise(resolve => setTimeout(resolve, 500)); 
        }

        const formData = new FormData(this);
        
        if (audioBlob) {
            formData.append('audio_file', audioBlob, 'recording.webm');
        }

        try {
            const response = await fetch(this.getAttribute('action'), {
                method: 'POST',
                body: formData
            });

            if (response.ok) {
                // If the servlet returns a redirect or ok status
                const idPrueba = document.querySelector('input[name="idPrueba"]').value;
                Swal.fire({
                    title: '¡Evaluación completada!',
                    text: 'Los datos del participante y la grabación se han guardado con éxito.',
                    icon: 'success',
                    confirmButtonColor: '#3b8285',
                    confirmButtonText: 'Aceptar'
                }).then(() => {
                    window.location.href = '${pageContext.request.contextPath}/participantes?idPrueba=' + idPrueba;
                });
            } else {
                alert("Error al guardar la evaluación. Status: " + response.status);
                btnFinalizar.disabled = false;
                btnFinalizar.innerHTML = '<i class="bi bi-check-circle me-2"></i> Finalizar y Guardar';
            }
        } catch (error) {
            console.error("Error submitting form:", error);
            alert("Error de conexión al guardar la evaluación.");
            btnFinalizar.disabled = false;
            btnFinalizar.innerHTML = '<i class="bi bi-check-circle me-2"></i> Finalizar y Guardar';
        }
    });
</script>

</body>
</html>
