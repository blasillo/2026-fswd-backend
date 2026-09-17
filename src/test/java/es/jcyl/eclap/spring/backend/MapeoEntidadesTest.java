package es.jcyl.eclap.spring.backend;

import es.jcyl.eclap.spring.backend.persistencia.entidades.Rol;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Tarea;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Usuario;
import es.jcyl.eclap.spring.backend.persistencia.repositorios.RolesRepositorio;
import es.jcyl.eclap.spring.backend.persistencia.repositorios.TareasRepositorio;
import es.jcyl.eclap.spring.backend.persistencia.repositorios.UsuariosRepositorio;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MapeoEntidadesTest {

    @Autowired
    private RolesRepositorio rolRepo;

    @Autowired
    private UsuariosRepositorio usuarioRepo;

    @Autowired
    private TareasRepositorio tareaRepo;
    @Test
    public void testInsertTarea() {

        Optional<Usuario> usuario =  usuarioRepo.findById(1);

        Tarea tarea = Tarea.builder()
                .estado(50)
                .usuario(usuario.get())
                .color("Azul")
                .nombre("Tarea de prueba")
                .build();
        tarea = tareaRepo.save(tarea);

        Tarea savedTarea = tareaRepo.findById(tarea.getId()).orElse(null);

        assertNotNull(savedTarea);
        assertEquals("Tarea de prueba", savedTarea.getNombre());
    }

    @Test
    public void testReadUsuarios() {
        Optional<Usuario> usuario =  usuarioRepo.findById(1);

        assertNotNull( usuario.get() );
        assertEquals(2 , usuario.get().getRoles().size());
    }

    @Test
    public void testCrearUsuarioConRoles() {
        Rol rol = rolRepo.save(Rol.builder()
                .nombre("TESTER_" + System.nanoTime()) // nombre único por ejecución
                .build());

        Usuario usuario = usuarioRepo.save(Usuario.builder()
                .nombreCompleto("Ana García")
                .iniciales("AG")
                .correo("ana." + System.nanoTime() + "@test.com")
                .clave("ClaveSegura1")
                .roles(List.of(rol))
                .build());

        Usuario recuperado = usuarioRepo.findById(usuario.getId()).orElse(null);

        assertNotNull(recuperado);
        assertEquals("Ana García", recuperado.getNombreCompleto());
        assertNotNull(recuperado.getFechaCreacion()); // @CreatedDate debe rellenarse solo
        assertEquals(1, recuperado.getRoles().size());
        assertTrue(recuperado.getRoles().stream().anyMatch(r -> r.getNombre().equals(rol.getNombre())));
    }

    @Test
    public void testCrearTareaAsociadaAUsuario() {
        Usuario usuario = usuarioRepo.save(Usuario.builder()
                .nombreCompleto("Carlos Ruiz")
                .correo("carlos." + System.nanoTime() + "@test.com")
                .clave("OtraClave1")
                .build());

        Tarea tarea = tareaRepo.save(Tarea.builder()
                .nombre("Revisar informe")
                .estado(30)
                .color("Verde")
                .usuario(usuario)
                .build());

        Tarea recuperada = tareaRepo.findById(tarea.getId()).orElse(null);

        assertNotNull(recuperada);
        assertEquals("Revisar informe", recuperada.getNombre());
        assertNotNull(recuperada.getUsuario());
        assertEquals(usuario.getId(), recuperada.getUsuario().getId());
        assertEquals("Carlos Ruiz", recuperada.getUsuario().getNombreCompleto());
    }

    @Test
    public void testEstadoTareaFueraDeRangoLanzaExcepcion() {
        // El proyecto incluye spring-boot-starter-validation, así que Hibernate
        // valida automáticamente las anotaciones @Min/@Max antes de hacer INSERT.
        Usuario usuario = usuarioRepo.save(Usuario.builder()
                .nombreCompleto("Test Validacion")
                .correo("valid." + System.nanoTime() + "@test.com")
                .clave("ClaveValida1")
                .build());

        Tarea tarea = Tarea.builder()
                .nombre("Tarea con estado fuera de rango")
                .estado(150) // fuera de 0-100
                .usuario(usuario)
                .build();

        assertThrows(ConstraintViolationException.class, () -> {
            tareaRepo.saveAndFlush(tarea);
        });
    }

    @Test
    public void testCorreoDuplicadoLanzaExcepcion() {
        String correo = "duplicado." + System.nanoTime() + "@test.com";

        usuarioRepo.save(Usuario.builder()
                .nombreCompleto("Primer Usuario")
                .correo(correo)
                .clave("Clave123456")
                .build());

        Usuario duplicado = Usuario.builder()
                .nombreCompleto("Segundo Usuario")
                .correo(correo) // mismo correo -> viola UNIQUE
                .clave("Clave654321")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            usuarioRepo.saveAndFlush(duplicado);
        });
    }

    @Test
    public void testUsuarioConVariosRoles() {
        Rol rolBase = rolRepo.save(Rol.builder().nombre("BASE_" + System.nanoTime()).build());
        Rol rolAdmin = rolRepo.save(Rol.builder().nombre("ADMIN_" + System.nanoTime()).build());

        Usuario usuario = usuarioRepo.save(Usuario.builder()
                .nombreCompleto("Usuario Multirol")
                .correo("multirol." + System.nanoTime() + "@test.com")
                .clave("ClaveMultirol1")
                .roles(List.of(rolBase, rolAdmin))
                .build());

        Usuario recuperado = usuarioRepo.findById(usuario.getId()).orElse(null);

        assertNotNull(recuperado);
        assertEquals(2, recuperado.getRoles().size());
    }

    @Test
    public void testBorrarTareaNoBorraUsuario() {
        Usuario usuario = usuarioRepo.save(Usuario.builder()
                .nombreCompleto("Usuario Persistente")
                .correo("persistente." + System.nanoTime() + "@test.com")
                .clave("ClavePersist1")
                .build());

        Tarea tarea = tareaRepo.save(Tarea.builder()
                .nombre("Tarea temporal")
                .estado(10)
                .usuario(usuario)
                .build());

        tareaRepo.deleteById(tarea.getId());

        assertFalse(tareaRepo.findById(tarea.getId()).isPresent());
        assertTrue(usuarioRepo.findById(usuario.getId()).isPresent()); // el usuario sigue existiendo
    }
}


