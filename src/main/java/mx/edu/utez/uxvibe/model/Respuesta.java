package mx.edu.utez.uxvibe.model;

import java.io.Serializable;

/**
 * Mapea:
 * - 3 reactivos del modelo SAM (Self-Assessment Manikin, escala 1-9: Valencia, Activación, Dominancia).
 * - 15 preguntas de usabilidad general (escala Likert 1-5).
 * - 2 reactivos de frecuencia emocional (estrés / relajación).
 */
public class Respuesta implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idRespuestas;
    private int idParticipante;
    private int idPrueba;

    // Reactivos del modelo SAM (1 al 9)
    private int sam1; // Valencia
    private int sam2; // Activación
    private int sam3; // Dominancia

    // Reactivos de Usabilidad (1 al 5)
    private int r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15;

    // Frecuencias emocionales (1 al 5)
    private int frecuenciaEstadoAnimo1;
    private int frecuenciaEstadoAnimo2;

    public Respuesta() {
    }

    public int getIdRespuestas() { return idRespuestas; }
    public void setIdRespuestas(int idRespuestas) { this.idRespuestas = idRespuestas; }

    public int getIdParticipante() { return idParticipante; }
    public void setIdParticipante(int idParticipante) { this.idParticipante = idParticipante; }

    public int getIdPrueba() { return idPrueba; }
    public void setIdPrueba(int idPrueba) { this.idPrueba = idPrueba; }

    public int getSam1() { return sam1; }
    public void setSam1(int sam1) { this.sam1 = sam1; }

    public int getSam2() { return sam2; }
    public void setSam2(int sam2) { this.sam2 = sam2; }

    public int getSam3() { return sam3; }
    public void setSam3(int sam3) { this.sam3 = sam3; }

    public int getR1() { return r1; }
    public void setR1(int r1) { this.r1 = r1; }
    public int getR2() { return r2; }
    public void setR2(int r2) { this.r2 = r2; }
    public int getR3() { return r3; }
    public void setR3(int r3) { this.r3 = r3; }
    public int getR4() { return r4; }
    public void setR4(int r4) { this.r4 = r4; }
    public int getR5() { return r5; }
    public void setR5(int r5) { this.r5 = r5; }
    public int getR6() { return r6; }
    public void setR6(int r6) { this.r6 = r6; }
    public int getR7() { return r7; }
    public void setR7(int r7) { this.r7 = r7; }
    public int getR8() { return r8; }
    public void setR8(int r8) { this.r8 = r8; }
    public int getR9() { return r9; }
    public void setR9(int r9) { this.r9 = r9; }
    public int getR10() { return r10; }
    public void setR10(int r10) { this.r10 = r10; }
    public int getR11() { return r11; }
    public void setR11(int r11) { this.r11 = r11; }
    public int getR12() { return r12; }
    public void setR12(int r12) { this.r12 = r12; }
    public int getR13() { return r13; }
    public void setR13(int r13) { this.r13 = r13; }
    public int getR14() { return r14; }
    public void setR14(int r14) { this.r14 = r14; }
    public int getR15() { return r15; }
    public void setR15(int r15) { this.r15 = r15; }

    public int getFrecuenciaEstadoAnimo1() { return frecuenciaEstadoAnimo1; }
    public void setFrecuenciaEstadoAnimo1(int frecuenciaEstadoAnimo1) { this.frecuenciaEstadoAnimo1 = frecuenciaEstadoAnimo1; }

    public int getFrecuenciaEstadoAnimo2() { return frecuenciaEstadoAnimo2; }
    public void setFrecuenciaEstadoAnimo2(int frecuenciaEstadoAnimo2) { this.frecuenciaEstadoAnimo2 = frecuenciaEstadoAnimo2; }
    
    // Métodos temporales de compatibilidad para evitar roturas
    public int getValor() { return r1; } 
    public void setValor(int valor) { this.r1 = valor; }
    public String getComentariosLibres() { return ""; }
    public void setComentariosLibres(String s) { }
    public int getIdSesion() { return idParticipante; }
    public void setIdSesion(int id) { this.idParticipante = id; }
    public int getIdRespuesta() { return idRespuestas; }
    public void setIdRespuesta(int id) { this.idRespuestas = id; }
}
