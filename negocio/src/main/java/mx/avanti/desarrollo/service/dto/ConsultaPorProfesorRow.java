package mx.avanti.desarrollo.service.dto;

import java.util.ArrayList;
import java.util.List;

public class ConsultaPorProfesorRow {
    private Integer idProfesor;
    private String profesor;
    private final List<String> unidades = new ArrayList<>();
    private final List<String> horarios = new ArrayList<>();

    public ConsultaPorProfesorRow(Integer idProfesor, String profesor) {
        this.idProfesor = idProfesor;
        this.profesor = profesor;
    }

    public Integer getIdProfesor() { return idProfesor; }
    public String getProfesor() { return profesor; }
    public List<String> getUnidades() { return unidades; }
    public List<String> getHorarios() { return horarios; }
    public String getUnidadesTexto() { return unidades.isEmpty() ? "Este profesor no tiene unidades asignadas" : String.join(", ", unidades); }
    public String getHorariosTexto() { return horarios.isEmpty() ? "—" : String.join(", ", horarios); }
}
