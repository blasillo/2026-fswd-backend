package es.jcyl.eclap.spring.backend.persistencia.repositorios;

import es.jcyl.eclap.spring.backend.persistencia.entidades.Tarea;
import es.jcyl.eclap.spring.backend.persistencia.entidades.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TareasRepositorio extends JpaRepository<Tarea,Integer> {


    Page<Tarea> findByUsuarioId(Integer idUsuario, Pageable pageable);

    //TODO: tareas de un usuario concreto
    @Query("SELECT t FROM Tarea t WHERE t.usuario.id = :idUsuario")
    Page<Tarea> obtenerTareasPorUsuario(@Param("idUsuario") Integer idUsuario, Pageable pageable);

    // TODO: tareas pendientes (estado < 100), ordenadas por estado descendente
    @Query("SELECT t FROM Tarea t WHERE t.estado < 100 ORDER BY t.estado DESC")
    List<Tarea> obtenerTareasPendientes();

    // TODO: tareas completadas (estado = 100) de un usuario
    @Query("SELECT t FROM Tarea t WHERE t.usuario.id = :idUsuario AND t.estado = 100")
    List<Tarea> obtenerTareasCompletadasPorUsuario(@Param("idUsuario") Integer idUsuario);

    // TODO: contar tareas de un usuario
    @Query("SELECT COUNT(t) FROM Tarea t WHERE t.usuario.id = :idUsuario")
    long contarTareasPorUsuario(@Param("idUsuario") Integer idUsuario);


    // TODO: media de progreso (estado) de las tareas de un usuario
    @Query(value = "SELECT AVG(estado) FROM app_tareas WHERE id_usuario = :idUsuario", nativeQuery = true)
    Double calcularProgresoMedioPorUsuario(@Param("idUsuario") Integer idUsuario);



}
