package es.jcyl.eclap.spring.backend.servicios;

import es.jcyl.eclap.spring.backend.dto.UsuarioCrearDto;
import es.jcyl.eclap.spring.backend.dto.UsuarioDto;

import java.util.List;

public interface UsuarioServicio {

    UsuarioDto crearUsuario(UsuarioCrearDto usuario);
    List<UsuarioDto> obtenerUsuarios();
    UsuarioDto modificarUsuario(UsuarioDto usuario);
    Integer borrarUsuario(Integer usuarioId);
}
