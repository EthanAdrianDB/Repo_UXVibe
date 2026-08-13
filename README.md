# UXVibe — Proyecto Maven con Base de Datos Oracle

Esta versión del proyecto está configurada para conectarse a **Oracle Autonomous Database** utilizando el patrón DAO.

## Configuración y Ejecución

Para que el proyecto se ejecute correctamente al clonarlo, **es necesario configurar las credenciales de la base de datos y la Wallet**, las cuales NO están incluidas en este repositorio por motivos de seguridad.

### Pasos para importarlo y ejecutarlo:

1. **Clonar e Importar**: Abre tu IDE (IntelliJ / Eclipse / NetBeans) → **Open/Import Project** → selecciona la carpeta del repositorio (donde está el `pom.xml`). Es un proyecto Maven normal.
2. **Descargar Dependencias**: Deja que Maven descargue las dependencias (el conector de Oracle OJDBC, JSTL, jakarta.servlet-api, etc.).
3. **Agregar la Wallet de Oracle**:
   - Descarga o solicita los archivos de la Wallet de Oracle de tu equipo (tu profesor o compañero).
   - Crea la carpeta `src/main/resources/wallet/` dentro del proyecto.
   - Coloca todos los archivos de la Wallet descomprimida dentro de esa carpeta (ej. `cwallet.sso`, `tnsnames.ora`, `ewallet.p12`, `keystore.jks`, etc.).
4. **Configurar las Credenciales**:
   - Crea un archivo llamado `credentials.properties` en `src/main/resources/`.
   - Añade el usuario, contraseña y URL de conexión (que corresponde al nombre de la conexión en el archivo `tnsnames.ora` de tu Wallet). Ejemplo del contenido del archivo:
     ```properties
     db.url=jdbc:oracle:thin:@nombre_de_conexion_high?TNS_ADMIN=RUTA_ABSOLUTA_A_TU_CARPETA_WALLET
     db.user=TU_USUARIO
     db.password=TU_CONTRASEÑA
     ```
   *(Asegúrate de ajustar la ruta `TNS_ADMIN` a la ruta absoluta de la carpeta wallet en tu computadora, por ejemplo: `C:/Ruta/A/Tu/Proyecto/src/main/resources/wallet`)*
5. **Ejecutar**: Configura tu servidor Tomcat 10+ (necesita Jakarta EE 9+) en tu IDE y ejecuta el proyecto.

## Estructura del Proyecto

El proyecto sigue el patrón **DAO (Data Access Object)** para interactuar con la base de datos de manera limpia.

```
src/main/java/mx/edu/utez/uxvibe/
├── model/
│   ├── Investigador.java, Colaborador.java, SesionEvaluacion.java, RespuestaCuestionario.java, etc.
│   └── dao/
│       ├── Dao.java                 <- Interfaz genérica <T,K>
│       ├── InvestigadorDao.java
│       ├── ParticipanteDao.java
│       └── ...
├── utils/
│   ├── SQLConnector.java <- Maneja la conexión con Oracle DB
│   ├── PasswordUtil.java <- Encriptado SHA-256
│   └── EmailSender.java  <- Utilidad de envío de correos simulado
└── controller/
    ├── LoginServlet.java
    ├── RegisterServlet.java
    └── ...
```

## Flujo de Evaluación (Actualización)
- Ahora el flujo de evaluación para el investigador (`evaluacion-investigador.jsp`) consta de la grabación de audio, la captura del participante mediante el formulario y las preguntas de evaluación.
- Las vistas JSP están construidas y estilizadas con Bootstrap 5 y se encuentran en la ruta `src/main/webapp/`.

> **Nota:** Si al ejecutar el proyecto experimentas errores de conexión o un "Error 500" al intentar iniciar sesión o registrar un usuario, asegúrate de que la ruta absoluta hacia la carpeta de tu Wallet y las credenciales estén correctamente escritas en tu archivo `credentials.properties`.
