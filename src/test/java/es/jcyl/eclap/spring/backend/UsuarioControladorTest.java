package es.jcyl.eclap.spring.backend;

import es.jcyl.eclap.spring.backend.config.WebConfig;
import es.jcyl.eclap.spring.backend.controladores.UsuarioControlador;
import es.jcyl.eclap.spring.backend.dto.UsuarioCrearDto;
import es.jcyl.eclap.spring.backend.dto.UsuarioDto;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Rol;
import es.jcyl.eclap.spring.backend.persistencia.repositorios.RolesRepositorio;
import es.jcyl.eclap.spring.backend.servicios.UsuarioServicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioControlador.class)
@Import(WebConfig.class)
public class UsuarioControladorTest {

    private static final String RUTA_BASE = "/api/v1.0/usuarios";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioServicio servicio;

    @MockitoBean
    private RolesRepositorio rolesRepo;


    private UsuarioCrearDto crearDtoValido() {
        return UsuarioCrearDto.builder()
                .nombreCompleto("Usuario Válido")
                .iniciales("UV")
                .correo("valido@correo.com")
                .clave("ClaveValida1")
                .roles(new String[]{"BASE"})
                .build();
    }

    private UsuarioDto dtoValido() {
        return UsuarioDto.builder()
                .id(1)
                .nombreCompleto("Usuario Válido")
                .iniciales("UV")
                .correo("valido@correo.com")
                .roles(new String[]{"BASE"})
                .build();
    }

    private void mockearRolBaseComoExistente() {
        Rol base = Rol.builder().id(1).nombre("BASE").build();
        when(rolesRepo.findByNombreIn(List.of("BASE"))).thenReturn(List.of(base));
    }

    // ============================================================
    // POST /api/v1.0/usuarios
    // ============================================================

    @Test
    public void testNuevoUsuario_datosValidos_devuelve200() throws Exception {
        mockearRolBaseComoExistente();

        UsuarioCrearDto crearDto = crearDtoValido();
        UsuarioDto respuesta = dtoValido();
        when(servicio.crearUsuario(any(UsuarioCrearDto.class))).thenReturn(respuesta);

        mockMvc.perform(post(RUTA_BASE)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(crearDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("valido@correo.com"));
    }

    @Test
    public void testNuevoUsuario_datosInvalidos_devuelve400() throws Exception {
        mockearRolBaseComoExistente();

        UsuarioCrearDto invalido = crearDtoValido();
        invalido.setCorreo("no-es-un-correo"); // viola @Email

        mockMvc.perform(post(RUTA_BASE)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    // ============================================================
    // GET /api/v1.0/usuarios
    // ============================================================

    @Test
    public void testListadoUsuarios_devuelveLaLista() throws Exception {
        when(servicio.obtenerUsuarios()).thenReturn(List.of(dtoValido()));

        mockMvc.perform(get(RUTA_BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].correo").value("valido@correo.com"));
    }

    // ============================================================
    // PUT /api/v1.0/usuarios
    // ============================================================

    @Test
    public void testEditarUsuario_datosValidos_devuelve200() throws Exception {
        mockearRolBaseComoExistente();

        UsuarioDto dto = dtoValido();
        when(servicio.modificarUsuario(any(UsuarioDto.class))).thenReturn(dto);

        mockMvc.perform(put(RUTA_BASE)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testEditarUsuario_datosInvalidos_devuelve400() throws Exception {
        mockearRolBaseComoExistente();

        UsuarioDto invalido = dtoValido();
        invalido.setId(null); // viola @NotNull

        mockMvc.perform(put(RUTA_BASE)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    // ============================================================
    // DELETE /api/v1.0/usuarios
    // ============================================================

    @Test
    public void testBorrarUsuario_devuelveElIdBorrado() throws Exception {
        when(servicio.borrarUsuario(anyInt())).thenReturn(1);

        mockMvc.perform(delete(RUTA_BASE).param("usuarioId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(servicio).borrarUsuario(1);
    }
}
