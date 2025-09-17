package mx.avanti.desarrollo.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.Asignacion;
import mx.desarrollo.entity.Profesor;

import java.util.List;

public class ConsultaDAO {

    public List<Profesor> listProfesoresOrdenados() {
        EntityManager em = JpaUtil.em();
        try {
            return em.createQuery(
                    "select p from Profesor p order by p.apellidoPaterno, p.apellidoMaterno, p.nombre",
                    Profesor.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Asignacion> listAsignacionesTodas() {
        EntityManager em = JpaUtil.em();
        try {
            return em.createQuery(
                    "select a from Asignacion a join fetch a.profesor p join fetch a.unidad u",
                    Asignacion.class
            ).getResultList();
        } finally {
            em.close();
        }
    }
}
