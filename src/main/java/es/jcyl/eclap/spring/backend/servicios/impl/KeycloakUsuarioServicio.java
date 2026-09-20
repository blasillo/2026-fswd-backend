package es.jcyl.eclap.spring.backend.servicios.impl;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeycloakUsuarioServicio {
    private final Keycloak keycloakAdmin;

    @Value("${keycloak.admin.realm-gestionado}")
    private String realm;

    public KeycloakUsuarioServicio(Keycloak keycloakAdmin) {
        this.keycloakAdmin = keycloakAdmin;
    }

    private RealmResource realmResource() {
        return keycloakAdmin.realm(realm);
    }

    public String crearUsuario(String correo, String nombreCompleto, String clave, List<String> roles) {
        UserRepresentation usuarioKc = new UserRepresentation();
        usuarioKc.setUsername(correo);
        usuarioKc.setEmail(correo);
        usuarioKc.setFirstName(nombreCompleto);
        usuarioKc.setEnabled(true);
        usuarioKc.setEmailVerified(true);

        UsersResource usersResource = realmResource().users();
        try (Response respuesta = usersResource.create(usuarioKc)) {
            if (respuesta.getStatus() != 201) {
                throw new IllegalStateException("No se ha podido crear el usuario en Keycloak (HTTP " + respuesta.getStatus() + ")");
            }
            String ubicacion = respuesta.getLocation().getPath();
            String idKeycloak = ubicacion.substring(ubicacion.lastIndexOf('/') + 1);

            establecerClave(idKeycloak, clave);
            asignarRoles(idKeycloak, roles);

            return idKeycloak;
        }
    }

    public void actualizarUsuario(String correo, String nombreCompleto, List<String> roles) {
        UserRepresentation usuarioKc = buscarPorCorreo(correo);
        usuarioKc.setFirstName(nombreCompleto);
        realmResource().users().get(usuarioKc.getId()).update(usuarioKc);
        asignarRoles(usuarioKc.getId(), roles);
    }

    public void borrarUsuario(String correo) {
        UserRepresentation usuarioKc = buscarPorCorreo(correo);
        realmResource().users().get(usuarioKc.getId()).remove();
    }

    private UserRepresentation buscarPorCorreo(String correo) {
        return realmResource().users().search(correo).stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado en Keycloak: " + correo));
    }

    private void establecerClave(String idKeycloak, String clave) {
        CredentialRepresentation credencial = new CredentialRepresentation();
        credencial.setType(CredentialRepresentation.PASSWORD);
        credencial.setValue(clave);
        credencial.setTemporary(false);
        realmResource().users().get(idKeycloak).resetPassword(credencial);
    }

    private void asignarRoles(String idKeycloak, List<String> nombresRoles) {
        if (nombresRoles == null || nombresRoles.isEmpty()) {
            return;
        }
        UserResource usuarioResource = realmResource().users().get(idKeycloak);

        List<RoleRepresentation> rolesDisponibles = realmResource().roles().list();
        List<RoleRepresentation> rolesAAsignar = rolesDisponibles.stream()
                .filter(rol -> nombresRoles.contains(rol.getName()))
                .toList();

        usuarioResource.roles().realmLevel().add(rolesAAsignar);
    }
}
