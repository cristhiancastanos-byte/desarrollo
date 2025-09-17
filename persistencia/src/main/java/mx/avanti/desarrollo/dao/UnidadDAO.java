package mx.avanti.desarrollo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import mx.desarrollo.entity.Asignacion;
import mx.desarrollo.entity.Unidad;

import java.util.List;

public class UnidadDAO {

    public Unidad findById(Integer id) {
        if (id == null) return null;
        EntityManager em = JpaUtil.em();
        try {
            return em.find(Unidad.class, id);
        } finally {
            em.close();
        }
    }

    public List<Unidad> findByNombreLike(String filtro) {
        EntityManager em = JpaUtil.em();
        try {
            String f = filtro == null ? "" : filtro.trim();
            TypedQuery<Unidad> q = em.createQuery(
                    "select u from Unidad u where u.nombre like :f order by u.nombre asc",
                    Unidad.class
            );
            q.setParameter("f", "%" + f + "%");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public void save(Unidad u) {
        EntityManager em = JpaUtil.em();
        try {
            em.getTransaction().begin();
            if (u.getId() == null) {
                em.persist(u);
            } else {
                em.merge(u);
            }
            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public void delete(Integer id) {
        EntityManager em = JpaUtil.em();
        try {
            em.getTransaction().begin();
            Unidad ref = em.find(Unidad.class, id);
            if (ref != null) em.remove(ref);
            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public long countAsignaciones(Integer idUnidad) {
        EntityManager em = JpaUtil.em();
        try {
            TypedQuery<Long> q = em.createQuery(
                    "select count(a) from Asignacion a where a.unidad.id = :id",
                    Long.class
            );
            q.setParameter("id", idUnidad);
            return q.getSingleResult();
        } finally {
            em.close();
        }
    }
}
