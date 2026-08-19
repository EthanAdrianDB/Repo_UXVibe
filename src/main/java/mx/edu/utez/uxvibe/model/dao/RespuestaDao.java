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
        
        try (Connection con = SQLConnector.getConnection()) {
            PreparedStatement ps;
            try {
                ps = con.prepareStatement(sql, new String[]{"ID_RESPUESTAS"});
            } catch (SQLException e) {
                ps = con.prepareStatement(sql, new String[]{"ID_RESPUESTA"});
            }
            try (PreparedStatement statement = ps) {
                statement.setInt(1, entidad.getIdParticipante());
                statement.setInt(2, entidad.getIdPrueba());
                statement.setInt(3, entidad.getSam1());
                statement.setInt(4, entidad.getSam2());
                statement.setInt(5, entidad.getSam3());
                statement.setInt(6, entidad.getR1());
                statement.setInt(7, entidad.getR2());
                statement.setInt(8, entidad.getR3());
                statement.setInt(9, entidad.getR4());
                statement.setInt(10, entidad.getR5());
                statement.setInt(11, entidad.getR6());
                statement.setInt(12, entidad.getR7());
                statement.setInt(13, entidad.getR8());
                statement.setInt(14, entidad.getR9());
                statement.setInt(15, entidad.getR10());
                statement.setInt(16, entidad.getR11());
                statement.setInt(17, entidad.getR12());
                statement.setInt(18, entidad.getR13());
                statement.setInt(19, entidad.getR14());
                statement.setInt(20, entidad.getR15());
                statement.setInt(21, entidad.getFrecuenciaEstadoAnimo1());
                statement.setInt(22, entidad.getFrecuenciaEstadoAnimo2());

                int rows = statement.executeUpdate();
                if (rows > 0) {
                    try (ResultSet rs = statement.getGeneratedKeys()) {
                        if (rs != null && rs.next()) {
                            try {
                                entidad.setIdRespuestas(rs.getInt(1));
                            } catch (Exception ignored) {}
                        }
                    } catch (Exception ignored) {}
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("[RespuestaDao] Error al insertar respuesta: " + e.getMessage());
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
    
    public Map<String, Double> promedioPorPregunta(int idPrueba) {
        Map<String, Double> promedios = new LinkedHashMap<>();
        String sql = "SELECT AVG(sam_1) as s1, AVG(sam_2) as s2, AVG(sam_3) as s3, " +
                     "AVG(r1) as r1, AVG(r2) as r2, AVG(r3) as r3, AVG(r4) as r4, AVG(r5) as r5, " +
                     "AVG(r6) as r6, AVG(r7) as r7, AVG(r8) as r8, AVG(r9) as r9, AVG(r10) as r10, " +
                     "AVG(r11) as r11, AVG(r12) as r12, AVG(r13) as r13, AVG(r14) as r14, AVG(r15) as r15 " +
                     "FROM Respuesta WHERE id_prueba = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPrueba);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double s1 = rs.getDouble("s1");
                    if (!rs.wasNull()) { // Solo agregar si hay datos
                        promedios.put("SAM_1", Math.round(s1 * 10.0) / 10.0);
                        promedios.put("SAM_2", Math.round(rs.getDouble("s2") * 10.0) / 10.0);
                        promedios.put("SAM_3", Math.round(rs.getDouble("s3") * 10.0) / 10.0);
                        
                        for (int i = 1; i <= 15; i++) {
                            promedios.put("Pregunta " + i, Math.round(rs.getDouble("r" + i) * 10.0) / 10.0);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promedios;
    }

    private Respuesta aRespuesta(ResultSet rs) throws SQLException {
        Respuesta res = new Respuesta();
        try {
            res.setIdRespuestas(rs.getInt("id_respuesta"));
        } catch (SQLException e) {
            try {
                res.setIdRespuestas(rs.getInt("id_respuestas"));
            } catch (SQLException ignored) {}
        }
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
