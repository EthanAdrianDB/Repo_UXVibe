# UXVibe — Proyecto Maven con Base de Datos Oracle

Plataforma de evaluación de usabilidad configurada para conectarse a **Oracle Autonomous Database** utilizando **HikariCP** y el patrón **DAO**.

## Configuración y Ejecución

El proyecto está preparado para funcionar *out-of-the-box* si se proporcionan las credenciales correctas. Por motivos de seguridad, las credenciales no se incluyen en el repositorio.

### Pasos para importarlo y ejecutarlo:

1. **Clonar e Importar**: Abre tu IDE (IntelliJ / Eclipse / NetBeans) → **Open/Import Project** → selecciona la carpeta del repositorio (donde está el `pom.xml`).
2. **Descargar Dependencias**: Deja que Maven descargue las dependencias necesarias.
3. **Agregar la Wallet de Oracle**:
   - Descarga los archivos de la Wallet de Oracle.
   - Crea la carpeta `src/main/resources/wallet/` dentro del proyecto.
   - Coloca todos los archivos de la Wallet descomprimida dentro de esa carpeta (ej. `cwallet.sso`, `tnsnames.ora`, `ewallet.p12`, `keystore.jks`, etc.).
   - *Nota: El sistema (`SQLConnector`) buscará y resolverá la ruta de la Wallet automáticamente.*
4. **Configurar las Credenciales**:
   - Crea un archivo llamado `credentials.properties` en `src/main/resources/`.
   - Añade el usuario, contraseña y nombre de conexión de tu base de datos Oracle:
     ```properties
     db.name=bduxvibe_high
     db.user=ADMIN
     db.pass=TU_CONTRASEÑA
     ```
   *(Asegúrate de que `db.name` coincida con el nombre de tu conexión definido en el archivo `tnsnames.ora` de la Wallet)*
5. **Ejecutar**: Configura tu servidor Tomcat 10+ (necesita Jakarta EE 9+) en tu IDE y ejecuta el proyecto. Las tablas se inicializarán automáticamente en la base de datos si no existen.

## Estructura del Proyecto

El proyecto sigue el patrón **DAO (Data Access Object)** para interactuar con la base de datos de manera limpia, utilizando `HikariCP` para el pool de conexiones.

```text
src/main/java/mx/edu/utez/uxvibe/
├── model/
│   ├── Evaluador.java, Participante.java, Prueba.java, Respuesta.java, ArchivoAudio.java
│   └── dao/
│       ├── Dao.java                 <- Interfaz genérica <T,K>
│       ├── EvaluadorDao.java
│       ├── ParticipanteDao.java
│       ├── PruebaDao.java
│       └── ...
├── utils/
│   ├── SQLConnector.java <- Maneja HikariCP y la conexión con Oracle DB (inicializa tablas autom.)
│   ├── PasswordUtil.java <- Utilidad de encriptado SHA-256
│   └── EmailSender.java  <- Envío de correos para recuperación de contraseña
└── controller/
    ├── LoginServlet.java
    ├── RegisterServlet.java
    ├── ResultadosServlet.java
    └── ...
```

## Flujo de Evaluación

1. **Registro/Login**: El Evaluador crea una cuenta.
2. **Pruebas**: El Evaluador crea "Pruebas" que contienen un grupo de Participantes.
3. **Participantes**: Se captura la información general del participante antes de la sesión.
4. **Cuestionario**: El participante llena una escala Likert de usabilidad, un modelo SAM (valencia, activación, dominio) de 1-9 y escalas de frecuencia de emociones.
5. **Resultados**: El Evaluador analiza las gráficas, distribuciones y puntajes generales desde el panel (con validaciones seguras a través de la sesión).
6. **Seguridad Integrada**: Sistema robusto para el encriptado de claves, checklist de requisitos dinámico, validación de acceso por IDs y manejo seguro de recursos (try-with-resources).

> **Nota:** Si al ejecutar el proyecto experimentas errores de conexión o un "Error 500", verifica que tu carpeta `wallet` está correctamente ubicada en `src/main/resources/` y que `credentials.properties` cuenta con tus accesos válidos.
