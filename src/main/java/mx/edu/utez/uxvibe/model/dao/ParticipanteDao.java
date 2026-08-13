package mx.edu.utez.uxvibe.model.dao;

import mx.edu.utez.uxvibe.model.Colaborador;
import mx.edu.utez.uxvibe.utils.SQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParticipanteDao implements Dao<Colaborador, Integer> {

    private static boolean columnaNombreVerificada = false;

    private synchronized void asegurarColumnaNombre(Connection con) {
        if (columnaNombreVerificada) return;
        try (Statement st = con.createStatement()) {
            st.execute("ALTER TABLE colaborador ADD (nombre VARCHAR2(150))");
        } catch (SQLException ignored) {
            // Ignorar si la columna ya existe
        }
        columnaNombreVerificada = true;
    }

    @Override
    public boolean create(Colaborador entidad) {
        String sqlMaxCol = "SELECT NVL(MAX(id_colaborador), 0) + 1 FROM colaborador";
        String sqlMaxSes = "SELECT NVL(MAX(id_sesion), 0) + 1 FROM sesion_evaluacion";

        String sqlColaboradorWithNombre = "INSERT INTO colaborador (id_colaborador, rango_edad, genero, aviso_consentimiento, nombre) VALUES (?, ?, ?, ?, ?)";
        String sqlColaboradorWithId = "INSERT INTO colaborador (id_colaborador, rango_edad, genero, aviso_consentimiento) VALUES (?, ?, ?, ?)";
        
        String sqlSesionWithId = "INSERT INTO sesion_evaluacion (id_sesion, id_prueba, id_colaborador, fecha_hora_ejecucion) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

        String sqlColaboradorIdentityWithNombre = "INSERT INTO colaborador (rango_edad, genero, aviso_consentimiento, nombre) VALUES (?, ?, ?, ?)";
        String sqlColaboradorIdentity = "INSERT INTO colaborador (rango_edad, genero, aviso_consentimiento) VALUES (?, ?, ?)";
        
        String sqlSesionIdentity = "INSERT INTO sesion_evaluacion (id_prueba, id_colaborador, fecha_hora_ejecucion) VALUES (?, ?, CURRENT_TIMESTAMP)";

        String rangoEdad = (entidad.getRangoEdad() != null && !entidad.getRangoEdad().trim().isEmpty()) ? entidad.getRangoEdad().trim() : "18-25";
        String genero = (entidad.getGenero() != null && !entidad.getGenero().trim().isEmpty()) ? entidad.getGenero().trim() : "Otro";
        String nombre = (entidad.getNombre() != null && !entidad.getNombre().trim().isEmpty() && !entidad.getNombre().startsWith("Colaborador #") && !entidad.getNombre().startsWith("Participante #"))
                ? entidad.getNombre().trim() : null;

        try (Connection con = SQLConnector.getConnection()) {
            asegurarColumnaNombre(con);
            con.setAutoCommit(false);

            // Strategy A: NVL(MAX + 1)
            int newColId = 1;
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlMaxCol)) {
                if (rs.next()) newColId = rs.getInt(1);
            }
            int newSesId = 1;
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlMaxSes)) {
                if (rs.next()) newSesId = rs.getInt(1);
            }

            boolean colInserted = false;
            if (nombre != null) {
                try (PreparedStatement psCol = con.prepareStatement(sqlColaboradorWithNombre)) {
                    psCol.setInt(1, newColId);
                    psCol.setString(2, rangoEdad);
                    psCol.setString(3, genero);
                    psCol.setInt(4, entidad.getAvisoConsentimiento());
                    psCol.setString(5, nombre);
                    psCol.executeUpdate();
                    colInserted = true;
                } catch (SQLException ignored) {}
            }

            if (!colInserted) {
                try (PreparedStatement psCol = con.prepareStatement(sqlColaboradorWithId)) {
                    psCol.setInt(1, newColId);
                    psCol.setString(2, rangoEdad);
                    psCol.setString(3, genero);
                    psCol.setInt(4, entidad.getAvisoConsentimiento());
                    psCol.executeUpdate();
                }
            }

            try (PreparedStatement psSes = con.prepareStatement(sqlSesionWithId)) {
                psSes.setInt(1, newSesId);
                psSes.setInt(2, entidad.getIdPrueba());
                psSes.setInt(3, newColId);
                psSes.executeUpdate();

                con.commit();
                entidad.setIdColaborador(newColId);
                entidad.setIdSesion(newSesId);
                return true;
            } catch (SQLException ex1) {
                con.rollback();
                // Strategy B: Identity Columns
                con.setAutoCommit(false);
                boolean identityInserted = false;
                if (nombre != null) {
                    try (PreparedStatement psCol = con.prepareStatement(sqlColaboradorIdentityWithNombre, Statement.RETURN_GENERATED_KEYS)) {
                        psCol.setString(1, rangoEdad);
                        psCol.setString(2, genero);
                        psCol.setInt(3, entidad.getAvisoConsentimiento());
                        psCol.setString(4, nombre);
                        int rows = psCol.executeUpdate();
                        if (rows > 0) {
                            try (ResultSet rsCol = psCol.getGeneratedKeys()) {
                                if (rsCol.next()) entidad.setIdColaborador(rsCol.getInt(1));
                            }
                            identityInserted = true;
                        }
                    } catch (SQLException ignored) {}
                }

                if (!identityInserted) {
                    try (PreparedStatement psCol = con.prepareStatement(sqlColaboradorIdentity, Statement.RETURN_GENERATED_KEYS)) {
                        psCol.setString(1, rangoEdad);
                        psCol.setString(2, genero);
                        psCol.setInt(3, entidad.getAvisoConsentimiento());
                        int rows = psCol.executeUpdate();
                        if (rows > 0) {
                            try (ResultSet rsCol = psCol.getGeneratedKeys()) {
                                if (rsCol.next()) entidad.setIdColaborador(rsCol.getInt(1));
                            }
                        }
                    }
                }

                try (PreparedStatement psSes = con.prepareStatement(sqlSesionIdentity, Statement.RETURN_GENERATED_KEYS)) {
                    psSes.setInt(1, entidad.getIdPrueba());
                    psSes.setInt(2, entidad.getIdColaborador());
                    psSes.executeUpdate();
                    try (ResultSet rsSes = psSes.getGeneratedKeys()) {
                        if (rsSes.next()) entidad.setIdSesion(rsSes.getInt(1));
                    }
                }
                con.commit();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Colaborador> getAll() {
        List<Colaborador> lista = new ArrayList<>();
        String sql = "SELECT c.*, s.id_prueba, s.id_sesion, s.fecha_hora_ejecucion FROM colaborador c JOIN sesion_evaluacion s ON c.id_colaborador = s.id_colaborador ORDER BY c.id_colaborador DESC";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(aColaborador(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Colaborador> getPorPrueba(int idPrueba) {
        List<Colaborador> lista = new ArrayList<>();
        String sql = "SELECT c.*, s.id_prueba, s.id_sesion, s.fecha_hora_ejecucion FROM colaborador c JOIN sesion_evaluacion s ON c.id_colaborador = s.id_colaborador WHERE s.id_prueba = ? ORDER BY c.id_colaborador DESC";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPrueba);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(aColaborador(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public int contarPorPrueba(int idPrueba) {
        String sql = "SELECT COUNT(*) FROM sesion_evaluacion WHERE id_prueba = ?";
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
    public Colaborador getById(Integer id) {
        String sql = "SELECT c.*, s.id_prueba, s.id_sesion, s.fecha_hora_ejecucion FROM colaborador c JOIN sesion_evaluacion s ON c.id_colaborador = s.id_colaborador WHERE c.id_colaborador = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return aColaborador(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Colaborador entidad) {
        String sqlWithNombre = "UPDATE colaborador SET rango_edad = ?, genero = ?, aviso_consentimiento = ?, nombre = ? WHERE id_colaborador = ?";
        String sql = "UPDATE colaborador SET rango_edad = ?, genero = ?, aviso_consentimiento = ? WHERE id_colaborador = ?";
        try (Connection con = SQLConnector.getConnection()) {
            asegurarColumnaNombre(con);
            if (entidad.getNombre() != null && !entidad.getNombre().startsWith("Colaborador #")) {
                try (PreparedStatement ps = con.prepareStatement(sqlWithNombre)) {
                    ps.setString(1, entidad.getRangoEdad());
                    ps.setString(2, entidad.getGenero());
                    ps.setInt(3, entidad.getAvisoConsentimiento());
                    ps.setString(4, entidad.getNombre());
                    ps.setInt(5, entidad.getIdColaborador());
                    return ps.executeUpdate() > 0;
                } catch (SQLException ignored) {}
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, entidad.getRangoEdad());
                ps.setString(2, entidad.getGenero());
                ps.setInt(3, entidad.getAvisoConsentimiento());
                ps.setInt(4, entidad.getIdColaborador());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        String deleteRespuesta = "DELETE FROM respuesta_cuestionario WHERE id_sesion IN (SELECT id_sesion FROM sesion_evaluacion WHERE id_colaborador = ?)";
        String deleteSesion = "DELETE FROM sesion_evaluacion WHERE id_colaborador = ?";
        String deleteColaborador = "DELETE FROM colaborador WHERE id_colaborador = ?";

        Connection con = null;
        PreparedStatement psRes = null;
        PreparedStatement psSes = null;
        PreparedStatement psCol = null;

        try {
            con = SQLConnector.getConnection();
            con.setAutoCommit(false);

            try {
                psRes = con.prepareStatement(deleteRespuesta);
                psRes.setInt(1, id);
                psRes.executeUpdate();
            } catch (SQLException ignored) {}

            psSes = con.prepareStatement(deleteSesion);
            psSes.setInt(1, id);
            psSes.executeUpdate();

            psCol = con.prepareStatement(deleteColaborador);
            psCol.setInt(1, id);
            int rows = psCol.executeUpdate();

            con.commit();
            return rows > 0;
        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            try {
                if (psRes != null) psRes.close();
                if (psSes != null) psSes.close();
                if (psCol != null) psCol.close();
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    public void eliminarPorPrueba(int idPrueba) {
        String sql = "DELETE FROM colaborador WHERE id_colaborador IN (SELECT id_colaborador FROM sesion_evaluacion WHERE id_prueba = ?)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPrueba);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double edadPromedio(int idPrueba) {
        String sql = "SELECT AVG(CASE WHEN REGEXP_LIKE(c.rango_edad, '^[0-9]+$') THEN TO_NUMBER(c.rango_edad) ELSE 0 END) " +
                     "FROM colaborador c JOIN sesion_evaluacion s ON c.id_colaborador = s.id_colaborador " +
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

    public Map<String, Double> distribucionPorSexo(int idPrueba) {
        Map<String, Double> resultado = new LinkedHashMap<>();
        String sqlTotal = "SELECT COUNT(*) FROM sesion_evaluacion WHERE id_prueba = ?";
        String sqlDistrib = "SELECT c.genero, COUNT(*) as total " +
                            "FROM colaborador c JOIN sesion_evaluacion s ON c.id_colaborador = s.id_colaborador " +
                            "WHERE s.id_prueba = ? GROUP BY c.genero";

        try (Connection con = SQLConnector.getConnection();
             PreparedStatement psTotal = con.prepareStatement(sqlTotal);
             PreparedStatement psDistrib = con.prepareStatement(sqlDistrib)) {

            psTotal.setInt(1, idPrueba);
            int total = 0;
            try (ResultSet rsTotal = psTotal.executeQuery()) {
                if (rsTotal.next()) {
                    total = rsTotal.getInt(1);
                }
            }

            if (total == 0) return resultado;

            psDistrib.setInt(1, idPrueba);
            try (ResultSet rsDistrib = psDistrib.executeQuery()) {
                while (rsDistrib.next()) {
                    String sexo = rsDistrib.getString("genero");
                    int conteo = rsDistrib.getInt("total");
                    double pct = Math.round(((conteo * 100.0) / total) * 10.0) / 10.0;
                    resultado.put(sexo != null ? sexo : "No especificado", pct);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    // ---- helper de conversión ResultSet -> objeto ----

    private Colaborador aColaborador(ResultSet rs) throws SQLException {
        Colaborador col = new Colaborador(
                rs.getInt("id_colaborador"),
                rs.getString("rango_edad"),
                rs.getString("genero"),
                rs.getInt("aviso_consentimiento")
        );
        try {
            col.setIdPrueba(rs.getInt("id_prueba"));
        } catch (SQLException ignored) {}
        try {
            col.setIdSesion(rs.getInt("id_sesion"));
        } catch (SQLException ignored) {}
        try {
            String dbNombre = rs.getString("nombre");
            if (dbNombre != null && !dbNombre.trim().isEmpty()) {
                col.setNombre(dbNombre.trim());
            }
        } catch (SQLException ignored) {}
        try {
            java.sql.Timestamp ts = rs.getTimestamp("fecha_hora_ejecucion");
            if (ts != null) {
                col.setFechaRealizacion(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(ts));
            }
        } catch (SQLException ignored) {}
        return col;
    }
}
