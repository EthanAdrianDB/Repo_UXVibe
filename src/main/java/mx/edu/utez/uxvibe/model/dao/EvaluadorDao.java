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

public class EvaluadorDao implements Dao<Evaluador, Integer> {

    @Override
    public boolean create(Evaluador entidad) {
        String sql = "INSERT INTO Evaluador (nombre, apellido_m, apellido_p, correo, contrasena) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"ID_EVALUADOR"})) {

            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getApellidoM());
            ps.setString(3, entidad.getApellidoP());
            ps.setString(4, entidad.getCorreo());
            ps.setString(5, entidad.getContrasena());

            int rows = ps.executeUpdate();
            if (rows > 0) {
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
            System.err.println("[EvaluadorDao] Error al insertar evaluador: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

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
    
    // Método temporal para no romper código existente que esperaba Token/Salt/Password
    public boolean actualizarContrasena(int idEvaluador, String nuevoHash, String nuevoSalt) {
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
    
    // Método temporal para no romper buscarPorToken (puedes adaptarlo según cómo manejes recuperación ahora)
    public Evaluador buscarPorToken(String token) {
        return null; // A definir la lógica de recuperación de contraseña en el nuevo esquema
    }

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
