package mx.edu.utez.uxvibe.model;

import java.io.InputStream;
import java.io.Serializable;

public class ArchivoAudio implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idAudio;
    private int idParticipante;
    private int idPrueba;
    private InputStream audio; // Para manejo de BLOB

    public ArchivoAudio() {
    }

    public int getIdAudio() { return idAudio; }
    public void setIdAudio(int idAudio) { this.idAudio = idAudio; }

    public int getIdParticipante() { return idParticipante; }
    public void setIdParticipante(int idParticipante) { this.idParticipante = idParticipante; }

    public int getIdPrueba() { return idPrueba; }
    public void setIdPrueba(int idPrueba) { this.idPrueba = idPrueba; }

    public InputStream getAudio() { return audio; }
    public void setAudio(InputStream audio) { this.audio = audio; }
}
