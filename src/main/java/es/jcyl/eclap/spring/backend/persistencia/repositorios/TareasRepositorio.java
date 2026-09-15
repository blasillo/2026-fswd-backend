package es.jcyl.eclap.spring.backend.persistencia.repositorios;

import es.jcyl.eclap.spring.backend.persistencia.entidades.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TareasRepositorio extends JpaRepository<Tarea,Integer> {
}
