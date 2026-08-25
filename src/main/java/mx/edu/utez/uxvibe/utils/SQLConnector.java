package mx.edu.utez.uxvibe.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Conector principal a la Base de Datos Oracle Autonomous Database en la nube.
 * Implementa el pool de conexiones de alto rendimiento HikariCP,
 * localiza de forma automática la carpeta de la Wallet de Oracle y crea las tablas si aún no existen.
 */
public class SQLConnector {

    // Instancia única (Singleton) del DataSource de Hikari
    private static volatile HikariDataSource dataSource;

    /**
     * Obtiene una conexión activa del pool de conexiones HikariCP.
     */
    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    /**
     * Cierra el pool completo cuando se detiene el servidor.
     */
    public static void closeConnection() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    /**
     * Inicializa y configura el DataSource si aún no está creado (Lazy Initialization seguro para hilos).
     */
    private static synchronized HikariDataSource getDataSource() throws SQLException {
        if (dataSource != null && !dataSource.isClosed()) {
            return dataSource;
        }

        try {
            // 1. Localizar la carpeta de la Wallet con múltiples estrategias de fallback
            String walletPath = buscarRutaWallet();
            System.out.println("[SQLConnector] Ruta de Wallet detectada: " + walletPath);

            // 2. Leer credenciales y nombre de BD
            Properties creds = cargarCredenciales();
            String dbUser = creds.getProperty("db.user");
            String dbPass = creds.getProperty("db.pass");
            String dbName = creds.getProperty("db.name");

            if (dbName == null || dbName.trim().isEmpty() || dbName.contains("NOMBRE_DE_TU_CONEXION")) {
                throw new SQLException("El nombre de la base de datos (db.name) no está configurado correctamente en credentials.properties.");
            }
            if (dbUser == null || dbUser.trim().isEmpty()) {
                throw new SQLException("El usuario de la base de datos (db.user) no está configurado en credentials.properties.");
            }
            if (dbPass == null || dbPass.trim().isEmpty() || dbPass.equals("TU_PASSWORD_AQUI")) {
                throw new SQLException("Debes colocar tu contraseña real en 'db.pass' dentro de credentials.properties (actualmente está 'TU_PASSWORD_AQUI').");
            }

            // 3. Configuración de HikariCP para Oracle Autonomous Database
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("oracle.jdbc.OracleDriver");
            config.setJdbcUrl("jdbc:oracle:thin:@" + dbName + "?TNS_ADMIN=" + walletPath);
            config.setUsername(dbUser);
            config.setPassword(dbPass);

            // Parámetros de conexión y tamaño del pool
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setConnectionTimeout(20000);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            dataSource = new HikariDataSource(config);
            System.out.println("[SQLConnector] ¡Conexión a Oracle Cloud establecida con éxito!");

            // 4. Inicialización automática de tablas en la base de datos
            inicializarTablas(dataSource.getConnection());

            return dataSource;
        } catch (SQLException e) {
            System.err.println("[SQLConnector] Error SQL al inicializar DataSource: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("[SQLConnector] Error inesperado al inicializar DataSource: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Error de configuración de conexión a Oracle: " + e.getMessage(), e);
        }
    }

    private static String buscarRutaWallet() {
        // Estrategia 1: ClassLoader de Tomcat / Java
        ClassLoader cl = SQLConnector.class.getClassLoader();
        URL walletUrl = cl != null ? cl.getResource("wallet") : null;
        if (walletUrl == null) {
            cl = Thread.currentThread().getContextClassLoader();
            if (cl != null) walletUrl = cl.getResource("wallet");
        }

        if (walletUrl != null) {
            try {
                File f = Paths.get(walletUrl.toURI()).toFile();
                if (f.exists() && f.isDirectory() && new File(f, "tnsnames.ora").exists()) {
                    return f.getAbsolutePath().replace("\\", "/");
                }
            } catch (Exception ignored) {
                File f = new File(walletUrl.getPath());
                if (f.exists() && f.isDirectory() && new File(f, "tnsnames.ora").exists()) {
                    return f.getAbsolutePath().replace("\\", "/");
                }
            }
        }

        // Estrategia 2: Rutas directas en disco
        File[] candidatosWallet = new File[] {
            new File("src/main/resources/wallet"),
            new File("C:/Users/artur/Desktop/Repo_UXVibe/src/main/resources/wallet"),
            new File("../src/main/resources/wallet"),
            new File("../../src/main/resources/wallet")
        };
        for (File candidato : candidatosWallet) {
            if (candidato.exists() && candidato.isDirectory() && new File(candidato, "tnsnames.ora").exists()) {
                return candidato.getAbsolutePath().replace("\\", "/");
            }
        }

        // Estrategia 3: Buscar subiendo desde la ubicación del .class
        try {
            URL codeLocation = SQLConnector.class.getProtectionDomain().getCodeSource().getLocation();
            if (codeLocation != null) {
                File current = Paths.get(codeLocation.toURI()).toFile();
                for (int i = 0; i < 8 && current != null; i++) {
                    File candidate = new File(current, "src/main/resources/wallet");
                    if (candidate.exists() && candidate.isDirectory() && new File(candidate, "tnsnames.ora").exists()) {
                        return candidate.getAbsolutePath().replace("\\", "/");
                    }
                    File candidate2 = new File(current, "wallet");
                    if (candidate2.exists() && candidate2.isDirectory() && new File(candidate2, "tnsnames.ora").exists()) {
                        return candidate2.getAbsolutePath().replace("\\", "/");
                    }
                    current = current.getParentFile();
                }
            }
        } catch (Exception ignored) {}

        throw new RuntimeException("No se encontró la carpeta 'wallet' con los archivos de Oracle (tnsnames.ora, cwallet.sso).");
    }

    private static Properties cargarCredenciales() {
        Properties creds = new Properties();

        // 1. Variables de entorno
        String envUser = System.getenv("DB_USER");
        String envPass = System.getenv("DB_PASS");
        String envName = System.getenv("DB_NAME");

        if (envUser != null) creds.setProperty("db.user", envUser);
        if (envPass != null) creds.setProperty("db.pass", envPass);
        if (envName != null) creds.setProperty("db.name", envName);

        if (esValido(creds.getProperty("db.name")) && esValido(creds.getProperty("db.user")) && esValido(creds.getProperty("db.pass"))) {
            return creds;
        }

        // 2. Intentar cargar desde el ClassLoader
        ClassLoader cl = SQLConnector.class.getClassLoader();
        InputStream is = cl != null ? cl.getResourceAsStream("credentials.properties") : null;
        if (is == null) {
            cl = Thread.currentThread().getContextClassLoader();
            if (cl != null) is = cl.getResourceAsStream("credentials.properties");
        }

        if (is != null) {
            try (InputStream stream = is) {
                creds.load(stream);
            } catch (Exception e) {
                System.err.println("[SQLConnector] Error leyendo credentials.properties desde classpath: " + e.getMessage());
            }
        }

        // 3. Fallback: Buscar archivo en el disco si aún no tiene datos válidos
        if (!esValido(creds.getProperty("db.name")) || !esValido(creds.getProperty("db.pass"))) {
            File[] candidatos = new File[] {
                new File("src/main/resources/credentials.properties"),
                new File("C:/Users/artur/Desktop/Repo_UXVibe/src/main/resources/credentials.properties"),
                new File("../src/main/resources/credentials.properties"),
                new File("../../src/main/resources/credentials.properties")
            };
            for (File candidato : candidatos) {
                if (candidato.exists()) {
                    try (InputStream stream = new FileInputStream(candidato)) {
                        creds.load(stream);
                        if (esValido(creds.getProperty("db.name")) && esValido(creds.getProperty("db.pass"))) {
                            System.out.println("[SQLConnector] Credenciales cargadas exitosamente desde: " + candidato.getAbsolutePath());
                            break;
                        }
                    } catch (Exception ignored) {}
                }
            }
        }

        // 4. Buscar desde la ubicación del .class
        if (!esValido(creds.getProperty("db.name")) || !esValido(creds.getProperty("db.pass"))) {
            try {
                URL codeLocation = SQLConnector.class.getProtectionDomain().getCodeSource().getLocation();
                if (codeLocation != null) {
                    File current = Paths.get(codeLocation.toURI()).toFile();
                    for (int i = 0; i < 8 && current != null; i++) {
                        File candidate = new File(current, "src/main/resources/credentials.properties");
                        if (candidate.exists()) {
                            try (InputStream stream = new FileInputStream(candidate)) {
                                creds.load(stream);
                                if (esValido(creds.getProperty("db.name")) && esValido(creds.getProperty("db.pass"))) {
                                    break;
                                }
                            }
                        }
                        current = current.getParentFile();
                    }
                }
            } catch (Exception ignored) {}
        }

        // Normalizar db.password -> db.pass
        if (creds.getProperty("db.pass") == null && creds.getProperty("db.password") != null) {
            creds.setProperty("db.pass", creds.getProperty("db.password"));
        }

        // Valores por defecto de emergencia para el proyecto si faltaran
        if (!esValido(creds.getProperty("db.name"))) {
            creds.setProperty("db.name", "bduxvibe_high");
        }
        if (!esValido(creds.getProperty("db.user"))) {
            creds.setProperty("db.user", "ADMIN");
        }

        return creds;
    }

    private static boolean esValido(String valor) {
        return valor != null && !valor.trim().isEmpty() && !valor.contains("NOMBRE_DE_TU_CONEXION") && !valor.equals("TU_PASSWORD_AQUI");
    }

    /**
     * Revisa si las tablas de la base de datos ya existen en Oracle Cloud.
     * Si no existen, ejecuta los scripts DDL para crearlas automáticamente en orden
     * (Evaluador -> Prueba -> Participante -> Respuesta -> Archivo_Audio).
     */
    public static void inicializarTablas() {
        try (Connection con = getConnection()) {
            inicializarTablas(con);
        } catch (SQLException e) {
            System.err.println("[SQLConnector] Error al inicializar tablas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void inicializarTablas(Connection con) {
        try (Statement stmt = con.createStatement()) {

            // Verificamos si la tabla EVALUADOR ya existe en el diccionario de datos de Oracle
            boolean tablasExisten = false;
            try (ResultSet rs = stmt.executeQuery("SELECT count(*) FROM user_tables WHERE UPPER(table_name) = 'EVALUADOR'")) {
                if (rs.next() && rs.getInt(1) > 0) {
                    tablasExisten = true;
                }
            }

            if (!tablasExisten) {
                System.out.println("[SQLConnector] Las tablas no existen en la base de datos. Creando esquema...");

                stmt.execute("CREATE TABLE Evaluador (" +
                        "id_evaluador NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, " +
                        "nombre VARCHAR2(100) NOT NULL, " +
                        "apellido_m VARCHAR2(100), " +
                        "apellido_p VARCHAR2(100) NOT NULL, " +
                        "correo VARCHAR2(150) UNIQUE NOT NULL, " +
                        "contrasena VARCHAR2(256) NOT NULL" +
                        ")");

                stmt.execute("CREATE TABLE Prueba (" +
                        "id_prueba NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, " +
                        "nombre VARCHAR2(150) NOT NULL, " +
                        "descripcion VARCHAR2(4000), " +
                        "url_sistema VARCHAR2(255), " +
                        "id_evaluador NUMBER NOT NULL, " +
                        "CONSTRAINT fk_prueba_evaluador FOREIGN KEY (id_evaluador) REFERENCES Evaluador(id_evaluador) ON DELETE CASCADE" +
                        ")");

                stmt.execute("CREATE TABLE Participante (" +
                        "id_participante NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, " +
                        "nombre VARCHAR2(150) NOT NULL, " +
                        "apellido_m VARCHAR2(100), " +
                        "apellido_p VARCHAR2(100), " +
                        "sexo NUMBER, " +
                        "id_prueba NUMBER NOT NULL, " +
                        "edad NUMBER, " +
                        "fecha_realizacion VARCHAR2(50), " +
                        "CONSTRAINT fk_participante_prueba FOREIGN KEY (id_prueba) REFERENCES Prueba(id_prueba) ON DELETE CASCADE" +
                        ")");

                stmt.execute("CREATE TABLE Respuesta (" +
                        "id_respuestas NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, " +
                        "id_participante NUMBER NOT NULL, " +
                        "id_prueba NUMBER NOT NULL, " +
                        "sam_1 NUMBER, " +
                        "sam_2 NUMBER, " +
                        "sam_3 NUMBER, " +
                        "r1 NUMBER, r2 NUMBER, r3 NUMBER, r4 NUMBER, r5 NUMBER, " +
                        "r6 NUMBER, r7 NUMBER, r8 NUMBER, r9 NUMBER, r10 NUMBER, " +
                        "r11 NUMBER, r12 NUMBER, r13 NUMBER, r14 NUMBER, r15 NUMBER, " +
                        "frecuencia_estado_animo_1 NUMBER, " +
                        "frecuencia_estado_animo_2 NUMBER, " +
                        "CONSTRAINT fk_resp_participante FOREIGN KEY (id_participante) REFERENCES Participante(id_participante) ON DELETE CASCADE, " +
                        "CONSTRAINT fk_resp_prueba FOREIGN KEY (id_prueba) REFERENCES Prueba(id_prueba) ON DELETE CASCADE" +
                        ")");

                stmt.execute("CREATE TABLE Archivo_Audio (" +
                        "id_audio NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, " +
                        "id_participante NUMBER NOT NULL, " +
                        "id_prueba NUMBER NOT NULL, " +
                        "audio BLOB, " +
                        "CONSTRAINT fk_audio_participante FOREIGN KEY (id_participante) REFERENCES Participante(id_participante) ON DELETE CASCADE, " +
                        "CONSTRAINT fk_audio_prueba FOREIGN KEY (id_prueba) REFERENCES Prueba(id_prueba) ON DELETE CASCADE" +
                        ")");

                System.out.println("[SQLConnector] Tablas creadas con éxito en Oracle Autonomous Database.");
            } else {
                System.out.println("[SQLConnector] Las tablas ya existen en la base de datos. Verificando actualizaciones de esquema...");
                // Agregar columnas nuevas de forma segura (ignora el error si ya existen)
                String[] columnasNuevas = {
                    "sam_1 NUMBER", "sam_2 NUMBER", "sam_3 NUMBER",
                    "frecuencia_estado_animo_1 NUMBER", "frecuencia_estado_animo_2 NUMBER"
                };
                for (String col : columnasNuevas) {
                    try {
                        stmt.execute("ALTER TABLE Respuesta ADD " + col);
                        System.out.println("[SQLConnector] Se agregó la columna " + col + " a Respuesta.");
                    } catch (SQLException ignored) {
                        // Si ya existe (ORA-01430) simplemente continuamos
                    }
                }
                
                // Buscar y eliminar constraints CHECK que limitan los valores de SAM
                try (ResultSet rs = stmt.executeQuery("SELECT constraint_name, search_condition FROM user_constraints WHERE table_name = 'RESPUESTA' AND constraint_type = 'C'")) {
                    while (rs.next()) {
                        String cName = rs.getString(1);
                        String cCond = rs.getString(2);
                        if (cCond != null && (cCond.toLowerCase().contains("sam") || cName.equals("SYS_C0047608"))) {
                            try (Statement dropStmt = con.createStatement()) {
                                dropStmt.execute("ALTER TABLE Respuesta DROP CONSTRAINT " + cName);
                                System.out.println("[SQLConnector] Constraint " + cName + " eliminada exitosamente para permitir valores de SAM > 5.");
                            } catch (SQLException ignored) {}
                        }
                    }
                } catch (SQLException ignored) {}
            }

        } catch (SQLException e) {
            System.err.println("[SQLConnector] Error al inicializar las tablas de la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try (Connection con = getConnection()) {
            if (con.isValid(5)) {
                System.out.println("[SQLConnector] ========================================");
                System.out.println("[SQLConnector] Conexión de prueba exitosa a Oracle Cloud.");
                System.out.println("[SQLConnector] ========================================");
                
                // 1. Inicializar tablas
                inicializarTablas(con);

                // 2. Listar tablas del usuario
                try (Statement stmt = con.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT table_name FROM user_tables ORDER BY table_name")) {
                    System.out.println("[SQLConnector] Tablas existentes en la base de datos:");
                    while (rs.next()) {
                        System.out.println("  - " + rs.getString("table_name"));
                    }
                }

                // 3. Probar inserción con EvaluadorDao
                mx.edu.utez.uxvibe.model.Evaluador testUser = new mx.edu.utez.uxvibe.model.Evaluador(
                        0, "Test", "Tester", "Demo", "test_" + System.currentTimeMillis() + "@test.com", "Hash12345"
                );
                mx.edu.utez.uxvibe.model.dao.EvaluadorDao dao = new mx.edu.utez.uxvibe.model.dao.EvaluadorDao();
                boolean insertado = dao.create(testUser);
                if (insertado) {
                    System.out.println("[SQLConnector] ¡Prueba de inserción en EVALUADOR exitosa! ID generado: " + testUser.getIdEvaluador());
                    dao.delete(testUser.getIdEvaluador());
                    System.out.println("[SQLConnector] Usuario de prueba eliminado limpiamente.");
                } else {
                    System.err.println("[SQLConnector] Falló la prueba de inserción. Error: " + mx.edu.utez.uxvibe.model.dao.EvaluadorDao.getUltimoError());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
