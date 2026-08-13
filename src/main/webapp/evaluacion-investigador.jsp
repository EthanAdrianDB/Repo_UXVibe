<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>UXVibe | Evaluación en Curso</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/uxvibe.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
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
                    <button class="btn text-white px-4 py-2" style="background-color: #1a4a5b;" onclick="nextStep(2)">
                        Siguiente <i class="bi bi-arrow-right ms-2"></i>
                    </button>
                </div>
            </div>
            
            <!-- PASO 2: SAM -->
            <div id="step2" class="step-container text-center">
                <h4 class="fw-bold mb-4">Paso 2: SAM (Self-Assessment Manikin)</h4>
                <p class="text-muted mb-4">Selecciona el nivel de emoción o sentimiento del participante.</p>
                
                <div class="sam-placeholder mb-5">
                    <i class="bi bi-emoji-smile fs-1 d-block mb-3"></i>
                    <p class="mb-0">[ Aquí se colocará el componente o imagen del SAM ]</p>
                </div>
                
                <hr class="my-4 text-muted">
                
                <div class="d-flex justify-content-between">
                    <button class="btn btn-outline-secondary px-4 py-2" onclick="nextStep(1)">
                        <i class="bi bi-arrow-left me-2"></i> Atrás
                    </button>
                    <button class="btn text-white px-4 py-2" style="background-color: #1a4a5b;" onclick="nextStep(3)">
                        Siguiente <i class="bi bi-arrow-right ms-2"></i>
                    </button>
                </div>
            </div>
            
            <!-- PASO 3: Preguntas -->
            <div id="step3" class="step-container">
                <h4 class="fw-bold mb-4 text-center">Paso 3: Cuestionario Final</h4>
                <p class="text-muted text-center mb-5">Por favor, responde las siguientes preguntas de evaluación.</p>
                
                <div class="mb-4">
                    <label class="form-label fw-bold">1. Lorem ipsum dolor sit amet?</label>
                    <textarea class="form-control" rows="3" placeholder="Respuesta..."></textarea>
                </div>
                
                <div class="mb-5">
                    <label class="form-label fw-bold">2. Consectetur adipiscing elit, sed do eiusmod tempor incididunt?</label>
                    <select class="form-select">
                        <option>Seleccionar opción</option>
                        <option>Opción A</option>
                        <option>Opción B</option>
                        <option>Opción C</option>
                    </select>
                </div>
                
                <hr class="my-4 text-muted">
                
                <div class="d-flex justify-content-between">
                    <button class="btn btn-outline-secondary px-4 py-2" onclick="nextStep(2)">
                        <i class="bi bi-arrow-left me-2"></i> Atrás
                    </button>
                    <button class="btn text-white px-4 py-2" style="background-color: #1a4a5b;" onclick="nextStep(4)">
                        Siguiente <i class="bi bi-arrow-right ms-2"></i>
                    </button>
                </div>
            </div>
            
            <!-- PASO 4: Datos del Participante -->
            <div id="step4" class="step-container">
                <h4 class="fw-bold mb-4 text-center">Paso 4: Datos del Participante</h4>
                <p class="text-muted text-center mb-5">Ingresa los datos del participante para guardar y finalizar la evaluación.</p>
                
                <form action="${pageContext.request.contextPath}/participantes" method="post" id="formFinalizar">
                    <!-- Suponiendo que recibe el idPrueba por parámetro en la url, lo tomamos por getParameter en un entorno JSP. -->
                    <input type="hidden" name="action" value="create">
                    <input type="hidden" name="idPrueba" value="${not empty param.idPrueba ? param.idPrueba : '0'}">
                    
                    <div class="mb-3">
                        <label class="form-label fw-medium">Nombre completo: <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" name="nombre" placeholder="Ej. Juan Pérez" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-medium">Edad / Rango de Edad: <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" name="edad" placeholder="Ej. 23" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-medium">Sexo / Género: <span class="text-danger">*</span></label>
                        <select class="form-select" name="sexo" required>
                            <option value="">Selecciona una opción</option>
                            <option value="Femenino">Femenino</option>
                            <option value="Masculino">Masculino</option>
                            <option value="Otro">Otro</option>
                        </select>
                    </div>

                    <hr class="my-4 text-muted">
                    
                    <div class="d-flex justify-content-between">
                        <button type="button" class="btn btn-outline-secondary px-4 py-2" onclick="nextStep(3)">
                            <i class="bi bi-arrow-left me-2"></i> Atrás
                        </button>
                        <button type="submit" class="btn text-white px-4 py-2" style="background-color: #3b8285;" onclick="detenerGrabacion()">
                            <i class="bi bi-check-circle me-2"></i> Finalizar y Guardar
                        </button>
                    </div>
                </form>
            </div>
            
        </div>
    </div>
</div>

<script>
    let isRecording = false;
    
    function toggleRecording() {
        const btn = document.getElementById('btnRecord');
        const icon = document.getElementById('iconRecord');
        const status = document.getElementById('recordStatus');
        
        isRecording = !isRecording;
        
        if (isRecording) {
            btn.classList.add('recording');
            icon.classList.replace('bi-mic-fill', 'bi-stop-fill');
            status.classList.remove('d-none');
        } else {
            btn.classList.remove('recording');
            icon.classList.replace('bi-stop-fill', 'bi-mic-fill');
            status.classList.add('d-none');
        }
    }

    function nextStep(stepNumber) {
        // Ocultar todos los pasos
        document.querySelectorAll('.step-container').forEach(el => {
            el.classList.remove('active');
        });
        
        // Mostrar el paso solicitado
        document.getElementById('step' + stepNumber).classList.add('active');
    }

    function detenerGrabacion() {
        if (isRecording) {
            toggleRecording(); // Detiene la simulación
        }
    }
</script>

</body>
</html>
