package mx.edu.utez.uxvibe.model;

import java.io.Serializable;

public class Colaborador implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idColaborador;
    private int idSesion;
    private int idPrueba; // Campo en memoria para compatibilidad
    private String rangoEdad;
    private String genero;
    private int avisoConsentimiento; // 1 = Aceptado, 0 = No

    private String nombre;
    private String fechaRealizacion;

    public Colaborador() {
    }

    public Colaborador(int idColaborador, String rangoEdad, String genero, int avisoConsentimiento) {
        this.idColaborador = idColaborador;
        this.rangoEdad = rangoEdad;
        this.genero = genero;
        this.avisoConsentimiento = avisoConsentimiento;
    }

    public int getIdColaborador() { return idColaborador; }
    public void setIdColaborador(int idColaborador) { this.idColaborador = idColaborador; }

    public int getIdSesion() { return idSesion; }
    public void setIdSesion(int idSesion) { this.idSesion = idSesion; }

    public int getIdPrueba() { return idPrueba; }
    public void setIdPrueba(int idPrueba) { this.idPrueba = idPrueba; }

    // Compatibilidad
    public int getId() { return idColaborador; }
    public void setId(int id) { this.idColaborador = id; }

    public String getRangoEdad() { return rangoEdad; }
    public void setRangoEdad(String rangoEdad) { this.rangoEdad = rangoEdad; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public int getAvisoConsentimiento() { return avisoConsentimiento; }
    public void setAvisoConsentimiento(int avisoConsentimiento) { this.avisoConsentimiento = avisoConsentimiento; }

    // Compatibilidad con JSPs de participantes
    public String getNombre() {
        if (nombre == null || nombre.isEmpty()) {
            return "Colaborador #" + idColaborador;
        }
        return nombre;
    }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getEdad() {
        try {
            if (rangoEdad != null) {
                return Integer.parseInt(rangoEdad);
            }
        } catch (NumberFormatException ignored) {}
        return 0;
    }
    public void setEdad(int edad) {
        this.rangoEdad = String.valueOf(edad);
    }

    public String getSexo() { return genero; }
    public void setSexo(String sexo) { this.genero = sexo; }

    public String getFechaRealizacion() {
        if (fechaRealizacion == null || fechaRealizacion.trim().isEmpty()) {
            return "-";
        }
        return fechaRealizacion;
    }
    public void setFechaRealizacion(String fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }

    public String getDuracionFormateada() { return "0:00"; }
    public String getAudioPath() { return ""; }
}
