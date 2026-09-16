package es.jcyl.eclap.spring.backend;


import es.jcyl.eclap.spring.backend.persistencia.entidades.Rol;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Tarea;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Usuario;
import es.jcyl.eclap.spring.backend.persistencia.repositorios.RolesRepositorio;
import es.jcyl.eclap.spring.backend.persistencia.repositorios.TareasRepositorio;
import es.jcyl.eclap.spring.backend.persistencia.repositorios.UsuariosRepositorio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // rollback automático al terminar cada test
public class ConsultasRepositorioTest {

    @Autowired
    private TareasRepositorio tareaRepo;

    @Autowired
    private UsuariosRepositorio usuarioRepo;

    @Autowired
    private RolesRepositorio rolRepo;

    // ============================================================
    // TareasRepositorio
    // ============================================================
    @Test
    public void testFindByUsuarioIdPaginado() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("estado").descending());

        Page<Tarea> pagina = tareaRepo.findByUsuarioId(1, pageable);

        assertEquals(3, pagina.getTotalElements()); // Usuario1 tiene 3 tareas en total
        assertEquals(2, pagina.getContent().size()); // pero pedimos solo 2 por página
        assertTrue(pagina.hasNext());
        // ordenado por estado desc: la primera tarea de la página debe ser la de estado 100
        assertEquals(100, pagina.getContent().get(0).getEstado());
    }

    @Test
    public void testObtenerTareasCompletadasPorUsuario() {
        List<Tarea> completadasUsuario1 = tareaRepo.obtenerTareasCompletadasPorUsuario(1);
        List<Tarea> completadasUsuario2 = tareaRepo.obtenerTareasCompletadasPorUsuario(2);

        assertEquals(1, completadasUsuario1.size()); // "Preparar entorno de desarrollo" (100)
        assertEquals(0, completadasUsuario2.size()); // ninguna tarea del Usuario2 está al 100%
    }

    // ============================================================
    // UsuariosRepositorio
    // ============================================================

    @Test
    public void testFindByCorreo() {
        Optional<Usuario> usuario = usuarioRepo.findByCorreo("formacion@eclap.jcyl.es");

        assertTrue(usuario.isPresent());
        assertEquals("Usuario demo", usuario.get().getNombreCompleto());
    }


    @Test
    public void testObtenerUsuariosPorRol() {
        List<Usuario> admins = usuarioRepo.obtenerUsuariosPorRol("ADMINISTRADOR");
        List<Usuario> base = usuarioRepo.obtenerUsuariosPorRol("BASE");

        assertEquals(1, admins.size());
        assertEquals("Usuario demo", admins.get(0).getNombreCompleto());
        assertEquals(3, base.size()); // los tres usuarios tienen rol BASE
    }

    @Test
    public void testObtenerUsuariosSinTareas() {
        // Con los datos sembrados, todos los usuarios tienen tareas
        List<Usuario> sinTareasAntes = usuarioRepo.obtenerUsuariosSinTareas();
        assertTrue(sinTareasAntes.isEmpty());

        // Creamos un usuario nuevo, sin tareas, dentro de esta transacción (se hará rollback al final)
        Usuario nuevo = usuarioRepo.saveAndFlush(Usuario.builder()
                .nombreCompleto("Usuario Sin Tareas")
                .correo("sintareas." + System.nanoTime() + "@test.com")
                .clave("ClaveSinTareas1")
                .build());

        List<Usuario> sinTareasDespues = usuarioRepo.obtenerUsuariosSinTareas();

        assertEquals(1, sinTareasDespues.size());
        assertEquals(nuevo.getId(), sinTareasDespues.get(0).getId());
    }

    // ============================================================
    // RolesRepositorio
    // ============================================================

    @Test
    public void testContarUsuariosPorRol() {
        assertEquals(3, rolRepo.contarUsuariosPorRol("BASE"));
        assertEquals(1, rolRepo.contarUsuariosPorRol("ADMINISTRADOR"));
    }


}
