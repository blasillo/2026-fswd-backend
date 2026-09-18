package es.jcyl.eclap.spring.backend;



import es.jcyl.eclap.spring.backend.dto.UsuarioCrearDto;
import es.jcyl.eclap.spring.backend.dto.UsuarioDto;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Usuario;
import es.jcyl.eclap.spring.backend.persistencia.repositorios.UsuariosRepositorio;
import es.jcyl.eclap.spring.backend.servicios.UsuarioMapeo;
import es.jcyl.eclap.spring.backend.servicios.UsuarioServicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
public class UsuarioServicioTest {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private UsuariosRepositorio usuariosRepo;

    @Autowired
    private UsuarioMapeo mapeo;

    @Test
    public void testAgregarRolAdministradorAUnUsuario() {
        // 1. Buscar un usuario que todavía NO tenga el rol ADMINISTRADOR
        UsuarioCrearDto nuevoUsuario = UsuarioCrearDto.builder()
                .nombreCompleto("Usuario Test Sin Admin")
                .correo("test.sinadmin." + System.nanoTime() + "@correo.com")
                .clave("ClaveTest123")
                .roles(new String[]{"BASE"})
                .build();

        UsuarioDto dtoCreado = usuarioServicio.crearUsuario(nuevoUsuario);

        Usuario usuarioSinAdmin = usuariosRepo.findById(dtoCreado.getId())
                .orElseThrow(() -> new IllegalStateException("El usuario recién creado no se encuentra"));

        List<String> rolesActuales = usuarioSinAdmin.getRoles() == null
                ? new ArrayList<>()
                : usuarioSinAdmin.getRoles().stream().map(r -> r.getNombre()).toList();

        assertFalse(rolesActuales.contains("ADMINISTRADOR"),
                "Precondición: el usuario elegido no debía tener ya el rol");

        // 2. Construir el DTO añadiendo ADMINISTRADOR a los roles que ya tenía
        List<String> nuevosRoles = new ArrayList<>(rolesActuales);
        nuevosRoles.add("ADMINISTRADOR");

        UsuarioDto dtoModificacion = mapeo.deEntidadADto(usuarioSinAdmin);
        dtoModificacion.setRoles(nuevosRoles.toArray(new String[0]));

        assertEquals(usuarioSinAdmin.getCorreo(), dtoModificacion.getCorreo());

        // 3. Llamar al servicio real (que usa UsuarioMapeo de verdad, sin mocks)
        UsuarioDto resultado = usuarioServicio.modificarUsuario(dtoModificacion);

        // 4. Comprobar la respuesta del servicio
        assertTrue(Arrays.asList(resultado.getRoles()).contains("ADMINISTRADOR"),
                "El DTO devuelto por el servicio debería incluir ADMINISTRADOR");

        // 5. Comprobar que se actualizó DE VERDAD en la base de datos,
        // recargando la entidad desde cero (no reutilizamos la instancia en memoria)
        Optional<Usuario> usuarioRecargado = usuariosRepo.findById(usuarioSinAdmin.getId());

        assertTrue(usuarioRecargado.isPresent());
        boolean tieneAdminEnBd = usuarioRecargado.get().getRoles().stream()
                .anyMatch(r -> r.getNombre().equals("ADMINISTRADOR"));

        assertTrue(tieneAdminEnBd,
                "El usuario " + usuarioSinAdmin.getId() + " debería tener ADMINISTRADOR en la base de datos");
    }
}
