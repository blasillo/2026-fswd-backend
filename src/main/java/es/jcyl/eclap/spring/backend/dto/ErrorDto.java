package es.jcyl.eclap.spring.backend.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDto {
    private LocalDateTime fecha;
    private int estadoHttp;
    private String error;
    private String mensaje;
    private String ruta;

    // Solo se rellena en validación (@Valid)  campo y mensaje de la anotación que falló.
    private Map<String, String> camposConError;
}
