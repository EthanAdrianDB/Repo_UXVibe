package mx.edu.utez.uxvibe.model.dao;

import mx.edu.utez.uxvibe.model.Participante;
import mx.edu.utez.uxvibe.utils.SQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParticipanteDao implements Dao<Participante, Integer> {

    @Override
    public boolean create(Participante entidad) {
        String sql = "INSERT INTO Participante (nombre, apellido_m, apellido_p, sexo, id_prueba) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getApellidoM());
            ps.setString(3, entidad.getApellidoP());
            ps.setInt(4, entidad.getSexo());
            ps.setInt(5, entidad.getIdPrueba());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        entidad.setIdParticipante(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Participante> getAll() {
        List<Participante> lista = new ArrayList<>();
        String sql = "SELECT * FROM Participante ORDER BY id_participante DESC";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(aParticipante(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Participante> getPorPrueba(int idPrueba) {
        List<Participante> lista = new ArrayList<>();
        String sql = "SELECT * FROM Participante WHERE id_prueba = ? ORDER BY id_participante DESC";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPrueba);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(aParticipante(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public int contarPorPrueba(int idPrueba) {
        String sql = "SELECT COUNT(*) FROM Participante WHERE id_prueba = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPrueba);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Participante getById(Integer id) {
        String sql = "SELECT * FROM Participante WHERE id_participante = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return aParticipante(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Participante entidad) {
        String sql = "UPDATE Participante SET nombre = ?, apellido_m = ?, apellido_p = ?, sexo = ?, id_prueba = ? WHERE id_participante = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getApellidoM());
            ps.setString(3, entidad.getApellidoP());
            ps.setInt(4, entidad.getSexo());
            ps.setInt(5, entidad.getIdPrueba());
            ps.setInt(6, entidad.getIdParticipante());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM Participante WHERE id_participante = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void eliminarPorPrueba(int idPrueba) {
        String sql = "DELETE FROM Participante WHERE id_prueba = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPrueba);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Estadísticas
    public Map<String, Double> distribucionPorSexo(int idPrueba) {
        Map<String, Double> resultado = new LinkedHashMap<>();
        int total = contarPorPrueba(idPrueba);
        if (total == 0) return resultado;

        String sql = "SELECT sexo, COUNT(*) as conteo FROM Participante WHERE id_prueba = ? GROUP BY sexo";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPrueba);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int sexo = rs.getInt("sexo");
                    int conteo = rs.getInt("conteo");
                    double pct = Math.round(((conteo * 100.0) / total) * 10.0) / 10.0;
                    String label = (sexo == 1) ? "Masculino" : "Femenino";
                    resultado.put(label, pct);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    // Eliminado edadPromedio porque ya no hay campo de edad en la BD

    private Participante aParticipante(ResultSet rs) throws SQLException {
        return new Participante(
                rs.getInt("id_participante"),
                rs.getString("nombre"),
                rs.getString("apellido_m"),
                rs.getString("apellido_p"),
                rs.getInt("sexo"),
                rs.getInt("id_prueba")
        );
    }
}
