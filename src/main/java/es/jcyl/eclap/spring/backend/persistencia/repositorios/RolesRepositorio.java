package es.jcyl.eclap.spring.backend.persistencia.repositorios;

import es.jcyl.eclap.spring.backend.persistencia.entidades.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepositorio extends JpaRepository<Rol,Integer> {
}
