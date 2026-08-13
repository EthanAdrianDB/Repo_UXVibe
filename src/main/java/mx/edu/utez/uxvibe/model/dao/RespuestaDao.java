package mx.edu.utez.uxvibe.model.dao;

import mx.edu.utez.uxvibe.model.RespuestaCuestionario;
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

public class RespuestaDao implements Dao<RespuestaCuestionario, Integer> {

    @Override
    public boolean create(RespuestaCuestionario entidad) {
        String sqlMax = "SELECT NVL(MAX(id_respuesta), 0) + 1 FROM respuesta_cuestionario";
        String sql = "INSERT INTO respuesta_cuestionario (id_respuesta, id_sesion, escala_satisfaccion, comentarios_libres) VALUES (?, ?, ?, ?)";
        
        try (Connection con = SQLConnector.getConnection()) {
            int newId = 1;
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlMax)) {
                if (rs.next()) {
                    newId = rs.getInt(1);
                }
            }
            
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, newId);
                ps.setInt(2, entidad.getIdSesion());
                ps.setInt(3, entidad.getValor());
                ps.setString(4, entidad.getComentariosLibres() != null ? entidad.getComentariosLibres() : "");

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    entidad.setIdRespuesta(newId);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<RespuestaCuestionario> getAll() {
        List<RespuestaCuestionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM respuesta_cuestionario";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(aRespuesta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<RespuestaCuestionario> getPorParticipante(int idSesion) {
        List<RespuestaCuestionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM respuesta_cuestionario WHERE id_sesion = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idSesion);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(aRespuesta(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public RespuestaCuestionario getById(Integer id) {
        String sql = "SELECT * FROM respuesta_cuestionario WHERE id_respuesta = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return aRespuesta(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(RespuestaCuestionario entidad) {
        String sql = "UPDATE respuesta_cuestionario SET id_sesion = ?, escala_satisfaccion = ?, comentarios_libres = ? WHERE id_respuesta = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entidad.getIdSesion());
            ps.setInt(2, entidad.getValor());
            ps.setString(3, entidad.getComentariosLibres());
            ps.setInt(4, entidad.getIdRespuesta());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM respuesta_cuestionario WHERE id_respuesta = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Map<String, Double> promedioPorPregunta(int idPrueba) {
        Map<String, Double> resultado = new LinkedHashMap<>();
        resultado.put("Satisfacción General", satisfaccionPromedio(idPrueba));
        return resultado;
    }

    public double satisfaccionPromedio(int idPrueba) {
        String sql = "SELECT AVG(r.escala_satisfaccion) " +
                     "FROM respuesta_cuestionario r " +
                     "JOIN sesion_evaluacion s ON r.id_sesion = s.id_sesion " +
                     "WHERE s.id_prueba = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPrueba);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Math.round(rs.getDouble(1) * 10.0) / 10.0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public Map<String, Double> promedioSam(int idPrueba) {
        // SAM no mapeado en base de datos física, se retorna vacío para evitar errores
        return new LinkedHashMap<>();
    }

    public Map<String, Integer> respuestasLikertDeParticipante(int idColaborador, int idPrueba) {
        Map<String, Integer> resultado = new LinkedHashMap<>();
        String sql = "SELECT r.escala_satisfaccion " +
                     "FROM respuesta_cuestionario r " +
                     "JOIN sesion_evaluacion s ON r.id_sesion = s.id_sesion " +
                     "WHERE s.id_colaborador = ? AND s.id_prueba = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idColaborador);
            ps.setInt(2, idPrueba);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    resultado.put("Satisfacción General", rs.getInt("escala_satisfaccion"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public String comentariosDeParticipante(int idColaborador, int idPrueba) {
        String sql = "SELECT r.comentarios_libres " +
                     "FROM respuesta_cuestionario r " +
                     "JOIN sesion_evaluacion s ON r.id_sesion = s.id_sesion " +
                     "WHERE s.id_colaborador = ? AND s.id_prueba = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idColaborador);
            ps.setInt(2, idPrueba);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("comentarios_libres");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    // ---- helper de conversión ResultSet -> objeto ----

    private RespuestaCuestionario aRespuesta(ResultSet rs) throws SQLException {
        return new RespuestaCuestionario(
                rs.getInt("id_respuesta"),
                rs.getInt("id_sesion"),
                rs.getInt("escala_satisfaccion"),
                rs.getString("comentarios_libres")
        );
    }
}
