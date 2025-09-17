package mx.avanti.desarrollo.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.Unidad;

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
}
