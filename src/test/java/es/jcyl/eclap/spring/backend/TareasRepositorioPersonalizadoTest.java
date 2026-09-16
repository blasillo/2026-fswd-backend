package es.jcyl.eclap.spring.backend;


import es.jcyl.eclap.spring.backend.persistencia.entidades.Tarea;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Usuario;
import es.jcyl.eclap.spring.backend.persistencia.repositorios.TareasRepositorio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TareasRepositorioPersonalizadoTest {

    @Autowired
    private TareasRepositorio tareaRepo;

    @Test
    public void testBuscarTareasPorRolYEstadoMinimo_soloDevuelveLoQueCumpleElFiltro() {
        String rol = "BASE";
        int estadoMinimo = 0;

        List<Tarea> resultado = tareaRepo.buscarTareasPorRolYEstadoMinimo(rol, estadoMinimo);

        // Cada tarea devuelta DEBE cumplir realmente el filtro
        for (Tarea t : resultado) {
            assertTrue(t.getEstado() >= estadoMinimo,
                    "Tarea " + t.getId() + " tiene estado " + t.getEstado() + " < " + estadoMinimo);
            boolean tieneElRol = t.getUsuario().getRoles().stream()
                    .anyMatch(r -> r.getNombre().equals(rol));
            assertTrue(tieneElRol,
                    "La tarea " + t.getId() + " es de un usuario sin el rol " + rol);
        }
    }

    @Test
    public void testObtenerTareasConUsuarioYRoles_cadaTareaEstaBajoSuPropioUsuario() {
        Map<Usuario, List<Tarea>> resultado = tareaRepo.obtenerTareasConUsuarioYRoles();

        for (Map.Entry<Usuario, List<Tarea>> entrada : resultado.entrySet()) {
            Usuario usuarioClave = entrada.getKey();

            for (Tarea t : entrada.getValue()) {
                assertEquals(usuarioClave.getId(), t.getUsuario().getId(),
                        "La tarea " + t.getId() + " está agrupada bajo un usuario que no es el suyo");
            }
        }
    }
}
