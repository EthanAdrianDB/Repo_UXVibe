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
        String sql = "INSERT INTO Participante (nombre, apellido_m, apellido_p, sexo, id_prueba, edad, fecha_realizacion) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"ID_PARTICIPANTE"})) {

            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getApellidoM());
            ps.setString(3, entidad.getApellidoP());
            ps.setInt(4, entidad.getSexo());
            ps.setInt(5, entidad.getIdPrueba());
            ps.setInt(6, entidad.getEdad());
            // Guardar la fecha actual como fecha de realización
            String fechaActual = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
            ps.setString(7, entidad.getFechaRealizacion() != null ? entidad.getFechaRealizacion() : fechaActual);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs != null && rs.next()) {
                        try {
                            entidad.setIdParticipante(rs.getInt(1));
                        } catch (Exception ignored) {}
                    }
                } catch (Exception ignored) {}
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[ParticipanteDao] Error al insertar participante: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Participante> getAll() {
        List<Participante> lista = new ArrayList<>();
        String sql = "SELECT * FROM Participante ORDER BY id_participante ASC";
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
        String sql = "SELECT * FROM Participante WHERE id_prueba = ? ORDER BY id_participante ASC";
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
        String sql = "UPDATE Participante SET nombre = ?, apellido_m = ?, apellido_p = ?, sexo = ?, id_prueba = ?, edad = ?, fecha_realizacion = ? WHERE id_participante = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getApellidoM());
            ps.setString(3, entidad.getApellidoP());
            ps.setInt(4, entidad.getSexo());
            ps.setInt(5, entidad.getIdPrueba());
            ps.setInt(6, entidad.getEdad());
            ps.setString(7, entidad.getFechaRealizacion());
            ps.setInt(8, entidad.getIdParticipante());
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

    public Map<String, Double> distribucionPorSexoEvaluador(int idEvaluador) {
        Map<String, Double> resultado = new LinkedHashMap<>();
        resultado.put("Femenino", 0.0);
        resultado.put("Masculino", 0.0);

        String sql = "SELECT pa.sexo, COUNT(*) as conteo FROM Participante pa " +
                     "JOIN Prueba pr ON pa.id_prueba = pr.id_prueba " +
                     "WHERE pr.id_evaluador = ? GROUP BY pa.sexo";

        int total = 0;
        int countFemenino = 0;
        int countMasculino = 0;

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEvaluador);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int sexo = rs.getInt("sexo");
                    int conteo = rs.getInt("conteo");
                    total += conteo;
                    if (sexo == 1) {
                        countMasculino += conteo;
                    } else {
                        countFemenino += conteo;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (total > 0) {
            double pctFemenino = Math.round(((countFemenino * 100.0) / total) * 10.0) / 10.0;
            double pctMasculino = Math.round(((countMasculino * 100.0) / total) * 10.0) / 10.0;
            resultado.put("Femenino", pctFemenino);
            resultado.put("Masculino", pctMasculino);
        }

        return resultado;
    }
    
    public double edadPromedio(int idPrueba) {
        String sql = "SELECT AVG(edad) FROM Participante WHERE id_prueba = ? AND edad > 0";
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

    private Participante aParticipante(ResultSet rs) throws SQLException {
        Participante p = new Participante(
                rs.getInt("id_participante"),
                rs.getString("nombre"),
                rs.getString("apellido_m"),
                rs.getString("apellido_p"),
                rs.getInt("sexo"),
                rs.getInt("id_prueba")
        );
        // Leer campos opcionales que pueden no existir aún en la BD
        try { p.setEdad(rs.getInt("edad")); } catch (SQLException ignored) {}
        try { p.setFechaRealizacion(rs.getString("fecha_realizacion")); } catch (SQLException ignored) {}
        return p;
    }
}
