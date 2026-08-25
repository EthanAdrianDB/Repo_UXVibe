package mx.edu.utez.uxvibe.model.dao;

import mx.edu.utez.uxvibe.model.Evaluador;
import mx.edu.utez.uxvibe.utils.SQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para la tabla Evaluador.
 * Aquí manejamos todo lo que tenga que ver con la base de datos para los evaluadores:
 * registrarse, iniciar sesión, actualizar datos y gestionar tokens para recuperar contraseñas.
 */
public class EvaluadorDao implements Dao<Evaluador, Integer> {

    // Variable para guardar el último error de SQL por si ocupamos mostrarlo o debuguear
    private static String ultimoError = null;

    public static String getUltimoError() {
        return ultimoError;
    }

    /**
     * Inserta un nuevo evaluador en la base de datos Oracle.
     * Usamos PreparedStatement para evitar inyecciones SQL y try-with-resources
     * para que la conexión se cierre sola pase lo que pase.
     */
    @Override
    public boolean create(Evaluador entidad) {
        ultimoError = null;
        String sql = "INSERT INTO Evaluador (nombre, apellido_m, apellido_p, correo, contrasena) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"ID_EVALUADOR"})) {

            // Pasamos los parámetros en orden según los '?' de la consulta
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getApellidoM());
            ps.setString(3, entidad.getApellidoP());
            ps.setString(4, entidad.getCorreo());
            ps.setString(5, entidad.getContrasena()); // Debe venir ya con hash SHA-256

            int rows = ps.executeUpdate();
            if (rows > 0) {
                // Obtenemos el ID autogenerado en Oracle para asignárselo al objeto
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs != null && rs.next()) {
                        try {
                            entidad.setIdEvaluador(rs.getInt(1));
                        } catch (Exception ignored) {}
                    }
                } catch (Exception ignored) {}
                return true;
            }
        } catch (SQLException e) {
            ultimoError = e.getMessage();
            System.err.println("[EvaluadorDao] Error al insertar evaluador: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Trae todos los evaluadores registrados en la base de datos.
     * Recorremos el ResultSet y convertimos cada fila en un objeto Evaluador.
     */
    @Override
    public List<Evaluador> getAll() {
        List<Evaluador> lista = new ArrayList<>();
        String sql = "SELECT * FROM Evaluador";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(aEvaluador(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Busca a un evaluador por su ID único.
     * Regresa el objeto Evaluador si lo encuentra, o null si no existe.
     */
    @Override
    public Evaluador getById(Integer id) {
        String sql = "SELECT * FROM Evaluador WHERE id_evaluador = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return aEvaluador(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Actualiza la información de un evaluador existente (nombre, apellidos, correo, pass).
     */
    @Override
    public boolean update(Evaluador entidad) {
        String sql = "UPDATE Evaluador SET nombre = ?, apellido_m = ?, apellido_p = ?, correo = ?, contrasena = ? WHERE id_evaluador = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getApellidoM());
            ps.setString(3, entidad.getApellidoP());
            ps.setString(4, entidad.getCorreo());
            ps.setString(5, entidad.getContrasena());
            ps.setInt(6, entidad.getIdEvaluador());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Elimina un evaluador de la base de datos mediante su ID.
     */
    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM Evaluador WHERE id_evaluador = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Busca un evaluador por su correo electrónico.
     * Se usa principalmente para el Login y para el registro (para no repetir cuentas).
     */
    public Evaluador buscarPorCorreo(String correo) {
        String sql = "SELECT * FROM Evaluador WHERE correo = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return aEvaluador(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Estructura interna para guardar los tokens temporales de recuperación de contraseña en memoria
     * con su tiempo de expiración en milisegundos.
     */
    public static class TokenInfo {
        private final int idEvaluador;
        private final long expirationTime;

        public TokenInfo(int idEvaluador, long expirationTime) {
            this.idEvaluador = idEvaluador;
            this.expirationTime = expirationTime;
        }

        public int getIdEvaluador() {
            return idEvaluador;
        }

        // Checa si ya pasaron los minutos de validez
        public boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }
    }

    // Mapa en memoria concurrente para almacenar los códigos/tokens activos sin bloquear peticiones simultáneas
    private static final java.util.concurrent.ConcurrentHashMap<String, TokenInfo> tokensRecuperacion = new java.util.concurrent.ConcurrentHashMap<>();

    /**
     * Guarda un código temporal vinculado al ID del evaluador con un tiempo límite de vigencia.
     */
    public void guardarTokenRecuperacion(String token, int idEvaluador, int minutosValidez) {
        long exp = System.currentTimeMillis() + (minutosValidez * 60L * 1000L);
        tokensRecuperacion.put(token, new TokenInfo(idEvaluador, exp));
    }

    /**
     * Valida si un código de recuperación existe y todavía no expira.
     * Si es válido, regresa el Evaluador correspondiente.
     */
    public Evaluador buscarPorToken(String token) {
        if (token == null || token.trim().isEmpty()) return null;
        TokenInfo info = tokensRecuperacion.get(token);
        if (info == null) return null;
        if (info.isExpired()) {
            tokensRecuperacion.remove(token); // Si ya venció, lo limpiamos
            return null;
        }
        return getById(info.getIdEvaluador());
    }

    /**
     * Elimina el código una vez que el usuario ya cambió su contraseña exitosamente.
     */
    public void eliminarToken(String token) {
        if (token != null) {
            tokensRecuperacion.remove(token);
        }
    }

    /**
     * Actualiza únicamente la contraseña hasheada del evaluador (para cuando la recupera o edita perfil).
     */
    public boolean actualizarContrasena(int idEvaluador, String nuevoHash) {
        String sql = "UPDATE Evaluador SET contrasena = ? WHERE id_evaluador = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoHash);
            ps.setInt(2, idEvaluador);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Método auxiliar (helper) para mapear las columnas del ResultSet de SQL a un objeto Evaluador de Java.
     */
    private Evaluador aEvaluador(ResultSet rs) throws SQLException {
        return new Evaluador(
                rs.getInt("id_evaluador"),
                rs.getString("nombre"),
                rs.getString("apellido_m"),
                rs.getString("apellido_p"),
                rs.getString("correo"),
                rs.getString("contrasena")
        );
    }
}

