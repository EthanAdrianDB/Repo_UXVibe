# UXVibe — Proyecto Maven con "base de datos" en CSV

Estructura calcada del ejemplo de tu profesor (`mx.edu.utez...`, patrón `Dao<T,K>`,
Jakarta Servlets 6, filtro de sesión), pero usando **archivos CSV en vez de Oracle/SQL**
para que le puedas explicar el patrón DAO a tu equipo sin necesitar un motor de BD real.

## Cómo importarlo

1. Abre tu IDE (IntelliJ / Eclipse / NetBeans) → **Open/Import Project** → selecciona
   la carpeta `UXVibeMaven` (donde está el `pom.xml`). Es un proyecto Maven normal.
2. Deja que Maven descargue las dependencias (JSTL, jakarta.servlet-api).
3. Despliega en Tomcat 10+ (necesita Jakarta EE 9+, por el namespace `jakarta.*`).
4. Ve más abajo "Dónde se guardan los datos" para saber dónde buscar tus CSV.

## Dónde se guardan los datos

**Ya NO se guardan en una carpeta `/data` dentro del proyecto.** Se guardan
automáticamente en tu carpeta personal de usuario, para que siempre sea la
misma ruta sin importar desde dónde arranque Tomcat:

- Windows: `C:\Users\TU_USUARIO\uxvibe-data`
- Mac/Linux: `/Users/TU_USUARIO/uxvibe-data` o `/home/TU_USUARIO/uxvibe-data`

La carpeta y los 5 archivos CSV (con su encabezado) se crean **solos** la
primera vez que corres el proyecto. Además, cada vez que arranca, la consola
de tu servidor imprime la ruta exacta que está usando, algo como:

```
UXVibe: usando carpeta de datos -> /Users/dan/uxvibe-data
```

Si algún día quieren usar otra ubicación (por ejemplo para que todo el equipo
apunte a la misma carpeta compartida), pueden fijarla con la variable de
entorno `UXVIBE_DATA_DIR` antes de correr Tomcat.

## La "base de datos" (5 archivos CSV)

```
evaluadores.csv     id,nombres,apellidoPaterno,apellidoMaterno,correo,contrasenaHash,salt,rol,codigoRecuperacion
pruebas.csv         id,idEvaluador,nombre,plataforma,url,tarea,fechaCreacion
preguntas.csv       id,idPrueba,texto,orden,tipo
participantes.csv   id,idPrueba,nombre,edad,sexo,fechaRealizacion,duracionSegundos,audioPath
respuestas.csv      id,idParticipante,idPregunta,valor
```

Cada archivo es una "tabla": la primera línea son las columnas, cada línea
siguiente es un registro. `CsvUtil.java` es la clase que sabe leer y escribir
estos archivos — ahí es donde le puedes explicar a tu equipo cómo funciona
"por debajo" en vez de usar JDBC.

## Estructura del código (idéntica al patrón del ejemplo)

```
src/main/java/mx/edu/utez/uxvibe/
├── model/
│   ├── Evaluador.java, Prueba.java, Pregunta.java, Participante.java, Respuesta.java
│   └── dao/
│       ├── Dao.java                 <- interfaz genérica <T,K>: create/getAll/getById/update/delete
│       ├── EvaluadorDao.java
│       ├── PruebaDao.java
│       ├── PreguntaDao.java
│       ├── ParticipanteDao.java
│       └── RespuestaDao.java
├── utils/
│   ├── CsvUtil.java      <- lectura/escritura genérica de CSV (el "motor de BD")
│   ├── PasswordUtil.java <- hash SHA-256 + salt
│   └── EmailSender.java  <- "envío de correo" simulado (imprime en consola)
└── controller/
    ├── LoginServlet.java            /login
    ├── RegisterServlet.java         /registro
    ├── LogoutServlet.java           /logout
    ├── RecuperarServlet.java        /recuperar   (action=recuperar|checar|actualizar)
    ├── InicioServlet.java           /inicio      (Dashboard)
    ├── PruebaServlet.java           /prueba      (action=create|update|delete)
    ├── PreguntaServlet.java         /preguntas   (action=create|delete)
    ├── ParticipanteServlet.java     /participantes (action=create|delete)
    ├── ParticipanteDetalleServlet.java  /participante-detalle
    ├── ResultadosServlet.java       /resultados
    ├── AudioServlet.java            /audio
    ├── PerfilServlet.java           /perfil      (action=editarInfo|cambiarContrasena)
    └── filters/FiltroAutenticacion.java   <- protege todo excepto login/registro/recuperación/cuestionario
```

## Vistas (JSP + JSTL + Bootstrap 5 vía CDN)

```
src/main/webapp/
├── layout/header.jsp, layout/footer.jsp   <- navbar y cierre de <html>, se incluyen en cada página interna
├── prueba-tabs.jspf                        <- tabs reusables (Preguntas/Resultados/Participantes/Audio)
├── login.jsp, registro.jsp, terminos.jsp
├── recuperar-contra.jsp, colocar-codigo.jsp, cambiar-contra.jsp
├── inicio.jsp                (dashboard + form crear/editar prueba)
├── gestion-preguntas.jsp     (capturar preguntas Likert/SAM de una prueba)
├── gestion-participantes.jsp
├── participante-detalle.jsp
├── audio.jsp
├── resultados.jsp            (gráficas simples con barras en CSS puro)
├── perfil.jsp
└── cuestionario-inicio.jsp, cuestionario.jsp, cuestionario-gracias.jsp   <- flujo público, sin login
```

> Nota: los estilos usan Bootstrap 5 y Bootstrap Icons por CDN (jsdelivr), a
> diferencia del ejemplo de tu profe que los trae como archivos locales en
> `assets/`. Si prefieren tenerlos localmente (sin depender de internet),
> pueden descargar `bootstrap.min.css`/`bootstrap.bundle.min.js` y
> `bootstrap-icons` y ponerlos en `webapp/assets/`, cambiando los `<link>`
> y `<script>` de `layout/header.jsp` y `layout/footer.jsp`.

## 🔧 Fix importante (v2): el login no dejaba entrar

Si ya habías probado una versión anterior de este ZIP y el registro "parecía"
funcionar pero el login siempre daba "correo o contraseña incorrectos", era
un bug real: `CsvUtil` usaba una ruta **relativa** (`"data"`), y esa ruta
depende de la carpeta desde donde arranca Tomcat — casi nunca es la carpeta
de tu proyecto. Entonces el registro se guardaba en un lugar que no era el
esperado (o fallaba en silencio), y el login nunca encontraba al usuario.

**Ya está arreglado:** ahora los CSV se guardan automáticamente en una ruta
fija y absoluta: tu carpeta de usuario + `/uxvibe-data`
(ej. `C:\Users\Dan\uxvibe-data` en Windows o `/home/dan/uxvibe-data` en Linux/Mac).
Esa carpeta y los 5 archivos con su encabezado se crean solos la primera vez
que corres el proyecto — no necesitas copiar nada a mano.
Si prefieren otra ubicación, pueden fijarla con la variable de entorno
`UXVIBE_DATA_DIR`.

## 🎨 Diseño (v2): ahora sí se parece a tu Figma

La primera versión usaba un navbar de Bootstrap arriba, genérico. Ya lo cambié:

- Sidebar oscura fija a la izquierda (Inicio / Perfil / Cerrar sesión), igual
  que tus wireframes.
- Login y Registro son pantallas **split-screen** con los mismos colores
  (`#173E45` para login, `#E6DECB` para registro).
- Toda la paleta de tu guía de estilos aplicada vía `assets/css/uxvibe.css`
  (botones, tablas, tabs, tarjetas).

Si aun así hay pantallas que se ven distintas a como las tienes en Figma,
dime cuáles específicamente (o mándame captura) y las ajusto una por una —
armar el HTML/CSS exacto de cada pantalla es más rápido si trabajamos sobre
la que no cuadre en vez de que yo adivine el pixel-perfect de las 15 vistas.

## Flujo completo


1. **Registro/Login** → guarda al evaluador en sesión.
2. **Inicio** → dashboard con las pruebas del evaluador logueado + form para crear/editar.
3. Cada prueba tiene 4 pestañas: **Preguntas** (aquí el evaluador arma su
   cuestionario, eligiendo tipo Likert o SAM), **Resultados**, **Participantes**, **Audio**.
4. **Cuestionario** (`/cuestionario?idPrueba=X`) es la parte pública: la
   comparten con la gente que va a evaluar la plataforma. Pide datos
   demográficos, muestra una pregunta a la vez, y al final calcula la
   duración total y guarda todo.
5. **Perfil** → editar información y cambiar contraseña.

## Pendientes / decisiones para tu equipo

- **Grabación real de audio** en `/cuestionario` (MediaRecorder API del
  navegador) — el campo `audioPath` ya existe en `participantes.csv`, falta
  la subida real del archivo.
- **Envío real de correo** en recuperación de contraseña — por ahora
  `EmailSender.java` solo imprime en consola; si quieren correo real
  agregarían la dependencia `jakarta.mail` al `pom.xml`.
- El CSV se reescribe completo en cada operación (simple de entender, pero
  no pensado para muchísimos registros concurrentes — para un proyecto
  escolar es más que suficiente).

# UXVibe_Repo_FInish
