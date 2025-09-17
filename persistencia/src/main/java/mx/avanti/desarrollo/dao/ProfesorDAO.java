package mx.avanti.desarrollo.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.Profesor;

public class ProfesorDAO {

    public void save(Profesor p) {
        EntityManager em = JpaUtil.em();
        try {
            em.getTransaction().begin();
            if (p.getId() == null) em.persist(p);
            else em.merge(p);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public boolean existsByRFC(String rfc) {
        EntityManager em = JpaUtil.em();
        try {
            Long c = em.createQuery(
                            "SELECT COUNT(p) FROM Profesor p WHERE p.rfc = :r", Long.class)
                    .setParameter("r", rfc)
                    .getSingleResult();
            return c != 0;
        } finally {
            em.close();
        }
    }
}
