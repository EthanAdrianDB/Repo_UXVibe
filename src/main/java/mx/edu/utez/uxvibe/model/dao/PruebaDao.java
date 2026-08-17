package mx.edu.utez.uxvibe.model.dao;

import mx.edu.utez.uxvibe.model.Prueba;
import mx.edu.utez.uxvibe.utils.SQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PruebaDao implements Dao<Prueba, Integer> {

    @Override
    public boolean create(Prueba entidad) {
        String sql = "INSERT INTO Prueba (nombre, descripcion, url_sistema, id_evaluador) VALUES (?, ?, ?, ?)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"ID_PRUEBA"})) {

            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getDescripcion());
            ps.setString(3, entidad.getUrlSistema());
            ps.setInt(4, entidad.getIdEvaluador());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs != null && rs.next()) {
                        try {
                            entidad.setIdPrueba(rs.getInt(1));
                        } catch (Exception ignored) {}
                    }
                } catch (Exception ignored) {}
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[PruebaDao] Error al insertar prueba: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Prueba> getAll() {
        List<Prueba> lista = new ArrayList<>();
        String sql = "SELECT p.*, (SELECT COUNT(*) FROM Participante pt WHERE pt.id_prueba = p.id_prueba) as total_participantes FROM Prueba p";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Prueba p = aPrueba(rs);
                p.setTotalParticipantes(rs.getInt("total_participantes"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Prueba> getPorEvaluador(int idEvaluador) {
        List<Prueba> lista = new ArrayList<>();
        String sql = "SELECT p.*, (SELECT COUNT(*) FROM Participante pt WHERE pt.id_prueba = p.id_prueba) as total_participantes FROM Prueba p WHERE p.id_evaluador = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEvaluador);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Prueba p = aPrueba(rs);
                    p.setTotalParticipantes(rs.getInt("total_participantes"));
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Prueba getById(Integer id) {
        String sql = "SELECT p.*, (SELECT COUNT(*) FROM Participante pt WHERE pt.id_prueba = p.id_prueba) as total_participantes FROM Prueba p WHERE p.id_prueba = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Prueba p = aPrueba(rs);
                    p.setTotalParticipantes(rs.getInt("total_participantes"));
                    return p;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Prueba entidad) {
        String sql = "UPDATE Prueba SET nombre = ?, descripcion = ?, url_sistema = ? WHERE id_prueba = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getDescripcion());
            ps.setString(3, entidad.getUrlSistema());
            ps.setInt(4, entidad.getIdPrueba());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM Prueba WHERE id_prueba = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Helper temporal para no romper funcionalidad enlazada
    public Prueba getByEnlace(String enlaceUnico) {
        try {
            int id = Integer.parseInt(enlaceUnico);
            return getById(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Prueba aPrueba(ResultSet rs) throws SQLException {
        return new Prueba(
                rs.getInt("id_prueba"),
                rs.getInt("id_evaluador"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getString("url_sistema")
        );
    }
}
