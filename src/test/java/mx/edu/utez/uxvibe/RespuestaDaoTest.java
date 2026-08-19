package mx.edu.utez.uxvibe;

import mx.edu.utez.uxvibe.model.Participante;
import mx.edu.utez.uxvibe.model.Respuesta;
import mx.edu.utez.uxvibe.model.dao.ParticipanteDao;
import mx.edu.utez.uxvibe.model.dao.PruebaDao;
import mx.edu.utez.uxvibe.model.dao.RespuestaDao;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RespuestaDaoTest {

    @Test
    public void testGuardarYConsultarRespuestas() {
        PruebaDao pruebaDao = new PruebaDao();
        var pruebas = pruebaDao.getAll();
        if (pruebas.isEmpty()) {
            return;
        }

        int idPrueba = pruebas.get(0).getIdPrueba();

        Participante p = new Participante();
        p.setIdPrueba(idPrueba);
        p.setNombre("Test Auto");
        p.setApellidoP("Verificacion");
        p.setSexo(1);
        p.setEdad(25);

        ParticipanteDao pDao = new ParticipanteDao();
        boolean pCreado = pDao.create(p);
        assertTrue(pCreado, "El participante debe crearse correctamente");
        assertTrue(p.getIdParticipante() > 0, "El ID de participante debe ser generado");

        Respuesta r = new Respuesta();
        r.setIdParticipante(p.getIdParticipante());
        r.setIdPrueba(idPrueba);
        r.setSam1(4);
        r.setSam2(3);
        r.setSam3(5);
        r.setR1(5);
        r.setR2(4);
        r.setR3(5);
        r.setR4(4);
        r.setR5(5);
        r.setR6(4);
        r.setR7(5);
        r.setR8(4);
        r.setR9(5);
        r.setR10(4);
        r.setR11(5);
        r.setR12(4);
        r.setR13(5);
        r.setR14(4);
        r.setR15(5);
        r.setFrecuenciaEstadoAnimo1(2);
        r.setFrecuenciaEstadoAnimo2(4);

        RespuestaDao rDao = new RespuestaDao();
        boolean rCreado = rDao.create(r);
        assertTrue(rCreado, "La respuesta debe insertarse sin errores de clave primaria");

        List<Respuesta> lista = rDao.getPorParticipante(p.getIdParticipante());
        assertFalse(lista.isEmpty(), "Debe encontrarse la respuesta del participante");
        assertEquals(5, lista.get(0).getR1());
        assertEquals(4, lista.get(0).getSam1());

        Map<String, Double> promedios = rDao.promedioPorPregunta(idPrueba);
        assertNotNull(promedios);
        assertFalse(promedios.isEmpty(), "Los promedios deben calcularse");

        // Limpieza
        pDao.delete(p.getIdParticipante());
    }
}
