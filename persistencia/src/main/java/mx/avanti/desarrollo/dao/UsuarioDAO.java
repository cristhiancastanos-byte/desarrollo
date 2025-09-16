package mx.avanti.desarrollo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import mx.desarrollo.entity.Usuario;

import java.time.LocalDateTime;

public class UsuarioDAO {

    public Usuario findByCorreoAndPass(String correo, String pass) {
        EntityManager em = JpaUtil.em();
        try {
            return em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.correo = :c AND u.contrasena = :p", Usuario.class)
                    .setParameter("c", correo)
                    .setParameter("p", pass)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally { em.close(); }
    }

    public Usuario findByCorreo(String correo) {
        EntityManager em = JpaUtil.em();
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.correo = :c", Usuario.class)
                    .setParameter("c", correo)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally { em.close(); }
    }

    public Usuario findByToken(String token) {
        if (token == null || token.isEmpty()) return null;
        EntityManager em = JpaUtil.em();
        try {
            return em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.sessionToken = :t", Usuario.class)
                    .setParameter("t", token)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally { em.close(); }
    }

    public Usuario findById(Integer id) {
        EntityManager em = JpaUtil.em();
        try { return em.find(Usuario.class, id); }
        finally { em.close(); }
    }

    public void save(Usuario u) {
        EntityManager em = JpaUtil.em();
        try {
            em.getTransaction().begin();
            if (u.getId() == null) em.persist(u);
            else em.merge(u);
            em.getTransaction().commit();
        } finally { em.close(); }
    }

    public int addFailedAttempt(Integer id) {
        EntityManager em = JpaUtil.em();
        try {
            em.getTransaction().begin();
            Usuario u = em.find(Usuario.class, id);
            int n = (u.getIntentosFallidos() == null ? 0 : u.getIntentosFallidos()) + 1;
            u.setIntentosFallidos(n);
            em.getTransaction().commit();
            return n;
        } finally { em.close(); }
    }

    public void lockUntil(Integer id, LocalDateTime until) {
        EntityManager em = JpaUtil.em();
        try {
            em.getTransaction().begin();
            Usuario u = em.find(Usuario.class, id);
            u.setBloqueadoHasta(until);
            u.setIntentosFallidos(0);
            em.getTransaction().commit();
        } finally { em.close(); }
    }

    public void resetLoginState(Integer id) {
        EntityManager em = JpaUtil.em();
        try {
            em.getTransaction().begin();
            Usuario u = em.find(Usuario.class, id);
            u.setIntentosFallidos(0);
            u.setBloqueadoHasta(null);
            em.getTransaction().commit();
        } finally { em.close(); }
    }

    public void setToken(Integer id, String token, LocalDateTime expira) {
        EntityManager em = JpaUtil.em();
        try {
            em.getTransaction().begin();
            Usuario u = em.find(Usuario.class, id);
            u.setSessionToken(token);
            u.setTokenExpira(expira);
            em.getTransaction().commit();
        } finally { em.close(); }
    }

    public void clearToken(Integer id) {
        EntityManager em = JpaUtil.em();
        try {
            em.getTransaction().begin();
            Usuario u = em.find(Usuario.class, id);
            u.setSessionToken(null);
            u.setTokenExpira(null);
            em.getTransaction().commit();
        } finally { em.close(); }
    }

    public void clearTokenByValue(String token) {
        if (token == null) return;
        EntityManager em = JpaUtil.em();
        try {
            em.getTransaction().begin();
            Usuario u = null;
            try {
                u = em.createQuery("SELECT u FROM Usuario u WHERE u.sessionToken = :t", Usuario.class)
                        .setParameter("t", token)
                        .setMaxResults(1)
                        .getSingleResult();
            } catch (NoResultException ignore) {}
            if (u != null) {
                u.setSessionToken(null);
                u.setTokenExpira(null);
            }
            em.getTransaction().commit();
        } finally { em.close(); }
    }
}
