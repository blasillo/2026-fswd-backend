package es.jcyl.eclap.spring.backend.dto;


import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TareaDto {

    private Integer id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 5, max = 200, message = "El nombre debe tener entre 5 y 200 caracteres")
    private String  nombre;

    @Min(value = 0, message = "El estado no puede ser menor que 0")
    @Max(value=100, message = "El estado no puede ser mayor que 100")
    private Integer estado;

    @Size (max = 50)
    @Pattern(regexp = "Azul|Verde|Amarillo|Morado|Naranja|Rojo|Gris",
            message = "El color debe ser uno de: Azul, Verde, Amarillo, Morado, Naranja, Rojo, Gris")
    private String  color;

    @Email
    @NotBlank(message = "El correo es obligatorio")
    private String  usuarioCorreo;
}
