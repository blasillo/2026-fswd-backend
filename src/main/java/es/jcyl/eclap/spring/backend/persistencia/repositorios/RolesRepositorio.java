package es.jcyl.eclap.spring.backend.persistencia.repositorios;

import es.jcyl.eclap.spring.backend.persistencia.entidades.Rol;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RolesRepositorio extends JpaRepository<Rol,Integer> {

    // TODO : consultar todos los usuarios de un rol
    @Query("""
            SELECT u 
              FROM Usuario u JOIN u.roles r 
              WHERE r.nombre = :nombreRol
            """)
    List<Usuario> listadoUsuariosPorRol (String nombreRol);

    // TODO: contar cuántos usuarios tienen un rol concreto
    @Query("SELECT COUNT(u) FROM Rol r JOIN r.usuarios u WHERE r.nombre = :nombreRol")
    Long contarUsuariosPorRol(@Param("nombreRol") String nombreRol);

    // TODO: Native query: mismo resultado, pero contando sobre la tabla intermedia
    @Query(value = """
            SELECT COUNT(*)
              FROM app_usuarios_roles ur, app_roles r
             WHERE r.id_rol = ur.id_rol
               AND r.nombre_rol = :nombreRol
            """, nativeQuery = true)
    long contarUsuariosPorRolNative(@Param("nombreRol") String nombreRol);


    // añadido para servicio
    List<Rol> findByNombreIn(List<String> nombres);
}
