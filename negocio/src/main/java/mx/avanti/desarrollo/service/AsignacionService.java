package mx.avanti.desarrollo.service;

import jakarta.persistence.EntityManager;
import mx.avanti.desarrollo.dao.AsignacionDAO;
import mx.avanti.desarrollo.dao.JpaUtil;
import mx.avanti.desarrollo.dao.ProfesorDAO;
import mx.avanti.desarrollo.dao.UnidadDAO;
import mx.desarrollo.entity.Asignacion;
import mx.desarrollo.entity.Profesor;
import mx.desarrollo.entity.Unidad;

import java.util.List;

public class AsignacionService {

    private final AsignacionDAO adao = new AsignacionDAO();
    private final ProfesorDAO pdao = new ProfesorDAO();
    private final UnidadDAO udao = new UnidadDAO();

    public List<Profesor> listarProfesores() {
        EntityManager em = JpaUtil.em();
        try {
            return em.createQuery(
                    "SELECT p FROM Profesor p ORDER BY p.nombre, p.apellidoPaterno, p.apellidoMaterno",
                    Profesor.class).getResultList();
        } finally { em.close(); }
    }

    public List<Unidad> listarUnidades() {
        EntityManager em = JpaUtil.em();
        try {
            return em.createQuery(
                            "SELECT u FROM Unidad u ORDER BY u.nombre", Unidad.class)
                    .getResultList();
        } finally { em.close(); }
    }

    public int asignar(Integer profesorId, List<Integer> unidadIds, String horario) {
        if (profesorId == null) throw new IllegalArgumentException("Selecciona un profesor.");
        if (unidadIds == null || unidadIds.isEmpty()) throw new IllegalArgumentException("Selecciona una o más unidades.");
        if (horario == null || horario.trim().isEmpty()) throw new IllegalArgumentException("Ingresa el horario.");
        if (horario.length() > 50) throw new IllegalArgumentException("El horario no debe exceder 50 caracteres.");

        if (adao.conflictoHorario(profesorId, horario)) {
            throw new IllegalArgumentException("Conflicto de horario con otra asignación del profesor.");
        }

        int creadas = 0;
        for (Integer uid : unidadIds) {
            if (adao.existsDuplicado(profesorId, uid, horario)) {
                throw new IllegalArgumentException("Asignación duplicada: esa unidad ya está asignada al profesor con ese horario.");
            }
            Profesor p = new Profesor(); p.setId(profesorId);
            Unidad u = new Unidad();    u.setId(uid);

            Asignacion a = new Asignacion();
            a.setProfesor(p);
            a.setUnidad(u);
            a.setHorario(horario.trim());

            adao.save(a);
            creadas++;
        }
        return creadas;
    }
}
