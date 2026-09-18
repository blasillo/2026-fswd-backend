package es.jcyl.eclap.spring.backend.servicios;

import es.jcyl.eclap.spring.backend.dto.UsuarioCrearDto;
import es.jcyl.eclap.spring.backend.dto.UsuarioDto;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Rol;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Usuario;

import java.util.List;

public interface UsuarioMapeo {

    // Creación: lleva clave, porque es obligatoria en la base de datos.
    Usuario deCrearDtoAEntidad(UsuarioCrearDto modelo, List<Rol> roles);

    // Modificación: actualiza SOLO los campos del DTO sobre un Usuario ya
    // cargado (no crea uno nuevo), para no pisar la clave existente con
    // null (UsuarioDto de lectura no lleva clave).
    void actualizarEntidadDesdeDto(UsuarioDto modelo, List<Rol> roles, Usuario existente);

    // Lectura: nunca incluye la clave.
    UsuarioDto deEntidadADto(Usuario usuario);
}
