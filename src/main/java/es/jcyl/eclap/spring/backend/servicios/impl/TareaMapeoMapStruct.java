package es.jcyl.eclap.spring.backend.servicios.impl;


import es.jcyl.eclap.spring.backend.dto.TareaDto;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Tarea;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Usuario;
import es.jcyl.eclap.spring.backend.servicios.TareaMapeo;
import lombok.RequiredArgsConstructor;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Service
@Qualifier("mapstruct")
@RequiredArgsConstructor
public class TareaMapeoMapStruct implements TareaMapeo {

    private final TareaEntidadDtoGenerado mapperGenerado;

    @Override
    public Tarea deDtoAEntidad(TareaDto modelo, Usuario usuario) {
        return mapperGenerado.deDtoAEntidad(modelo, usuario);
    }

    @Override
    public TareaDto deEntidadADto(Tarea tarea) {
        return mapperGenerado.deEntidadADto(tarea);
    }

    // Mapper real, generado por MapStruct al compilar (paquete-privado:
    // nadie fuera de esta clase debe usarlo directamente, se usa TareaMapeo).
    @Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
    interface TareaEntidadDtoGenerado {

        @Mapping(target = "id", source = "modelo.id")
        @Mapping(target = "usuario", source = "usuario")
        Tarea deDtoAEntidad(TareaDto modelo, Usuario usuario);

        @Mapping(target = "usuarioCorreo", source = "usuario.correo")
        TareaDto deEntidadADto(Tarea tarea);
    }
}
