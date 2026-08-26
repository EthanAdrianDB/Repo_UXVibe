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

/**
 * DAO para la tabla Prueba.
 * Administra las pruebas de usabilidad creadas por los evaluadores, incluyendo
 * la creación, listado con conteo de participantes, edición, borrado y validación de nombres duplicados.
 */
public class PruebaDao implements Dao<Prueba, Integer> {

    /**
     * Inserta una nueva prueba asignada al evaluador logueado.
     * Recupera la clave primaria autogenerada en Oracle.
     */
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

    /**
     * Obtiene todas las pruebas registradas en el sistema junto con el conteo de participantes.
     */
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

    /**
     * Obtiene las pruebas creadas exclusivamente por un evaluador específico.
     * Incluye una subconsulta para saber cuántos participantes tiene asignada cada prueba.
     */
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

    /**
     * Busca una prueba por su ID e incluye la cantidad de participantes registrados en ella.
     */
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

    /**
     * Actualiza el nombre, descripción y URL del sistema bajo prueba.
     */
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

    /**
     * Elimina una prueba por su ID.
     */
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

    /**
     * Helper para buscar la prueba a partir de su enlace o ID numérico.
     */
    public Prueba getByEnlace(String enlaceUnico) {
        try {
            int id = Integer.parseInt(enlaceUnico);
            return getById(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Revisa si el evaluador ya tiene otra prueba con el mismo nombre (ignorando mayúsculas/minúsculas)
     * para evitar nombres duplicados al crear o editar.
     */
    public boolean existePrueba(int idEvaluador, String nombre, int idPruebaExcluida) {
        String sql = "SELECT COUNT(*) FROM Prueba WHERE id_evaluador = ? AND LOWER(nombre) = LOWER(?) AND id_prueba != ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEvaluador);
            ps.setString(2, nombre.trim());
            ps.setInt(3, idPruebaExcluida);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Helper para mapear una fila de ResultSet a un objeto Prueba.
     */
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
