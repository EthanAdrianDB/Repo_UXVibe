package mx.edu.utez.uxvibe.model.dao;

import mx.edu.utez.uxvibe.model.Respuesta;
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

public class RespuestaDao implements Dao<Respuesta, Integer> {

    @Override
    public boolean create(Respuesta entidad) {
        String sql = "INSERT INTO Respuesta (id_participante, id_prueba, " +
                     "sam_1, sam_2, sam_3, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, " +
                     "frecuencia_estado_animo_1, frecuencia_estado_animo_2) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, entidad.getIdParticipante());
            ps.setInt(2, entidad.getIdPrueba());
            ps.setInt(3, entidad.getSam1());
            ps.setInt(4, entidad.getSam2());
            ps.setInt(5, entidad.getSam3());
            ps.setInt(6, entidad.getR1());
            ps.setInt(7, entidad.getR2());
            ps.setInt(8, entidad.getR3());
            ps.setInt(9, entidad.getR4());
            ps.setInt(10, entidad.getR5());
            ps.setInt(11, entidad.getR6());
            ps.setInt(12, entidad.getR7());
            ps.setInt(13, entidad.getR8());
            ps.setInt(14, entidad.getR9());
            ps.setInt(15, entidad.getR10());
            ps.setInt(16, entidad.getR11());
            ps.setInt(17, entidad.getR12());
            ps.setInt(18, entidad.getR13());
            ps.setInt(19, entidad.getR14());
            ps.setInt(20, entidad.getR15());
            ps.setInt(21, entidad.getFrecuenciaEstadoAnimo1());
            ps.setInt(22, entidad.getFrecuenciaEstadoAnimo2());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        entidad.setIdRespuestas(rs.getInt(1));
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
    public List<Respuesta> getAll() {
        List<Respuesta> lista = new ArrayList<>();
        String sql = "SELECT * FROM Respuesta";
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

    public List<Respuesta> getPorParticipante(int idParticipante) {
        List<Respuesta> lista = new ArrayList<>();
        String sql = "SELECT * FROM Respuesta WHERE id_participante = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idParticipante);
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
    public Respuesta getById(Integer id) {
        String sql = "SELECT * FROM Respuesta WHERE id_respuestas = ?";
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
    public boolean update(Respuesta entidad) {
        return false; // Generalmente las respuestas no se actualizan
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM Respuesta WHERE id_respuestas = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Métodos de estadísticas requerirán ser reescritos en la fase 2 dependiendo de los requerimientos
    public Map<String, Double> promedioPorPregunta(int idPrueba) {
        return new LinkedHashMap<>();
    }

    private Respuesta aRespuesta(ResultSet rs) throws SQLException {
        Respuesta res = new Respuesta();
        res.setIdRespuestas(rs.getInt("id_respuestas"));
        res.setIdParticipante(rs.getInt("id_participante"));
        res.setIdPrueba(rs.getInt("id_prueba"));
        res.setSam1(rs.getInt("sam_1"));
        res.setSam2(rs.getInt("sam_2"));
        res.setSam3(rs.getInt("sam_3"));
        res.setR1(rs.getInt("r1"));
        res.setR2(rs.getInt("r2"));
        res.setR3(rs.getInt("r3"));
        res.setR4(rs.getInt("r4"));
        res.setR5(rs.getInt("r5"));
        res.setR6(rs.getInt("r6"));
        res.setR7(rs.getInt("r7"));
        res.setR8(rs.getInt("r8"));
        res.setR9(rs.getInt("r9"));
        res.setR10(rs.getInt("r10"));
        res.setR11(rs.getInt("r11"));
        res.setR12(rs.getInt("r12"));
        res.setR13(rs.getInt("r13"));
        res.setR14(rs.getInt("r14"));
        res.setR15(rs.getInt("r15"));
        res.setFrecuenciaEstadoAnimo1(rs.getInt("frecuencia_estado_animo_1"));
        res.setFrecuenciaEstadoAnimo2(rs.getInt("frecuencia_estado_animo_2"));
        return res;
    }
}
