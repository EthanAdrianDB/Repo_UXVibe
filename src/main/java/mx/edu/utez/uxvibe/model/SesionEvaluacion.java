package mx.edu.utez.uxvibe.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class SesionEvaluacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idSesion;
    private int idPrueba;
    private int idColaborador;
    private byte[] audioGrabacion;
    private Timestamp fechaHoraEjecucion;

    public SesionEvaluacion() {
    }

    public SesionEvaluacion(int idSesion, int idPrueba, int idColaborador, byte[] audioGrabacion, Timestamp fechaHoraEjecucion) {
        this.idSesion = idSesion;
        this.idPrueba = idPrueba;
        this.idColaborador = idColaborador;
        this.audioGrabacion = audioGrabacion;
        this.fechaHoraEjecucion = fechaHoraEjecucion;
    }

    public int getIdSesion() { return idSesion; }
    public void setIdSesion(int idSesion) { this.idSesion = idSesion; }

    public int getIdPrueba() { return idPrueba; }
    public void setIdPrueba(int idPrueba) { this.idPrueba = idPrueba; }

    public int getIdColaborador() { return idColaborador; }
    public void setIdColaborador(int idColaborador) { this.idColaborador = idColaborador; }

    public byte[] getAudioGrabacion() { return audioGrabacion; }
    public void setAudioGrabacion(byte[] audioGrabacion) { this.audioGrabacion = audioGrabacion; }

    public Timestamp getFechaHoraEjecucion() { return fechaHoraEjecucion; }
    public void setFechaHoraEjecucion(Timestamp fechaHoraEjecucion) { this.fechaHoraEjecucion = fechaHoraEjecucion; }
}
