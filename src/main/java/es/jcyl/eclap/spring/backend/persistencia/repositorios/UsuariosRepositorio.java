package es.jcyl.eclap.spring.backend.persistencia.repositorios;

import es.jcyl.eclap.spring.backend.persistencia.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuariosRepositorio extends JpaRepository<Usuario,Integer> {
}
