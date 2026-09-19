package es.jcyl.eclap.spring.backend;


import es.jcyl.eclap.spring.backend.config.WebConfig;
import es.jcyl.eclap.spring.backend.controladores.TareaControlador;
import es.jcyl.eclap.spring.backend.servicios.TareaServicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TareaControlador.class)
@Import(WebConfig.class)
public class GestorExcepcionJsonTest {

    private static final String RUTA_BASE = "/api/v1.0/tareas";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TareaServicio servicio;

    @Test
    public void testBodyAusente_devuelveErrorDtoCon400() throws Exception {
        mockMvc.perform(post(RUTA_BASE)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.estadoHttp").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.ruta").value(RUTA_BASE));
    }

    @Test
    public void testJsonMalFormado_devuelveErrorDtoCon400() throws Exception {
        String jsonConCeroInicial = """
                {
                  "nombre": "Tarea de prueba",
                  "estado": 020,
                  "color": "Azul",
                  "usuarioCorreo": "formacion@eclap.jcyl.es"
                }
                """;

        mockMvc.perform(post(RUTA_BASE)
                        .contentType("application/json")
                        .content(jsonConCeroInicial))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.estadoHttp").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.ruta").value(RUTA_BASE));
    }

    @Test
    public void testCorreoInvalido_devuelveErrorDtoCon400YCampoUsuarioCorreo() throws Exception {
        String jsonConCorreoInvalido = """
                {
                  "nombre": "Tarea de prueba",
                  "estado": "20",
                  "color": "Azul",
                  "usuarioCorreo": "formacion(at)eclap.jcyl.es"
                }
                """;

        mockMvc.perform(post(RUTA_BASE)
                        .contentType("application/json")
                        .content(jsonConCorreoInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.estadoHttp").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.ruta").value(RUTA_BASE))
                .andExpect(jsonPath("$.camposConError.usuarioCorreo").exists());
    }
}
