package mx.edu.utez.uxvibe.model;

import java.io.Serializable;

/**
 * Almacena datos demográficos (edad, sexo) y metadatos de su sesión (fecha de realización).
 */
public class Participante implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idParticipante;
    private String nombre;
    private String apellidoM;
    private String apellidoP;
    private int sexo; // 1 = Masculino, 0 = Femenino
    private int idPrueba;
    private int edad;
    private String fechaRealizacion;
    private String duracionFormateada;
    private String audioPath;

    public Participante() {
    }

    public Participante(int idParticipante, String nombre, String apellidoM, String apellidoP, int sexo, int idPrueba) {
        this.idParticipante = idParticipante;
        this.nombre = nombre;
        this.apellidoM = apellidoM;
        this.apellidoP = apellidoP;
        this.sexo = sexo;
        this.idPrueba = idPrueba;
    }

    public int getIdParticipante() { return idParticipante; }
    public void setIdParticipante(int idParticipante) { this.idParticipante = idParticipante; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidoM() { return apellidoM; }
    public void setApellidoM(String apellidoM) { this.apellidoM = apellidoM; }

    public String getApellidoP() { return apellidoP; }
    public void setApellidoP(String apellidoP) { this.apellidoP = apellidoP; }

    public int getSexo() { return sexo; }
    public void setSexo(int sexo) { this.sexo = sexo; }

    public int getIdPrueba() { return idPrueba; }
    public void setIdPrueba(int idPrueba) { this.idPrueba = idPrueba; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getFechaRealizacion() { return fechaRealizacion; }
    public void setFechaRealizacion(String fechaRealizacion) { this.fechaRealizacion = fechaRealizacion; }

    public String getDuracionFormateada() { return duracionFormateada; }
    public void setDuracionFormateada(String duracionFormateada) { this.duracionFormateada = duracionFormateada; }

    public String getAudioPath() { return audioPath; }
    public void setAudioPath(String audioPath) { this.audioPath = audioPath; }

    // Compatibilidad temporal para evitar errores en vistas antes de la fase 2
    public int getIdColaborador() { return idParticipante; }
    public void setIdColaborador(int idColaborador) { this.idParticipante = idColaborador; }
    
    public int getIdSesion() { return idParticipante; } // Mapeado a idParticipante por ahora
    public void setIdSesion(int idSesion) { }
    
    public String getRangoEdad() { return "No definido"; }
    public void setRangoEdad(String rangoEdad) { }
    
    public String getGenero() { return sexo == 1 ? "Masculino" : "Femenino"; }
    public void setGenero(String genero) { }
    
    public int getAvisoConsentimiento() { return 1; }
    public void setAvisoConsentimiento(int aviso) { }
}
