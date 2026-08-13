package mx.edu.utez.uxvibe.model.dao;

import mx.edu.utez.uxvibe.model.Prueba;
import mx.edu.utez.uxvibe.utils.SQLConnector;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PruebaDao implements Dao<Prueba, Integer> {

    @Override
    public boolean create(Prueba entidad) {
        if (entidad.getFechaInicio() == null) {
            entidad.setFechaInicio(Date.valueOf(LocalDate.now()));
        }
        if (entidad.getFechaFin() == null) {
            entidad.setFechaFin(Date.valueOf(LocalDate.now().plusMonths(1)));
        }
        if (entidad.getEnlaceUnico() == null || entidad.getEnlaceUnico().isEmpty()) {
            entidad.setEnlaceUnico(UUID.randomUUID().toString());
        }

        String sqlMax = "SELECT NVL(MAX(id_prueba), 0) + 1 FROM prueba";
        String sqlWithId = "INSERT INTO prueba (id_prueba, id_investigador, nombre_estudio, tarea_descripcion, url_destino, fecha_inicio, fecha_fin, enlace_unico) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlIdentity = "INSERT INTO prueba (id_investigador, nombre_estudio, tarea_descripcion, url_destino, fecha_inicio, fecha_fin, enlace_unico) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = SQLConnector.getConnection()) {
            int newId = 1;
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlMax)) {
                if (rs.next()) {
                    newId = rs.getInt(1);
                }
            }

            try (PreparedStatement ps = con.prepareStatement(sqlWithId)) {
                ps.setInt(1, newId);
                ps.setInt(2, entidad.getIdInvestigador());
                ps.setString(3, entidad.getNombreEstudio());
                ps.setString(4, entidad.getTareaDescripcion());
                ps.setString(5, entidad.getUrlDestino());
                ps.setDate(6, entidad.getFechaInicio());
                ps.setDate(7, entidad.getFechaFin());
                ps.setString(8, entidad.getEnlaceUnico());

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    entidad.setIdPrueba(newId);
                    return true;
                }
            } catch (SQLException ex1) {
                // Fallback for IDENTITY columns
                try (PreparedStatement psIdentity = con.prepareStatement(sqlIdentity, Statement.RETURN_GENERATED_KEYS)) {
                    psIdentity.setInt(1, entidad.getIdInvestigador());
                    psIdentity.setString(2, entidad.getNombreEstudio());
                    psIdentity.setString(3, entidad.getTareaDescripcion());
                    psIdentity.setString(4, entidad.getUrlDestino());
                    psIdentity.setDate(5, entidad.getFechaInicio());
                    psIdentity.setDate(6, entidad.getFechaFin());
                    psIdentity.setString(7, entidad.getEnlaceUnico());

                    int rows = psIdentity.executeUpdate();
                    if (rows > 0) {
                        try (ResultSet rsKeys = psIdentity.getGeneratedKeys()) {
                            if (rsKeys.next()) {
                                entidad.setIdPrueba(rsKeys.getInt(1));
                            }
                        }
                        return true;
                    }
                } catch (SQLException ex2) {
                    ex2.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Prueba> getAll() {
        List<Prueba> lista = new ArrayList<>();
        String sql = "SELECT p.*, (SELECT COUNT(*) FROM sesion_evaluacion pt WHERE pt.id_prueba = p.id_prueba) as total_participantes FROM prueba p";
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

    public List<Prueba> getPorEvaluador(int idInvestigador) {
        List<Prueba> lista = new ArrayList<>();
        String sql = "SELECT p.*, (SELECT COUNT(*) FROM sesion_evaluacion pt WHERE pt.id_prueba = p.id_prueba) as total_participantes FROM prueba p WHERE p.id_investigador = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idInvestigador);
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
        String sql = "SELECT p.*, (SELECT COUNT(*) FROM sesion_evaluacion pt WHERE pt.id_prueba = p.id_prueba) as total_participantes FROM prueba p WHERE p.id_prueba = ?";
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

    public Prueba getByEnlace(String enlaceUnico) {
        String sql = "SELECT p.*, (SELECT COUNT(*) FROM sesion_evaluacion pt WHERE pt.id_prueba = p.id_prueba) as total_participantes FROM prueba p WHERE p.enlace_unico = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, enlaceUnico);
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
        String sql = "UPDATE prueba SET nombre_estudio = ?, tarea_descripcion = ?, url_destino = ? WHERE id_prueba = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, entidad.getNombreEstudio());
            ps.setString(2, entidad.getTareaDescripcion());
            ps.setString(3, entidad.getUrlDestino());
            ps.setInt(4, entidad.getIdPrueba());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM prueba WHERE id_prueba = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ---- helper de conversión ResultSet -> objeto ----

    private Prueba aPrueba(ResultSet rs) throws SQLException {
        return new Prueba(
                rs.getInt("id_prueba"),
                rs.getInt("id_investigador"),
                rs.getString("nombre_estudio"),
                rs.getString("tarea_descripcion"),
                rs.getString("url_destino"),
                rs.getDate("fecha_inicio"),
                rs.getDate("fecha_fin"),
                rs.getString("enlace_unico")
        );
    }
}
