package es.jcyl.eclap.spring.backend.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TareaDto {
    @Schema(description = "Identificador de la tarea, se genera solo al crear", example = "9")
    private Integer id;

    @Schema(description = "Nombre descriptivo de la tarea", example = "Revisar documentación")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 5, max = 200, message = "El nombre debe tener entre 5 y 200 caracteres")
    private String  nombre;

    @Schema(description = "Progreso de la tarea, de 0 a 100", example = "50")
    @Min(value = 0, message = "El estado no puede ser menor que 0")
    @Max(value=100, message = "El estado no puede ser mayor que 100")
    private Integer estado;

    @Schema(description = "Color asociado a la tarea", example = "Azul")
    @Size (max = 50)
    @Pattern(regexp = "Azul|Verde|Amarillo|Morado|Naranja|Rojo|Gris",
            message = "El color debe ser uno de: Azul, Verde, Amarillo, Morado, Naranja, Rojo, Gris")
    private String  color;

    @Schema(description = "Correo del usuario propietario de la tarea", example = "formacion@correo.es")
    @Email
    @NotBlank(message = "El correo es obligatorio")
    private String  usuarioCorreo;
}
