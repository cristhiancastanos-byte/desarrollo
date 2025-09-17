package ui;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import mx.avanti.desarrollo.service.ConsultaService;
import mx.avanti.desarrollo.service.dto.ConsultaPorProfesorRow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("consultaUI")
@ViewScoped
public class ConsultaBeanUI implements Serializable {
    private static final long serialVersionUID = 1L;

    private final ConsultaService service = new ConsultaService();
    private List<ConsultaPorProfesorRow> rows = new ArrayList<>();

    @PostConstruct
    public void init() {
        rows = service.consultaGeneral();
    }

    public String buscar() {
        rows = service.consultaGeneral();
        return null;
    }

    public List<ConsultaPorProfesorRow> getRows() { return rows; }
    public void setRows(List<ConsultaPorProfesorRow> rows) { this.rows = rows; }
}
