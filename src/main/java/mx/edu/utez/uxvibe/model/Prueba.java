package mx.edu.utez.uxvibe.model;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Prueba implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idPrueba;
    private int idInvestigador;
    private String nombreEstudio;
    private String tareaDescripcion;
    private String urlDestino;
    private Date fechaInicio;
    private Date fechaFin;
    private String enlaceUnico;

    // Campo calculado, se llena en el DAO
    private int totalParticipantes;

    public Prueba() {
    }

    public Prueba(int idPrueba, int idInvestigador, String nombreEstudio, String tareaDescripcion,
                  String urlDestino, Date fechaInicio, Date fechaFin, String enlaceUnico) {
        this.idPrueba = idPrueba;
        this.idInvestigador = idInvestigador;
        this.nombreEstudio = nombreEstudio;
        this.tareaDescripcion = tareaDescripcion;
        this.urlDestino = urlDestino;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.enlaceUnico = enlaceUnico;
    }

    public int getIdPrueba() { return idPrueba; }
    public void setIdPrueba(int idPrueba) { this.idPrueba = idPrueba; }

    // Compatibilidad
    public int getId() { return idPrueba; }
    public void setId(int id) { this.idPrueba = id; }

    public int getIdInvestigador() { return idInvestigador; }
    public void setIdInvestigador(int idInvestigador) { this.idInvestigador = idInvestigador; }

    public int getIdEvaluador() { return idInvestigador; }
    public void setIdEvaluador(int idEvaluador) { this.idInvestigador = idEvaluador; }

    public String getNombreEstudio() { return nombreEstudio; }
    public void setNombreEstudio(String nombreEstudio) { this.nombreEstudio = nombreEstudio; }

    public String getNombre() { return nombreEstudio; }
    public void setNombre(String nombre) { this.nombreEstudio = nombre; }

    public String getTareaDescripcion() { return tareaDescripcion; }
    public void setTareaDescripcion(String tareaDescripcion) { this.tareaDescripcion = tareaDescripcion; }

    public String getTarea() { return tareaDescripcion; }
    public void setTarea(String tarea) { this.tareaDescripcion = tarea; }

    public String getUrlDestino() { return urlDestino; }
    public void setUrlDestino(String urlDestino) { this.urlDestino = urlDestino; }

    public String getUrl() { return urlDestino; }
    public void setUrl(String url) { this.urlDestino = url; }

    public String getPlataforma() { return ""; } // No existe en la base de datos real
    public void setPlataforma(String plataforma) { }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }

    public String getEnlaceUnico() { return enlaceUnico; }
    public void setEnlaceUnico(String enlaceUnico) { this.enlaceUnico = enlaceUnico; }

    public String getFechaCreacion() {
        if (fechaInicio != null) {
            return fechaInicio.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return "";
    }
    public void setFechaCreacion(String fechaCreacion) {
        try {
            if (fechaCreacion != null && !fechaCreacion.isEmpty()) {
                this.fechaInicio = Date.valueOf(LocalDate.parse(fechaCreacion, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
        } catch (Exception ignored) {}
    }

    public int getTotalParticipantes() { return totalParticipantes; }
    public void setTotalParticipantes(int totalParticipantes) { this.totalParticipantes = totalParticipantes; }
}
