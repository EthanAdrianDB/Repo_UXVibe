package mx.edu.utez.uxvibe.model;

import java.io.Serializable;

/**
 * Modelo (POJO) que representa una Prueba de Usabilidad creada por un Evaluador.
 * Contiene el nombre del estudio, la descripción o tarea asignada, la URL del sistema bajo prueba
 * y el conteo de participantes registrados.
 */
public class Prueba implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idPrueba;
    private int idEvaluador;
    private String nombre;
    private String descripcion;
    private String urlSistema;

    // Campo calculado, se llena dinámicamente en las consultas del DAO
    private int totalParticipantes;

    public Prueba() {
    }

    public Prueba(int idPrueba, int idEvaluador, String nombre, String descripcion, String urlSistema) {
        this.idPrueba = idPrueba;
        this.idEvaluador = idEvaluador;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.urlSistema = urlSistema;
    }

    public int getIdPrueba() { return idPrueba; }
    public void setIdPrueba(int idPrueba) { this.idPrueba = idPrueba; }

    public int getIdEvaluador() { return idEvaluador; }
    public void setIdEvaluador(int idEvaluador) { this.idEvaluador = idEvaluador; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUrlSistema() { return urlSistema; }
    public void setUrlSistema(String urlSistema) { this.urlSistema = urlSistema; }

    public int getTotalParticipantes() { return totalParticipantes; }
    public void setTotalParticipantes(int totalParticipantes) { this.totalParticipantes = totalParticipantes; }

    // Compatibilidad temporal para evitar errores en vistas antes de la fase 2
    public int getId() { return idPrueba; }
    public void setId(int id) { this.idPrueba = id; }
    
    public int getIdInvestigador() { return idEvaluador; }
    public void setIdInvestigador(int idInvestigador) { this.idEvaluador = idInvestigador; }
    
    public String getNombreEstudio() { return nombre; }
    public void setNombreEstudio(String nombreEstudio) { this.nombre = nombreEstudio; }
    
    public String getTareaDescripcion() { return descripcion; }
    public void setTareaDescripcion(String tareaDescripcion) { this.descripcion = tareaDescripcion; }
    
    public String getTarea() { return descripcion; }
    public void setTarea(String tarea) { this.descripcion = tarea; }
    
    public String getUrlDestino() { return urlSistema; }
    public void setUrlDestino(String urlDestino) { this.urlSistema = urlDestino; }
    
    public String getUrl() { return urlSistema; }
    public void setUrl(String url) { this.urlSistema = url; }
    
    public String getEnlaceUnico() { return String.valueOf(idPrueba); } // Enlace basado en ID por ahora
}
