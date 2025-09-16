package mx.avanti.desarrollo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("persistencePU");
    public static EntityManager em() { return emf.createEntityManager(); }
}
