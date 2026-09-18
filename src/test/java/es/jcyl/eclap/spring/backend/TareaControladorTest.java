package es.jcyl.eclap.spring.backend;


import es.jcyl.eclap.spring.backend.dto.TareaDto;
import tools.jackson.databind.ObjectMapper;
import es.jcyl.eclap.spring.backend.servicios.TareaServicio;
import es.jcyl.eclap.spring.backend.controladores.TareaControlador;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TareaControlador.class)
public class TareaControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TareaServicio servicio;

    private TareaDto dtoValido() {
        return TareaDto.builder()
                .id(1)
                .nombre("Tarea válida")
                .estado(50)
                .color("Azul")
                .usuarioCorreo("test@correo.com")
                .build();
    }

    // ============================================================
    // POST /tareas
    // ============================================================

    @Test
    public void testNuevaTarea_datosValidos_devuelve200() throws Exception {
        TareaDto dto = dtoValido();
        when(servicio.crearTarea(any(TareaDto.class))).thenReturn(dto);

        mockMvc.perform(post("/tareas")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Tarea válida"));
    }

    @Test
    public void testNuevaTarea_datosInvalidos_devuelve400() throws Exception {
        TareaDto invalido = dtoValido();
        invalido.setNombre("Ho"); // menos de 5 caracteres, viola @Size(min=5)

        mockMvc.perform(post("/tareas")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    // ============================================================
    // GET /tareas (paginado)
    // ============================================================

    @Test
    public void testListadoTareas_devuelvePaginaDeTareas() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TareaDto> pagina = new PageImpl<>(List.of(dtoValido()), pageable, 1);

        when(servicio.obtenerTareas(anyString(), any(Pageable.class))).thenReturn(pagina);

        mockMvc.perform(get("/tareas")
                        .param("correo", "test@correo.com")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nombre").value("Tarea válida"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    // ============================================================
    // PUT /tareas
    // ============================================================

    @Test
    public void testEditarTarea_datosValidos_devuelve200() throws Exception {
        TareaDto dto = dtoValido();
        when(servicio.modificarTarea(any(TareaDto.class))).thenReturn(dto);

        mockMvc.perform(put("/tareas")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testEditarTarea_datosInvalidos_devuelve400() throws Exception {
        TareaDto invalido = dtoValido();
        invalido.setUsuarioCorreo("no-es-un-correo"); // viola @Email

        mockMvc.perform(put("/tareas")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    // ============================================================
    // DELETE /tareas
    // ============================================================

    @Test
    public void testBorrarTarea_devuelveElIdBorrado() throws Exception {
        when(servicio.borrarTarea(anyInt())).thenReturn(1);

        mockMvc.perform(delete("/tareas").param("tareaId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(servicio).borrarTarea(1);
    }
}
