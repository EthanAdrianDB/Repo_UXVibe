package mx.edu.utez.uxvibe.model.dao;

import mx.edu.utez.uxvibe.model.ArchivoAudio;
import mx.edu.utez.uxvibe.utils.SQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;

/**
 * DAO para la tabla Archivo_Audio.
 * Guarda y recupera los archivos de audio binarios (BLOB en Oracle)
 * grabados durante las sesiones de evaluación de los participantes.
 */
public class ArchivoAudioDao implements Dao<ArchivoAudio, Integer> {

    /**
     * Inserta un nuevo archivo de audio en formato BLOB asociado al participante y su prueba.
     */
    @Override
    public boolean create(ArchivoAudio entidad) {
        String sql = "INSERT INTO Archivo_Audio (id_participante, id_prueba, audio) VALUES (?, ?, ?)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"ID_AUDIO"})) {
            
            ps.setInt(1, entidad.getIdParticipante());
            ps.setInt(2, entidad.getIdPrueba());
            ps.setBlob(3, entidad.getAudio());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs != null && rs.next()) {
                        try {
                            entidad.setIdAudio(rs.getInt(1));
                        } catch (Exception ignored) {}
                    }
                } catch (Exception ignored) {}
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[ArchivoAudioDao] Error al insertar audio: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * No se implementa listar todos los audios directos a memoria para no saturar el servidor con datos binarios pesados.
     */
    @Override
    public List<ArchivoAudio> getAll() {
        return new ArrayList<>();
    }

    /**
     * Busca los metadatos de un audio por su ID.
     */
    @Override
    public ArchivoAudio getById(Integer id) {
        String sql = "SELECT * FROM Archivo_Audio WHERE id_audio = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return aArchivoAudio(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obtiene el flujo binario (InputStream) del audio para transmitirlo (streaming) al navegador mediante AudioServlet.
     */
    public InputStream getAudioStream(int idParticipante, int idPrueba) {
        String sql = "SELECT audio FROM Archivo_Audio WHERE id_participante = ? AND id_prueba = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idParticipante);
            ps.setInt(2, idPrueba);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBinaryStream("audio");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Descarga el audio completo como arreglo de bytes (byte[]).
     */
    public byte[] getAudioBytes(int idParticipante, int idPrueba) {
        String sql = "SELECT audio FROM Archivo_Audio WHERE id_participante = ? AND id_prueba = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idParticipante);
            ps.setInt(2, idPrueba);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    java.sql.Blob blob = rs.getBlob("audio");
                    if (blob != null) {
                        return blob.getBytes(1, (int) blob.length());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Verifica de forma rápida si un participante ya tiene una grabación de audio subida.
     */
    public boolean hasAudio(int idParticipante, int idPrueba) {
        String sql = "SELECT COUNT(*) FROM Archivo_Audio WHERE id_participante = ? AND id_prueba = ?";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idParticipante);
            ps.setInt(2, idPrueba);
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
     * Los archivos de audio no se actualizan en sitio.
     */
    @Override
    public boolean update(ArchivoAudio entidad) {
        return false;
    }

    /**
     * Elimina un archivo de audio por ID.
     */
    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM Archivo_Audio WHERE id_audio = ?";
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
     * Helper para mapear metadatos del ResultSet al POJO ArchivoAudio.
     */
    private ArchivoAudio aArchivoAudio(ResultSet rs) throws SQLException {
        ArchivoAudio aa = new ArchivoAudio();
        aa.setIdAudio(rs.getInt("id_audio"));
        aa.setIdParticipante(rs.getInt("id_participante"));
        aa.setIdPrueba(rs.getInt("id_prueba"));
        return aa;
    }
}
