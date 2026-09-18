package es.jcyl.eclap.spring.backend.servicios.impl;

import es.jcyl.eclap.spring.backend.dto.UsuarioCrearDto;
import es.jcyl.eclap.spring.backend.dto.UsuarioDto;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Rol;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Usuario;
import es.jcyl.eclap.spring.backend.persistencia.repositorios.RolesRepositorio;
import es.jcyl.eclap.spring.backend.persistencia.repositorios.UsuariosRepositorio;
import es.jcyl.eclap.spring.backend.servicios.UsuarioMapeo;
import es.jcyl.eclap.spring.backend.servicios.UsuarioServicio;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServicioImpl implements UsuarioServicio {

    private final UsuariosRepositorio usuariosRepo;
    private final RolesRepositorio rolesRepo;
    private final UsuarioMapeo mapeo;

    @Override
    public UsuarioDto crearUsuario(UsuarioCrearDto modelo) {
        List<Rol> roles = resolverRoles(modelo.getRoles());

        Usuario nuevo = usuariosRepo.save(mapeo.deCrearDtoAEntidad(modelo, roles));
        return mapeo.deEntidadADto(nuevo);
    }

    @Override
    public List<UsuarioDto> obtenerUsuarios() {
        return usuariosRepo.findAll().stream()
                .map(mapeo::deEntidadADto)
                .toList();
    }

    @Override
    public UsuarioDto modificarUsuario(UsuarioDto modelo) {
        Usuario existente = usuariosRepo.findById(modelo.getId())
                .orElseThrow(() -> new EntityNotFoundException("El usuario no existe"));

        List<Rol> roles = resolverRoles(modelo.getRoles());

        mapeo.actualizarEntidadDesdeDto(modelo, roles, existente);

        Usuario actualizado = usuariosRepo.save(existente);
        return mapeo.deEntidadADto(actualizado);
    }

    @Override
    public Integer borrarUsuario(Integer usuarioId) {
        Usuario usuario = usuariosRepo.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("El usuario no existe"));

        usuariosRepo.delete(usuario);
        return usuarioId;
    }

    private List<Rol> resolverRoles(String[] nombresRoles) {
        if (nombresRoles == null) {
            return List.of();
        }
        return rolesRepo.findByNombreIn(Arrays.asList(nombresRoles));
    }
}
