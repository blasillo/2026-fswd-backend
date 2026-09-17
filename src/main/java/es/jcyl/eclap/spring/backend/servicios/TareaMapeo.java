package es.jcyl.eclap.spring.backend.servicios;

import es.jcyl.eclap.spring.backend.dto.TareaDto;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Tarea;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Usuario;
import org.springframework.stereotype.Service;

@Service
public class TareaMapeo {

    public Tarea deDtoAEntidad (TareaDto modelo, Usuario usuario) {

        return Tarea.builder()
                .id ( modelo.getId())
                .nombre( modelo.getNombre())
                .estado( modelo.getEstado())
                .color(modelo.getColor())
                .usuario( usuario )
                .build();
    }



    public TareaDto deEntidadADto (Tarea tarea) {

        return TareaDto.builder()
                .id ( tarea.getId())
                .nombre( tarea.getNombre())
                .estado( tarea.getEstado())
                .color( tarea.getColor())
                .usuarioCorreo( tarea.getUsuario().getCorreo())
                .build();
    }
}
