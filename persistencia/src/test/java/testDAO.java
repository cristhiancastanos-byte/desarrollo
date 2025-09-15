import mx.avanti.desarrollo.dao.AlumnoDAO;
import mx.avanti.desarrollo.persistence.HibernateUtil;
import mx.desarrollo.entity.Alumno;

import java.util.List;

public class testDAO {

    public static void main(String[] args) {
        AlumnoDAO alumnoDAO = new AlumnoDAO(HibernateUtil.getEntityManager());


        for (Alumno alumno : alumnoDAO.findAll()) {
            System.out.println(alumno + "|| id [" + alumno.getId()+ "]");
        }
    }
}
