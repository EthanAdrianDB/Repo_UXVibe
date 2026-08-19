<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/images/logoux.png" type="image/png">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>UXVibe | Cuestionario de Evaluación</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/uxvibe.css" rel="stylesheet">
    <style>
        .opcion-escala {
            width:40px; height:40px; border-radius:50%; border:2px solid #CAD3D2;
            display:flex; align-items:center; justify-content:center; font-weight:bold; cursor:pointer;
            transition: all 0.2s ease;
        }
        input[type=radio] { display:none; }
        input[type=radio]:checked + .opcion-escala { border-color:#173E45; background:#173E45; color: white; transform: scale(1.1); }
    </style>
</head>
<body class="bg-light">
<div class="container py-5" style="max-width:800px;">
    <div class="card shadow-sm">
        <div class="card-body p-4">
            <h4 class="text-center mb-1">CUESTIONARIO DE EVALUACIÓN</h4>
            <p class="text-center text-muted small border-bottom pb-3">Por favor responde a todas las preguntas</p>

            <form action="${pageContext.request.contextPath}/cuestionario" method="post" id="formPregunta">
                <input type="hidden" name="action" value="responder">

                <!-- 15 Preguntas tipo Likert -->
                <h5 class="mt-4 mb-3 text-dark border-bottom pb-2 fw-bold">Sección 1: Preguntas Generales de Usabilidad</h5>
                <p class="text-muted small mb-4">Califica del 1 al 5 (donde 1 es Totalmente en desacuerdo y 5 es Totalmente de acuerdo)</p>
                <% 
                    String[] uxPreguntas = {
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
                    for (int i = 0; i < uxPreguntas.length; i++) {
                %>
                    <div class="mb-4 p-3 bg-white border rounded">
                        <label class="form-label fw-bold mb-2"><%= uxPreguntas[i] %></label>
                        <div class="d-flex justify-content-between align-items-center pt-2">
                            <span class="small text-muted" style="font-size: 0.8rem;">Totalmente en desacuerdo (1)</span>
                            <div class="d-flex gap-2 justify-content-center">
                                <% for (int v = 1; v <= 5; v++) { %>
                                    <label>
                                        <input type="radio" name="r<%= (i + 1) %>" value="<%= v %>" required>
                                        <div class="opcion-escala"><%= v %></div>
                                    </label>
                                <% } %>
                            </div>
                            <span class="small text-muted" style="font-size: 0.8rem;">Totalmente de acuerdo (5)</span>
                        </div>
                    </div>
                <% } %>

                <!-- 3 Preguntas SAM -->
                <h5 class="mt-5 mb-3 text-dark border-bottom pb-2 fw-bold">Sección 2: Evaluación Emocional (Escala SAM)</h5>
                <p class="text-muted small mb-4">Califica tu estado emocional durante la interacción (Escala 1 a 5)</p>
                
                <div class="mb-4 p-3 bg-white border rounded">
                    <label class="form-label fw-bold mb-1">Valencia: ¿Cómo te sientes después de haber interactuado con la página/sistema Web?</label>
                    <div class="d-flex justify-content-between align-items-center pt-2">
                        <span class="small text-muted" style="font-size: 0.8rem;">1 (Muy mal)</span>
                        <div class="d-flex gap-2 justify-content-center">
                            <% for (int v = 1; v <= 5; v++) { %>
                                <label>
                                    <input type="radio" name="sam1" value="<%= v %>" required>
                                    <div class="opcion-escala"><%= v %></div>
                                </label>
                            <% } %>
                        </div>
                        <span class="small text-muted" style="font-size: 0.8rem;">5 (Muy bien)</span>
                    </div>
                </div>

                <div class="mb-4 p-3 bg-white border rounded">
                    <label class="form-label fw-bold mb-1">Activación: ¿Qué tan impactante o estimulante fue tu experiencia?</label>
                    <div class="d-flex justify-content-between align-items-center pt-2">
                        <span class="small text-muted" style="font-size: 0.8rem;">1 (Muy calmado)</span>
                        <div class="d-flex gap-2 justify-content-center">
                            <% for (int v = 1; v <= 5; v++) { %>
                                <label>
                                    <input type="radio" name="sam2" value="<%= v %>" required>
                                    <div class="opcion-escala"><%= v %></div>
                                </label>
                            <% } %>
                        </div>
                        <span class="small text-muted" style="font-size: 0.8rem;">5 (Muy alterado)</span>
                    </div>
                </div>

                <div class="mb-4 p-3 bg-white border rounded">
                    <label class="form-label fw-bold mb-1">Dominio: ¿Qué tanto control tuviste sobre tus emociones y la navegación?</label>
                    <div class="d-flex justify-content-between align-items-center pt-2">
                        <span class="small text-muted" style="font-size: 0.8rem;">1 (Muy influenciado)</span>
                        <div class="d-flex gap-2 justify-content-center">
                            <% for (int v = 1; v <= 5; v++) { %>
                                <label>
                                    <input type="radio" name="sam3" value="<%= v %>" required>
                                    <div class="opcion-escala"><%= v %></div>
                                </label>
                            <% } %>
                        </div>
                        <span class="small text-muted" style="font-size: 0.8rem;">5 (Muy dominante)</span>
                    </div>
                </div>

                <!-- 2 Frecuencias de estado de ánimo -->
                <h5 class="mt-5 mb-3 text-dark border-bottom pb-2 fw-bold">Sección 3: Frecuencia de Estados de Ánimo</h5>
                <div class="mb-4 p-3 bg-white border rounded">
                    <label class="form-label fw-bold mb-2">En una semana típica, ¿con qué frecuencia te sientes estresado/a?</label>
                    <select class="form-select" name="frecuenciaEstadoAnimo1" required>
                        <option value="">Selecciona una opción</option>
                        <option value="1">Nunca</option>
                        <option value="2">De vez en cuando</option>
                        <option value="3">Cerca de la mitad del tiempo</option>
                        <option value="4">La mayor parte del tiempo</option>
                        <option value="5">Siempre</option>
                    </select>
                </div>

                <div class="mb-4 p-3 bg-white border rounded">
                    <label class="form-label fw-bold mb-2">En una semana típica, ¿con qué frecuencia te sientes relajado/a?</label>
                    <select class="form-select" name="frecuenciaEstadoAnimo2" required>
                        <option value="">Selecciona una opción</option>
                        <option value="1">Nunca</option>
                        <option value="2">De vez en cuando</option>
                        <option value="3">Cerca de la mitad del tiempo</option>
                        <option value="4">La mayor parte del tiempo</option>
                        <option value="5">Siempre</option>
                    </select>
                </div>

                <!-- Botón que abre el modal de confirmación -->
                <button type="button" class="btn text-white w-100 py-3 mt-4" style="background-color: #3b8285;" data-bs-toggle="modal" data-bs-target="#modalConfirmarGuardar">
                    Enviar Cuestionario
                </button>
            </form>
        </div>
    </div>
</div>

<!-- Modal de Confirmación Guardar -->
<div class="modal fade" id="modalConfirmarGuardar" tabindex="-1" aria-labelledby="modalConfirmarGuardarLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow" style="border-radius: 4px;">
            <div class="modal-body p-5 text-center">
                <h5 class="fw-bold mb-5 mt-2">¿Seguro que quieres enviar tus respuestas?</h5>
                <div class="d-flex justify-content-center gap-3">
                    <button type="button" class="btn btn-outline-secondary px-4 py-2" style="width: 130px;" data-bs-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn text-white px-4 py-2" style="background-color: #1a4a5b; width: 130px;" onclick="document.getElementById('formPregunta').submit();">Enviar</button>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
