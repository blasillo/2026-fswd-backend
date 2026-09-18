package es.jcyl.eclap.spring.backend.dto;

import es.jcyl.eclap.spring.backend.dto.validacion.RolesExistentes;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioCrearDto {

    @Size(max = 200)
    private String  nombreCompleto;

    @Size(max = 10)
    private String  iniciales;

    @Email
    @NotBlank(message = "El correo es obligatorio")
    @Size(max = 100)
    private String  correo;

    @NotBlank(message = "La clave es obligatoria")
    @Size(min = 6, max = 100, message = "La clave debe tener entre 6 y 100 caracteres")
    private String  clave;

    @RolesExistentes
    private String[] roles;
}
