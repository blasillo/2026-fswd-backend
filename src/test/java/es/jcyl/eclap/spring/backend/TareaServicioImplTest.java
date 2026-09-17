package es.jcyl.eclap.spring.backend;

import es.jcyl.eclap.spring.backend.dto.TareaDto;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Tarea;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Usuario;
import es.jcyl.eclap.spring.backend.persistencia.repositorios.TareasRepositorio;
import es.jcyl.eclap.spring.backend.persistencia.repositorios.UsuariosRepositorio;
import es.jcyl.eclap.spring.backend.servicios.TareaMapeo;
import es.jcyl.eclap.spring.backend.servicios.TareaServicioImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TareaServicioImplTest {
    @Mock
    private TareasRepositorio tareasRepo;

    @Mock
    private UsuariosRepositorio usuariosRepo;

    @Mock
    private TareaMapeo mapeo;

    private TareaServicioImpl servicio;

    @BeforeEach
    public void setUp() {
        servicio = new TareaServicioImpl(tareasRepo, usuariosRepo, mapeo);
    }

    private Usuario usuarioDePrueba() {
        return Usuario.builder()
                .id(1)
                .correo("test@correo.com")
                .clave("ClaveTest1")
                .build();
    }

    private Tarea tareaDePrueba(Usuario usuario) {
        return Tarea.builder()
                .id(10)
                .nombre("Tarea de prueba")
                .estado(50)
                .color("Azul")
                .usuario(usuario)
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

    // ============================================================
    // crearTarea
    // ============================================================

    @Test
    public void testCrearTarea_usuarioExiste_creaLaTarea() {
        Usuario usuario = usuarioDePrueba();
        Tarea tareaSinGuardar = tareaDePrueba(usuario);
        Tarea tareaGuardada = tareaDePrueba(usuario);
        TareaDto dtoEntrada = dtoDePrueba();
        TareaDto dtoSalida = dtoDePrueba();

        when(usuariosRepo.findByCorreo("test@correo.com")).thenReturn(Optional.of(usuario));
        when(mapeo.deDtoAEntidad(dtoEntrada, usuario)).thenReturn(tareaSinGuardar);
        when(tareasRepo.save(tareaSinGuardar)).thenReturn(tareaGuardada);
        when(mapeo.deEntidadADto(tareaGuardada)).thenReturn(dtoSalida);

        TareaDto resultado = servicio.crearTarea(dtoEntrada);

        assertEquals(dtoSalida, resultado);
        verify(tareasRepo).save(tareaSinGuardar);
    }

    @Test
    public void testCrearTarea_usuarioNoExiste_lanzaExcepcion() {
        TareaDto dto = dtoDePrueba();
        when(usuariosRepo.findByCorreo("test@correo.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> servicio.crearTarea(dto));

        verify(tareasRepo, never()).save(any());
    }

    // ============================================================
    // obtenerTareas (paginado)
    // ============================================================

    @Test
    public void testObtenerTareas_usuarioExiste_devuelvePaginaDeDtos() {
        Usuario usuario = usuarioDePrueba();
        Tarea tarea = tareaDePrueba(usuario);
        TareaDto dto = dtoDePrueba();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Tarea> paginaEntidades = new PageImpl<>(List.of(tarea), pageable, 1);

        when(usuariosRepo.findByCorreo("test@correo.com")).thenReturn(Optional.of(usuario));
        when(tareasRepo.findByUsuarioId(usuario.getId(), pageable)).thenReturn(paginaEntidades);
        when(mapeo.deEntidadADto(tarea)).thenReturn(dto);

        Page<TareaDto> resultado = servicio.obtenerTareas("test@correo.com", pageable);

        assertEquals(1, resultado.getTotalElements());
        assertEquals(dto, resultado.getContent().get(0));
    }

    @Test
    public void testObtenerTareas_usuarioNoExiste_lanzaExcepcion() {
        Pageable pageable = PageRequest.of(0, 10);
        when(usuariosRepo.findByCorreo("noexiste@correo.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> servicio.obtenerTareas("noexiste@correo.com", pageable));
    }

    // ============================================================
    // modificarTarea
    // ============================================================

    @Test
    public void testModificarTarea_existeTareaYUsuario_modifica() {
        Usuario usuario = usuarioDePrueba();
        Tarea tareaExistente = tareaDePrueba(usuario);
        Tarea tareaModificada = tareaDePrueba(usuario);
        TareaDto dtoEntrada = dtoDePrueba();
        TareaDto dtoSalida = dtoDePrueba();

        when(tareasRepo.findById(10)).thenReturn(Optional.of(tareaExistente));
        when(usuariosRepo.findByCorreo("test@correo.com")).thenReturn(Optional.of(usuario));
        when(mapeo.deDtoAEntidad(dtoEntrada, usuario)).thenReturn(tareaModificada);
        when(tareasRepo.save(tareaModificada)).thenReturn(tareaModificada);
        when(mapeo.deEntidadADto(tareaModificada)).thenReturn(dtoSalida);

        TareaDto resultado = servicio.modificarTarea(dtoEntrada);

        assertEquals(dtoSalida, resultado);
    }

    @Test
    public void testModificarTarea_tareaNoExiste_lanzaExcepcion() {
        TareaDto dto = dtoDePrueba();
        when(tareasRepo.findById(10)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> servicio.modificarTarea(dto));
    }

    @Test
    public void testModificarTarea_usuarioNoExiste_lanzaExcepcion() {
        Usuario usuario = usuarioDePrueba();
        Tarea tareaExistente = tareaDePrueba(usuario);
        TareaDto dto = dtoDePrueba();

        when(tareasRepo.findById(10)).thenReturn(Optional.of(tareaExistente));
        when(usuariosRepo.findByCorreo("test@correo.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> servicio.modificarTarea(dto));
    }

    // ============================================================
    // borrarTarea
    // ============================================================

    @Test
    public void testBorrarTarea_existe_borraYDevuelveId() {
        Usuario usuario = usuarioDePrueba();
        Tarea tarea = tareaDePrueba(usuario);
        when(tareasRepo.findById(10)).thenReturn(Optional.of(tarea));

        Integer resultado = servicio.borrarTarea(10);

        assertEquals(10, resultado);
        verify(tareasRepo).delete(tarea);
    }

    @Test
    public void testBorrarTarea_noExiste_lanzaExcepcion() {
        when(tareasRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> servicio.borrarTarea(99));

        verify(tareasRepo, never()).delete(any());
    }

}
