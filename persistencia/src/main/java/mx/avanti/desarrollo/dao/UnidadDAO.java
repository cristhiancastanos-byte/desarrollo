package mx.avanti.desarrollo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import mx.desarrollo.entity.Unidad;

import java.util.List;

public class UnidadDAO {

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

    public Unidad findById(Integer id) {
        EntityManager em = JpaUtil.em();
        try {
            return em.find(Unidad.class, id);
        } finally {
            em.close();
        }
    }

    public List<Unidad> findAll() {
        EntityManager em = JpaUtil.em();
        try {
            TypedQuery<Unidad> q = em.createQuery(
                    "select u from Unidad u order by u.nombre asc", Unidad.class
            );
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Unidad> searchByNombre(String qStr) {
        EntityManager em = JpaUtil.em();
        try {
            TypedQuery<Unidad> q = em.createQuery(
                    "select u from Unidad u " +
                            "where lower(u.nombre) like :q " +
                            "order by u.nombre asc", Unidad.class
            );
            q.setParameter("q", "%" + qStr.toLowerCase() + "%");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
