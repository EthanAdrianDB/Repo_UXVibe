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

public class SQLConnector {

    private static HikariDataSource dataSource;

    static {
        try {
            // 1. Localizar la carpeta de la Wallet
            ClassLoader classLoader = SQLConnector.class.getClassLoader();
            URL walletUrl = classLoader.getResource("wallet");

            if (walletUrl == null) {
                throw new RuntimeException("No se encontró la carpeta 'wallet' en resources.");
            }

            // Uso de Paths para evitar problemas con caracteres especiales o espacios en la
            // ruta de Tomcat
            String walletPath = Paths.get(walletUrl.toURI()).toAbsolutePath().toString();
            walletPath = walletPath.replace("\\", "/");

            // 2. Intentar leer credenciales y nombre de BD desde el entorno o
            // credentials.properties
            String dbUser = System.getenv("DB_USER");
            String dbPass = System.getenv("DB_PASS");
            String dbName = System.getenv("DB_NAME");

            if (dbUser == null || dbPass == null || dbName == null) {
                System.out.println("Cargando credenciales desde credentials.properties...");
                Properties creds = new Properties();
                InputStream is = classLoader.getResourceAsStream("credentials.properties");

                if (is == null) {
                    File localCreds = new File("src/main/resources/credentials.properties");
                    if (localCreds.exists()) {
                        is = new FileInputStream(localCreds);
                    }
                }

                if (is == null) {
                    throw new RuntimeException(
                            "No se encontró el archivo credentials.properties en resources ni en src/main/resources.");
                }

                try (InputStream stream = is) {
                    creds.load(stream);
                    if (dbUser == null)
                        dbUser = creds.getProperty("db.user");
                    if (dbPass == null) {
                        dbPass = creds.getProperty("db.pass");
                        if (dbPass == null) {
                            dbPass = creds.getProperty("db.password");
                        }
                    }
                    if (dbName == null)
                        dbName = creds.getProperty("db.name");
                }
            }

            if (dbName == null || dbName.trim().isEmpty() || dbName.contains("NOMBRE_DE_TU_CONEXION")) {
                throw new RuntimeException(
                        "El nombre de la base de datos (db.name) no está configurado correctamente en credentials.properties.");
            }
            if (dbUser == null || dbUser.trim().isEmpty()) {
                throw new RuntimeException(
                        "El usuario de la base de datos (db.user) no está configurado en credentials.properties.");
            }
            if (dbPass == null || dbPass.trim().isEmpty() || dbPass.equals("TU_PASSWORD_AQUI")) {
                throw new RuntimeException(
                        "Debes colocar tu contraseña real en 'db.pass' dentro de credentials.properties (actualmente está 'TU_PASSWORD_AQUI').");
            }

            // 3. Configuración de HikariCP para Oracle
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("oracle.jdbc.OracleDriver");

            // Concatenación de la URL usando TNS_ADMIN
            config.setJdbcUrl("jdbc:oracle:thin:@" + dbName + "?TNS_ADMIN=" + walletPath);
            config.setUsername(dbUser);
            config.setPassword(dbPass);

            // Configuraciones del Pool
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setConnectionTimeout(20000);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            dataSource = new HikariDataSource(config);
            System.out.println("¡Conexión a Oracle Cloud establecida con éxito!");

            // 4. Inicialización automática de tablas deshabilitada ya que existen en la BD
            // real
            // inicializarTablas();

        } catch (Exception e) {
            System.err.println("Error al inicializar la base de datos");
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closeConnection() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    private static void inicializarTablas() {
        try (Connection con = getConnection();
                Statement stmt = con.createStatement()) {

            // Verificar si la tabla EVALUADORES existe
            boolean tablasExisten = false;
            try (ResultSet rs = stmt
                    .executeQuery("SELECT count(*) FROM user_tables WHERE table_name = 'EVALUADORES'")) {
                if (rs.next() && rs.getInt(1) > 0) {
                    tablasExisten = true;
                }
            }

            if (!tablasExisten) {
                System.out.println("La tabla EVALUADORES no existe. Creando tablas...");

                stmt.execute("CREATE TABLE evaluadores (" +
                        "id NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, " +
                        "nombres VARCHAR2(100) NOT NULL, " +
                        "apellido_paterno VARCHAR2(100) NOT NULL, " +
                        "apellido_materno VARCHAR2(100), " +
                        "correo VARCHAR2(150) UNIQUE NOT NULL, " +
                        "contrasena_hash VARCHAR2(256) NOT NULL, " +
                        "salt VARCHAR2(100) NOT NULL, " +
                        "rol VARCHAR2(50) DEFAULT 'Evaluador UX', " +
                        "codigo_recuperacion VARCHAR2(50)" +
                        ")");

                stmt.execute("CREATE TABLE pruebas (" +
                        "id NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, " +
                        "id_evaluador NUMBER NOT NULL, " +
                        "nombre VARCHAR2(150) NOT NULL, " +
                        "plataforma VARCHAR2(100), " +
                        "url VARCHAR2(255), " +
                        "tarea VARCHAR2(4000), " +
                        "fecha_creacion VARCHAR2(50), " +
                        "CONSTRAINT fk_pruebas_evaluador FOREIGN KEY (id_evaluador) REFERENCES evaluadores(id) ON DELETE CASCADE"
                        +
                        ")");

                stmt.execute("CREATE TABLE preguntas (" +
                        "id NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, " +
                        "id_prueba NUMBER NOT NULL, " +
                        "texto VARCHAR2(500) NOT NULL, " +
                        "orden NUMBER NOT NULL, " +
                        "tipo VARCHAR2(50) NOT NULL, " +
                        "CONSTRAINT fk_preguntas_prueba FOREIGN KEY (id_prueba) REFERENCES pruebas(id) ON DELETE CASCADE"
                        +
                        ")");

                stmt.execute("CREATE TABLE participantes (" +
                        "id NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, " +
                        "id_prueba NUMBER NOT NULL, " +
                        "nombre VARCHAR2(150) NOT NULL, " +
                        "edad NUMBER NOT NULL, " +
                        "sexo VARCHAR2(20) NOT NULL, " +
                        "fecha_realizacion VARCHAR2(50), " +
                        "duracion_segundos NUMBER, " +
                        "audio_path VARCHAR2(255), " +
                        "CONSTRAINT fk_participantes_prueba FOREIGN KEY (id_prueba) REFERENCES pruebas(id) ON DELETE CASCADE"
                        +
                        ")");

                stmt.execute("CREATE TABLE respuestas (" +
                        "id NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, " +
                        "id_participante NUMBER NOT NULL, " +
                        "id_pregunta NUMBER NOT NULL, " +
                        "valor NUMBER NOT NULL, " +
                        "CONSTRAINT fk_respuestas_participante FOREIGN KEY (id_participante) REFERENCES participantes(id) ON DELETE CASCADE, "
                        +
                        "CONSTRAINT fk_respuestas_pregunta FOREIGN KEY (id_pregunta) REFERENCES preguntas(id) ON DELETE CASCADE"
                        +
                        ")");

                System.out.println("Tablas de base de datos creadas con éxito.");
            } else {
                System.out.println("Las tablas ya existen en la base de datos.");
            }

        } catch (SQLException e) {
            System.err.println("Error al inicializar las tablas de la base de datos");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try (Connection con = getConnection()) {
            if (con.isValid(5)) {
                System.out.println("Conexión de prueba exitosa a Oracle Cloud.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
