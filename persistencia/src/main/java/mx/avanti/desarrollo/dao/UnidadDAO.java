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
            if (u.getId() == null) em.persist(u);
            else em.merge(u);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Unidad findById(Integer id) {
        EntityManager em = JpaUtil.em();
        try { return em.find(Unidad.class, id); }
        finally { em.close(); }
    }

    public List<Unidad> findAll() {
        EntityManager em = JpaUtil.em();
        try {
            TypedQuery<Unidad> q = em.createQuery("select u from Unidad u order by u.nombre asc", Unidad.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
