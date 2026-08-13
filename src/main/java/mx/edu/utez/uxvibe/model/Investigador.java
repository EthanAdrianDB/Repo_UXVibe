package mx.edu.utez.uxvibe.model;

import java.io.Serializable;

public class Investigador implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idInvestigador;
    private String nombre;
    private String email;
    private String contrasenaHash;

    public Investigador() {
    }

    public Investigador(int idInvestigador, String nombre, String email, String contrasenaHash) {
        this.idInvestigador = idInvestigador;
        this.nombre = nombre;
        this.email = email;
        this.contrasenaHash = contrasenaHash;
    }

    public int getIdInvestigador() { return idInvestigador; }
    public void setIdInvestigador(int idInvestigador) { this.idInvestigador = idInvestigador; }

    // Métodos de compatibilidad
    public int getId() { return idInvestigador; }
    public void setId(int id) { this.idInvestigador = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNombres() { return nombre; }
    public void setNombres(String nombres) { this.nombre = nombres; }

    public String getCorreo() { return email; }
    public void setCorreo(String email) { this.email = email; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasenaHash() { return contrasenaHash; }
    public void setContrasenaHash(String contrasenaHash) { this.contrasenaHash = contrasenaHash; }

    public String getNombreCompleto() { return nombre; }

    public String getApellidoPaterno() { return ""; }
    public String getApellidoMaterno() { return ""; }
    public String getSalt() { return ""; }
    public String getRol() { return "Investigador UX"; }
    public String getCodigoRecuperacion() { return ""; }
}
