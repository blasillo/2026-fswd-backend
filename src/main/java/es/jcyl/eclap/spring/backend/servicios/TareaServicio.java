package es.jcyl.eclap.spring.backend.servicios;

import es.jcyl.eclap.spring.backend.dto.TareaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TareaServicio {

    TareaDto crearTarea (TareaDto tarea );
    Page<TareaDto> obtenerTareas(String email, Pageable pageable);

    TareaDto modificarTarea (TareaDto tarea);
    Integer borrarTarea (Integer tareaId);
}
