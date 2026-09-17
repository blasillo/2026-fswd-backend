package es.jcyl.eclap.spring.backend.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TareaDto {

    private Integer id;
    private String  nombre;
    private Integer estado;
    private String  color;
    private String  usuarioCorreo;
}
