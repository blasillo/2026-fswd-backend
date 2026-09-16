package es.jcyl.eclap.spring.backend.persistencia.repositorios;

import es.jcyl.eclap.spring.backend.persistencia.entidades.Tarea;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Usuario;

import java.util.List;
import java.util.Map;

public interface TareasRepositorioPersonalizado {

    List<Tarea> buscarTareasPorRolYEstadoMinimo(String nombreRol, Integer estadoMinimo);

    Map<Usuario, List<Tarea>> obtenerTareasConUsuarioYRoles();
}
