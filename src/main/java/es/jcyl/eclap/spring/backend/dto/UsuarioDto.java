package es.jcyl.eclap.spring.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDto {

    @NotNull(message = "El id es obligatorio")
    private Integer id;

    @Size(max = 200)
    private String  nombreCompleto;

    @Size(max = 10)
    private String  iniciales;

    @NotBlank(message = "El correo es obligatorio")
    @Email
    @Size(max = 100)
    private String  correo;

    private String[] roles;
}