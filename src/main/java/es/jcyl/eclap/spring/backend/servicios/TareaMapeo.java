package es.jcyl.eclap.spring.backend.servicios;

import es.jcyl.eclap.spring.backend.dto.TareaDto;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Tarea;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Usuario;
import org.springframework.stereotype.Service;

public interface TareaMapeo {

    Tarea deDtoAEntidad(TareaDto modelo, Usuario usuario);
    TareaDto deEntidadADto(Tarea tarea);
}
