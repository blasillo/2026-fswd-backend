package es.jcyl.eclap.spring.backend.servicios.impl;

import es.jcyl.eclap.spring.backend.dto.TareaDto;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Tarea;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Usuario;
import es.jcyl.eclap.spring.backend.servicios.TareaMapeo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("manual")
public class TareaMapeoManual implements TareaMapeo {

    @Override
    public Tarea deDtoAEntidad(TareaDto modelo, Usuario usuario) {
        return Tarea.builder()
                .id(modelo.getId())
                .nombre(modelo.getNombre())
                .estado(modelo.getEstado())
                .color(modelo.getColor())
                .usuario(usuario)
                .build();
    }

    @Override
    public TareaDto deEntidadADto(Tarea tarea) {
        return TareaDto.builder()
                .id(tarea.getId())
                .nombre(tarea.getNombre())
                .estado(tarea.getEstado())
                .color(tarea.getColor())
                .usuarioCorreo(tarea.getUsuario().getCorreo())
                .build();
    }
}
