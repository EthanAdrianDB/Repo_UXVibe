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

public class ArchivoAudioDao implements Dao<ArchivoAudio, Integer> {

    @Override
    public boolean create(ArchivoAudio entidad) {
        String sql = "INSERT INTO Archivo_Audio (id_participante, id_prueba, audio) VALUES (?, ?, ?)";
        try (Connection con = SQLConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, entidad.getIdParticipante());
            ps.setInt(2, entidad.getIdPrueba());
            ps.setBlob(3, entidad.getAudio());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        entidad.setIdAudio(rs.getInt(1));
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
    public List<ArchivoAudio> getAll() {
        return new ArrayList<>(); // Usualmente no listaríamos todos los audios de la BD sin paginar
    }

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

    @Override
    public boolean update(ArchivoAudio entidad) {
        return false; // No se actualiza un audio
    }

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

    private ArchivoAudio aArchivoAudio(ResultSet rs) throws SQLException {
        ArchivoAudio aa = new ArchivoAudio();
        aa.setIdAudio(rs.getInt("id_audio"));
        aa.setIdParticipante(rs.getInt("id_participante"));
        aa.setIdPrueba(rs.getInt("id_prueba"));
        // El InputStream del audio solo se extrae cuando se necesite con getAudioStream
        return aa;
    }
}
