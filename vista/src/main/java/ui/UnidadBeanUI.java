package ui;

import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import mx.avanti.desarrollo.service.UnidadService;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Named("unidadUI")
@ViewScoped
public class UnidadBeanUI implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;   // requerido, <=50
    private String tipo;     // CLASE | TALLER | LABORATORIO
    private Integer horas;   // 0..4

    private boolean mostrarOk;  // dispara el modal
    private String okMsg;

    private final UnidadService service = new UnidadService();

    public List<String> getTipos() {
        return Arrays.asList("CLASE", "TALLER", "LABORATORIO");
    }

    public String guardar() {
        // MUY IMPORTANTE: si la validación falla, JSF no entra aquí,
        // pero cuando sí entra (caso válido), reseteamos antes:
        mostrarOk = false;
        okMsg = null;

        try {
            service.crear(nombre, tipo, horas);   // persiste
            okMsg = "Unidad registrada correctamente";
            mostrarOk = true;                     // solo en éxito
            limpiar();
        } catch (IllegalArgumentException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        } catch (Exception ex) {
            String msg = ex.getMessage();
            Throwable c = ex.getCause();
            while (c != null) { msg = c.getMessage(); c = c.getCause(); }
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            msg != null ? msg : "Error inesperado al guardar la unidad.", null));
        }
        return null; // permanecer en la misma página
    }

    private void limpiar() { nombre = ""; tipo = null; horas = null; }

    // Getters/Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Integer getHoras() { return horas; }
    public void setHoras(Integer horas) { this.horas = horas; }
    public boolean isMostrarOk() { return mostrarOk; }
    public void setMostrarOk(boolean mostrarOk) { this.mostrarOk = mostrarOk; }
    public String getOkMsg() { return okMsg; }
    public void setOkMsg(String okMsg) { this.okMsg = okMsg; }
}
