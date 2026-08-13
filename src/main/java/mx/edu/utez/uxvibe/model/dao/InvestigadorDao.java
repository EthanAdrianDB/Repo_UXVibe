package mx.edu.utez.uxvibe.model.dao;

import mx.edu.utez.uxvibe.model.Investigador;
import mx.edu.utez.uxvibe.utils.SQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InvestigadorDao implements Dao<Investigador, Integer> {

    @Override
    public boolean create(Investigador entidad) {
        String sqlMax = "SELECT NVL(MAX(id_investigador), 0) + 1 FROM investigador";
        String sqlWithId = "INSERT INTO investigador (id_investigador, nombre, email, contrasena_hash) VALUES (?, ?, ?, ?)";
        String sqlIdentity = "INSERT INTO investigador (nombre, email, contrasena_hash) VALUES (?, ?, ?)";

        try (Connection con = SQLConnector.getConnection()) {
            int newId = 1;
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlMax)) {
                if (rs.next()) {
                    newId = rs.getInt(1);
                }
            }

            try (PreparedStatement ps = con.prepareStatement(sqlWithId)) {
                ps.setInt(1, newId);
                ps.setString(2, entidad.getNombre());
                ps.setString(3, entidad.getCorreo());
                ps.setString(4, entidad.getContrasenaHash());

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    entidad.setIdInvestigador(newId);
                    return true;
                }
            } catch (SQLException ex1) {
                // Fallback for IDENTITY columns
                try (PreparedStatement psIdentity = con.prepareStatement(sqlIdentity, Statement.RETURN_GENERATED_KEYS)) {
                    psIdentity.setString(1, entidad.getNombre());
                    psIdentity.setString(2, entidad.getCorreo());
                    psIdentity.setString(3, entidad.getContrasenaHash());

                    int rows = psIdentity.executeUpdate();
                    if (rows > 0) {
                        try (ResultSet rsKeys = psIdentity.getGeneratedKeys()) {
                            if (rsKeys.next()) {
                                entidad.setIdInvestigador(rsKeys.getInt(1));
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
    public List<Investigador> getAll() {
        List<Investigador> lista = new ArrayList<>();
        String sql = "SELECT * FROM investigador";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(aInvestigador(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Investigador getById(Integer id) {
        String sql = "SELECT * FROM investigador WHERE id_investigador = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return aInvestigador(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Investigador entidad) {
        String sql = "UPDATE investigador SET nombre = ?, email = ?, contrasena_hash = ? WHERE id_investigador = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getCorreo());
            ps.setString(3, entidad.getContrasenaHash());
            ps.setInt(4, entidad.getIdInvestigador());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM investigador WHERE id_investigador = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Investigador buscarPorCorreo(String correo) {
        String sql = "SELECT * FROM investigador WHERE LOWER(email) = LOWER(?)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return aInvestigador(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean correoExiste(String correo) {
        return buscarPorCorreo(correo) != null;
    }

    private static final Map<String, String> TOKENS_RECUPERACION = new java.util.concurrent.ConcurrentHashMap<>();

    public boolean guardarTokenRecuperacion(String correo, String token) {
        if (correo != null && token != null) {
            TOKENS_RECUPERACION.put(token.trim(), correo.trim().toLowerCase());
            return true;
        }
        return false;
    }

    public Investigador buscarPorToken(String token) {
        if (token == null) return null;
        String correo = TOKENS_RECUPERACION.get(token.trim());
        if (correo != null) {
            return buscarPorCorreo(correo);
        }
        return null;
    }

    public void eliminarToken(String token) {
        if (token != null) {
            TOKENS_RECUPERACION.remove(token.trim());
        }
    }

    public boolean actualizarContrasena(int idInvestigador, String nuevoHash, String nuevoSalt) {
        Investigador e = getById(idInvestigador);
        if (e == null) return false;
        e.setContrasenaHash(nuevoHash);
        return update(e);
    }

    // ---- helper de conversión ResultSet -> objeto ----

    private Investigador aInvestigador(ResultSet rs) throws SQLException {
        return new Investigador(
                rs.getInt("id_investigador"),
                rs.getString("nombre"),
                rs.getString("email"),
                rs.getString("contrasena_hash")
        );
    }
}
