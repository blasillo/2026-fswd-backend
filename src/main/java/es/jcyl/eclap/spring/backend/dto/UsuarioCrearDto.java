package es.jcyl.eclap.spring.backend.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioCrearDto {
    private String  nombreCompleto;
    private String  iniciales;
    private String  correo;
    private String  clave;
    private String[] roles;
}
