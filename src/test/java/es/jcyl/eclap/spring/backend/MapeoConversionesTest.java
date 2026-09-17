package es.jcyl.eclap.spring.backend;

import es.jcyl.eclap.spring.backend.dto.TareaDto;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Tarea;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Usuario;
import es.jcyl.eclap.spring.backend.servicios.TareaMapeo;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class MapeoConversionesTest {

    @Autowired
    @Qualifier("manual")
    private TareaMapeo mapeoManual;

    @Autowired
    @Qualifier("mapstruct")
    private TareaMapeo mapeoMapStruct;

    private Usuario usuarioDePrueba() {
        return Usuario.builder()
                .id(1)
                .nombreCompleto("Usuario Test")
                .correo("test@correo.com")
                .clave("ClaveTest1")
                .build();
    }

    private TareaDto dtoDePrueba() {
        return TareaDto.builder()
                .id(10)
                .nombre("Tarea de prueba")
                .estado(50)
                .color("Azul")
                .usuarioCorreo("test@correo.com")
                .build();
    }

    private Tarea entidadDePrueba(Usuario usuario) {
        return Tarea.builder()
                .id(10)
                .nombre("Tarea de prueba")
                .estado(50)
                .color("Azul")
                .usuario(usuario)
                .build();
    }

    // ============================================================
    // Manual
    // ============================================================

    @Test
    public void testManual_deDtoAEntidad() {
        Usuario usuario = usuarioDePrueba();
        TareaDto dto = dtoDePrueba();

        Tarea tarea = mapeoManual.deDtoAEntidad(dto, usuario);

        assertNotNull(tarea);
        assertEquals(dto.getId(), tarea.getId());
        assertEquals(dto.getNombre(), tarea.getNombre());
        assertEquals(dto.getEstado(), tarea.getEstado());
        assertEquals(dto.getColor(), tarea.getColor());
        assertEquals(usuario, tarea.getUsuario());
    }

    @Test
    public void testManual_deEntidadADto() {
        Usuario usuario = usuarioDePrueba();
        Tarea tarea = entidadDePrueba(usuario);

        TareaDto dto = mapeoManual.deEntidadADto(tarea);

        assertNotNull(dto);
        assertEquals(tarea.getId(), dto.getId());
        assertEquals(tarea.getNombre(), dto.getNombre());
        assertEquals(tarea.getEstado(), dto.getEstado());
        assertEquals(tarea.getColor(), dto.getColor());
        assertEquals(usuario.getCorreo(), dto.getUsuarioCorreo());
    }

    // ============================================================
    // MapStruct
    // ============================================================

    @Test
    public void testMapStruct_deDtoAEntidad() {
        Usuario usuario = usuarioDePrueba();
        TareaDto dto = dtoDePrueba();

        Tarea tarea = mapeoMapStruct.deDtoAEntidad(dto, usuario);

        assertNotNull(tarea);
        assertEquals(dto.getId(), tarea.getId());
        assertEquals(dto.getNombre(), tarea.getNombre());
        assertEquals(dto.getEstado(), tarea.getEstado());
        assertEquals(dto.getColor(), tarea.getColor());
        assertEquals(usuario, tarea.getUsuario());
    }

    @Test
    public void testMapStruct_deEntidadADto() {
        Usuario usuario = usuarioDePrueba();
        Tarea tarea = entidadDePrueba(usuario);

        TareaDto dto = mapeoMapStruct.deEntidadADto(tarea);

        assertNotNull(dto);
        assertEquals(tarea.getId(), dto.getId());
        assertEquals(tarea.getNombre(), dto.getNombre());
        assertEquals(tarea.getEstado(), dto.getEstado());
        assertEquals(tarea.getColor(), dto.getColor());
        assertEquals(usuario.getCorreo(), dto.getUsuarioCorreo());
    }

    // ============================================================
    // Las dos implementaciones deben coincidir entre sí
    // ============================================================

    @Test
    public void testAmbasImplementacionesCoincidenEnDeDtoAEntidad() {
        Usuario usuario = usuarioDePrueba();
        TareaDto dto = dtoDePrueba();

        Tarea deManual = mapeoManual.deDtoAEntidad(dto, usuario);
        Tarea deMapStruct = mapeoMapStruct.deDtoAEntidad(dto, usuario);

        assertEquals(deManual.getId(), deMapStruct.getId());
        assertEquals(deManual.getNombre(), deMapStruct.getNombre());
        assertEquals(deManual.getEstado(), deMapStruct.getEstado());
        assertEquals(deManual.getColor(), deMapStruct.getColor());
        assertEquals(deManual.getUsuario(), deMapStruct.getUsuario());
    }

    @Test
    public void testAmbasImplementacionesCoincidenEnDeEntidadADto() {
        Usuario usuario = usuarioDePrueba();
        Tarea tarea = entidadDePrueba(usuario);

        TareaDto deManual = mapeoManual.deEntidadADto(tarea);
        TareaDto deMapStruct = mapeoMapStruct.deEntidadADto(tarea);

        assertEquals(deManual.getId(), deMapStruct.getId());
        assertEquals(deManual.getNombre(), deMapStruct.getNombre());
        assertEquals(deManual.getEstado(), deMapStruct.getEstado());
        assertEquals(deManual.getColor(), deMapStruct.getColor());
        assertEquals(deManual.getUsuarioCorreo(), deMapStruct.getUsuarioCorreo());
    }
}
