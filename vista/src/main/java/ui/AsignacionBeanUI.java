package ui;

import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import mx.avanti.desarrollo.service.AsignacionService;
import mx.desarrollo.entity.Profesor;
import mx.desarrollo.entity.Unidad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("asignacionUI")
@ViewScoped
public class AsignacionBeanUI implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer profesorId;
    private List<Integer> unidadIds = new ArrayList<>();
    private String horario;

    private boolean mostrarOk = false;
    private String okMsg = "Asignación guardada correctamente";

    private final AsignacionService service = new AsignacionService();

    public String guardar() {
        try {
            int n = service.asignar(profesorId, unidadIds, horario);
            okMsg = (n == 1) ? "Asignación guardada correctamente"
                    : (n + " asignaciones guardadas correctamente");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, okMsg, null));

            mostrarOk = true;

            limpiar();
        } catch (IllegalArgumentException ex) {
            mostrarOk = false;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        } catch (Exception ex) {
            mostrarOk = false;
            String msg = ex.getMessage();
            Throwable c = ex.getCause();
            while (c != null) { msg = c.getMessage(); c = c.getCause(); }
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            (msg != null && !msg.isBlank()) ? msg : "Error inesperado al guardar la asignación.", null));
        }
        return null;
    }

    private void limpiar() {
        profesorId = null;
        unidadIds = new ArrayList<>();
        horario = "";
    }

    public List<Profesor> getProfesores() { return service.listarProfesores(); }
    public List<Unidad> getUnidades() { return service.listarUnidades(); }

    public Integer getProfesorId() { return profesorId; }
    public void setProfesorId(Integer profesorId) { this.profesorId = profesorId; }

    public List<Integer> getUnidadIds() { return unidadIds; }
    public void setUnidadIds(List<Integer> unidadIds) { this.unidadIds = unidadIds; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    public boolean isMostrarOk() { return mostrarOk; }
    public String getOkMsg() { return okMsg; }
}
