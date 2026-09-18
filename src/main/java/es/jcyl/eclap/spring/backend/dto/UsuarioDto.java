package es.jcyl.eclap.spring.backend.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDto {

    private Integer id;
    private String  nombreCompleto;
    private String  iniciales;
    private String  correo;
    private String[] roles;
}