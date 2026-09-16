package es.jcyl.eclap.spring.backend.persistencia.repositorios;

import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import es.jcyl.eclap.spring.backend.persistencia.entidades.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TareasRepositorioImpl implements TareasRepositorioPersonalizado {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Tarea> buscarTareasPorRolYEstadoMinimo(String nombreRol, Integer estadoMinimo) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tarea> cq = cb.createQuery(Tarea.class);

        Root<Tarea> tarea = cq.from(Tarea.class);
        Join<Tarea, Usuario> usuario = tarea.join("usuario");
        Join<Usuario, Rol> rol = usuario.join("roles");

        Predicate predicadoRol = cb.equal(rol.get("nombre"), nombreRol);
        Predicate predicadoEstado = cb.greaterThanOrEqualTo(tarea.get("estado"), estadoMinimo);

        cq.select(tarea)
                .where(cb.and(predicadoRol, predicadoEstado))
                .distinct(true);

        return em.createQuery(cq).getResultList();
    }


    @Override
    public Map<Usuario, List<Tarea>> obtenerTareasConUsuarioYRoles() {
        // NOTA: .transform(GroupBy...) de QueryDSL 5.1.0 es incompatible con
        // Hibernate 7.x (bug conocido, sin arreglar: querydsl/querydsl#3819,
        // ScrollableResults.get(int) fue eliminado desde Hibernate 6).
        // Por eso se agrupa manualmente en Java tras traer la lista plana.
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        QTarea tarea = QTarea.tarea;
        QUsuario usuario = QUsuario.usuario;
        QRol rol = QRol.rol;

        List<Tarea> tareas = queryFactory
                .selectFrom(tarea)
                .innerJoin(tarea.usuario, usuario).fetchJoin()
                .innerJoin(usuario.roles, rol).fetchJoin()
                .distinct()
                .fetch();

        return tareas.stream()
                .collect(Collectors.groupingBy(
                        Tarea::getUsuario,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }
}


