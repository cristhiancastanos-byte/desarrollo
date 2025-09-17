package mx.avanti.desarrollo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import mx.desarrollo.entity.Asignacion;

import java.util.ArrayList;
import java.util.List;

public class AsignacionDAO {

    public void save(Asignacion a) {
        EntityManager em = JpaUtil.em();
        try {
            em.getTransaction().begin();
            if (a.getId() == null) em.persist(a);
            else em.merge(a);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public boolean existsDuplicado(Integer profesorId, Integer unidadId, String horario) {
        EntityManager em = JpaUtil.em();
        try {
            Long c = em.createQuery(
                            "SELECT COUNT(a) FROM Asignacion a " +
                                    "WHERE a.profesor.id = :p AND a.unidad.id = :u AND a.horario = :h", Long.class)
                    .setParameter("p", profesorId)
                    .setParameter("u", unidadId)
                    .setParameter("h", horario)
                    .getSingleResult();
            return c != 0;
        } finally {
            em.close();
        }
    }

    public List<Asignacion> listarPorProfesor(Integer profesorId) {
        EntityManager em = JpaUtil.em();
        try {
            return em.createQuery(
                            "SELECT a FROM Asignacion a " +
                                    "JOIN FETCH a.unidad " +
                                    "WHERE a.profesor.id = :p", Asignacion.class)
                    .setParameter("p", profesorId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public boolean conflictoHorario(Integer profesorId, String horarioNuevo) {
        List<Asignacion> existentes = listarPorProfesor(profesorId);
        int[] nuevo = parseRango(horarioNuevo);

        for (Asignacion a : existentes) {
            int[] ex = parseRango(a.getHorario());
            if (nuevo != null && ex != null) {
                if (nuevo[0] < ex[1] && ex[0] < nuevo[1]) return true;
            } else {
                if (a.getHorario() != null && a.getHorario().trim().equalsIgnoreCase(horarioNuevo.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    private int[] parseRango(String h) {
        if (h == null) return null;
        String s = h.trim();
        java.util.regex.Matcher m = java.util.regex.Pattern
                .compile("(\\d{1,2}):(\\d{2})\\s*-\\s*(\\d{1,2}):(\\d{2})")
                .matcher(s);
        if (!m.find()) return null;
        try {
            int h1 = Integer.parseInt(m.group(1)), m1 = Integer.parseInt(m.group(2));
            int h2 = Integer.parseInt(m.group(3)), m2 = Integer.parseInt(m.group(4));
            int start = h1 * 60 + m1;
            int end   = h2 * 60 + m2;
            if (end <= start) return null; // rango inválido
            return new int[]{start, end};
        } catch (Exception e) {
            return null;
        }
    }
}
