package es.jcyl.eclap.spring.backend.servicios.impl;


import es.jcyl.eclap.spring.backend.dto.UsuarioCrearDto;
import es.jcyl.eclap.spring.backend.dto.UsuarioDto;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Rol;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Usuario;
import es.jcyl.eclap.spring.backend.servicios.UsuarioMapeo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioMapeoImpl  implements UsuarioMapeo {

    @Override
    public Usuario deCrearDtoAEntidad(UsuarioCrearDto modelo, List<Rol> roles) {
        return Usuario.builder()
                .nombreCompleto(modelo.getNombreCompleto())
                .iniciales(modelo.getIniciales())
                .correo(modelo.getCorreo())
                .clave(modelo.getClave())
                .roles(roles)
                .build();
    }

    @Override
    public void actualizarEntidadDesdeDto(UsuarioDto modelo, List<Rol> roles, Usuario existente) {
        existente.setNombreCompleto(modelo.getNombreCompleto());
        existente.setIniciales(modelo.getIniciales());
        existente.setCorreo(modelo.getCorreo());
        existente.setRoles(roles);
        // clave NO se toca aquí a propósito
    }

    @Override
    public UsuarioDto deEntidadADto(Usuario usuario) {
        String[] nombresRoles = usuario.getRoles() == null
                ? new String[0]
                : usuario.getRoles().stream()
                .map(Rol::getNombre)
                .toArray(String[]::new);

        return UsuarioDto.builder()
                .id(usuario.getId())
                .nombreCompleto(usuario.getNombreCompleto())
                .iniciales(usuario.getIniciales())
                .correo(usuario.getCorreo())
                .roles(nombresRoles)
                .build();
    }
}
