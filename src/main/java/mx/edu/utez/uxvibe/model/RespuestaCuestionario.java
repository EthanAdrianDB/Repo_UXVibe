package mx.edu.utez.uxvibe.model;

import java.io.Serializable;

public class RespuestaCuestionario implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idRespuesta;
    private int idSesion;
    private int escalaSatisfaccion;
    private String comentariosLibres;

    public RespuestaCuestionario() {
    }

    public RespuestaCuestionario(int idRespuesta, int idSesion, int escalaSatisfaccion, String comentariosLibres) {
        this.idRespuesta = idRespuesta;
        this.idSesion = idSesion;
        this.escalaSatisfaccion = escalaSatisfaccion;
        this.comentariosLibres = comentariosLibres;
    }

    public int getIdRespuesta() { return idRespuesta; }
    public void setIdRespuesta(int idRespuesta) { this.idRespuesta = idRespuesta; }

    // Compatibilidad
    public int getId() { return idRespuesta; }
    public void setId(int id) { this.idRespuesta = id; }

    public int getIdSesion() { return idSesion; }
    public void setIdSesion(int idSesion) { this.idSesion = idSesion; }

    public int getIdParticipante() { return idSesion; }
    public void setIdParticipante(int idParticipante) { this.idSesion = idParticipante; }

    public int getIdPregunta() { return 0; }
    public void setIdPregunta(int idPregunta) {}

    public int getValor() { return escalaSatisfaccion; }
    public void setValor(int valor) { this.escalaSatisfaccion = valor; }

    public int getEscalasatisfaccion() { return escalaSatisfaccion; }
    public void setEscalasatisfaccion(int escalaSatisfaccion) { this.escalaSatisfaccion = escalaSatisfaccion; }

    public String getComentariosLibres() { return comentariosLibres; }
    public void setComentariosLibres(String comentariosLibres) { this.comentariosLibres = comentariosLibres; }
}
