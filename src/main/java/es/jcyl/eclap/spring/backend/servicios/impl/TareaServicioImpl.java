package es.jcyl.eclap.spring.backend.servicios.impl;


import es.jcyl.eclap.spring.backend.dto.TareaDto;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Tarea;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Usuario;
import es.jcyl.eclap.spring.backend.persistencia.repositorios.TareasRepositorio;
import es.jcyl.eclap.spring.backend.persistencia.repositorios.UsuariosRepositorio;
import es.jcyl.eclap.spring.backend.servicios.TareaMapeo;
import es.jcyl.eclap.spring.backend.servicios.TareaServicio;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TareaServicioImpl implements TareaServicio {

    private final TareasRepositorio tareasRepo;
    private final UsuariosRepositorio usuariosRepo;
    private final TareaMapeo mapeo;

    public TareaServicioImpl(
            TareasRepositorio tareasRepo,
            UsuariosRepositorio usuariosRepo,
            @Qualifier("manual") TareaMapeo mapeo) {
        this.tareasRepo = tareasRepo;
        this.usuariosRepo = usuariosRepo;
        this.mapeo = mapeo;
    }

    @Override
    public TareaDto crearTarea(TareaDto modelo) {
        var usuario = usuariosRepo.findByCorreo( modelo.getUsuarioCorreo())
                .orElseThrow( () ->  new EntityNotFoundException("El usuario no existe"));

        Tarea nueva =  tareasRepo.save (  mapeo.deDtoAEntidad( modelo , usuario )  );
        return mapeo.deEntidadADto( nueva ) ;
    }

    @Override
    public Page<TareaDto> obtenerTareas(String correo, Pageable pageable) {
        Optional<Usuario> usuario = usuariosRepo.findByCorreo(correo);
        if (usuario.isEmpty()) {
            throw new EntityNotFoundException("El usuario no existe");
        }

        Page<Tarea> tareas = tareasRepo.findByUsuarioId(usuario.get().getId(), pageable);

        return tareas.map(mapeo::deEntidadADto);
    }

    @Override
    public TareaDto modificarTarea(TareaDto modelo) {
        Optional<Tarea> tarea = tareasRepo.findById(modelo.getId());
        if(tarea.isEmpty()) {
            throw new EntityNotFoundException("La tarea no existe");
        }
        Optional<Usuario> usuario = usuariosRepo.findByCorreo(modelo.getUsuarioCorreo());
        if(usuario.isEmpty()) {
            throw new EntityNotFoundException("El usuario no existe");
        }
        return mapeo.deEntidadADto( tareasRepo.save( mapeo.deDtoAEntidad (modelo,usuario.get()) ) );
    }

    @Override
    public Integer borrarTarea(Integer tareaId) {
        Optional<Tarea> tarea = tareasRepo.findById(tareaId);

        if(tarea.isEmpty()) {
            throw new EntityNotFoundException("La tarea no existe");
        }
        tarea.ifPresent(tareasRepo::delete);
        return tareaId;
    }
}
