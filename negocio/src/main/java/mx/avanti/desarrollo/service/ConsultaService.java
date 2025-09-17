package mx.avanti.desarrollo.service;

import mx.avanti.desarrollo.dao.ConsultaDAO;
import mx.avanti.desarrollo.service.dto.ConsultaPorProfesorRow;
import mx.desarrollo.entity.Asignacion;
import mx.desarrollo.entity.Profesor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConsultaService {

    private final ConsultaDAO dao = new ConsultaDAO();

    public List<ConsultaPorProfesorRow> consultaGeneral() {
        List<Profesor> profesores = dao.listProfesoresOrdenados();
        List<Asignacion> asignaciones = dao.listAsignacionesTodas();

        Map<Integer, ConsultaPorProfesorRow> mapa = new LinkedHashMap<>();
        for (Profesor p : profesores) {
            Integer id = p.getId();
            String nombre = nombreCompleto(p.getNombre(), p.getApellidoPaterno(), p.getApellidoMaterno());
            mapa.put(id, new ConsultaPorProfesorRow(id, nombre));
        }

        for (Asignacion a : asignaciones) {
            Integer id = a.getProfesor().getId();
            ConsultaPorProfesorRow row = mapa.get(id);
            if (row != null) {
                row.getUnidades().add(a.getUnidad().getNombre());
                String h = a.getHorario();
                row.getHorarios().add(h == null || h.isBlank() ? "Sin horario" : h);
            }
        }
        return new ArrayList<>(mapa.values());
    }

    private String nombreCompleto(String n, String ap, String am) {
        String s = (ap == null ? "" : ap) + " " + (am == null ? "" : am) + " " + (n == null ? "" : n);
        return s.trim().replaceAll("\\s{2,}", " ");
    }
}
