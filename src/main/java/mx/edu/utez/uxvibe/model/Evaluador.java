package mx.edu.utez.uxvibe.model;

import java.io.Serializable;

public class Evaluador implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idEvaluador;
    private String nombre;
    private String apellidoM;
    private String apellidoP;
    private String correo;
    private String contrasena;

    public Evaluador() {
    }

    public Evaluador(int idEvaluador, String nombre, String apellidoM, String apellidoP, String correo, String contrasena) {
        this.idEvaluador = idEvaluador;
        this.nombre = nombre;
        this.apellidoM = apellidoM;
        this.apellidoP = apellidoP;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public int getIdEvaluador() { return idEvaluador; }
    public void setIdEvaluador(int idEvaluador) { this.idEvaluador = idEvaluador; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidoM() { return apellidoM; }
    public void setApellidoM(String apellidoM) { this.apellidoM = apellidoM; }

    public String getApellidoP() { return apellidoP; }
    public void setApellidoP(String apellidoP) { this.apellidoP = apellidoP; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getNombreCompleto() { 
        return nombre + " " + apellidoP + (apellidoM != null && !apellidoM.isEmpty() ? " " + apellidoM : ""); 
    }

    // Métodos de compatibilidad temporal para evitar romper vistas antes de la fase 2
    public int getId() { return idEvaluador; }
    public void setId(int id) { this.idEvaluador = id; }
    public String getEmail() { return correo; }
    public void setEmail(String email) { this.correo = email; }
    public String getContrasenaHash() { return contrasena; }
    public void setContrasenaHash(String contrasenaHash) { this.contrasena = contrasenaHash; }
    public String getRol() { return "Evaluador UX"; }
}
