package es.jcyl.eclap.spring.backend.persistencia.repositorios;

import es.jcyl.eclap.spring.backend.persistencia.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuariosRepositorio extends JpaRepository<Usuario,Integer> {

    //TODO
    Optional<Usuario> findByCorreo(String correo);


    // TODO: usuarios que no tienen ninguna tarea asignada
    @Query("SELECT u FROM Usuario u WHERE u.id NOT IN (SELECT DISTINCT t.usuario.id FROM Tarea t)")
    List<Usuario> obtenerUsuariosSinTareas();


    // JPQL: usuarios que tienen un rol concreto por nombre
    @Query("SELECT DISTINCT u FROM Usuario u JOIN u.roles r WHERE r.nombre = :nombreRol")
    List<Usuario> obtenerUsuariosPorRol(@Param("nombreRol") String nombreRol);


}
