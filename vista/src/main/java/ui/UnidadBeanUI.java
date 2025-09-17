package ui;

import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import mx.avanti.desarrollo.service.UnidadService;
import mx.desarrollo.entity.Unidad;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Named("unidadUI")
@ViewScoped
public class UnidadBeanUI implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UnidadService service = new UnidadService();

    private Integer idEditar;
    private Integer idSeleccionado;
    private String nombre;
    private String tipo;
    private Integer horas;

    private boolean mostrarOk;
    private String okMsg;

    public List<String> getTipos() {
        return Arrays.asList("CLASE", "TALLER", "LABORATORIO");
    }

    public List<Unidad> getUnidades() {
        return service.listar();
    }

    public void onPickUnidad() {
        try {
            if (idSeleccionado == null) return;
            Unidad u = service.obtener(idSeleccionado);
            if (u == null) throw new IllegalArgumentException("No se encontró la unidad.");
            idEditar = u.getId();
            nombre = u.getNombre();
            tipo = u.getTipo();
            horas = u.getHoras();
        } catch (Exception e) {
            String msg = e.getMessage();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, msg != null ? msg : "Error al cargar la unidad.", null));
        }
    }

    public String guardarEdicion() {
        try {
            if (idEditar == null) throw new IllegalArgumentException("Selecciona una unidad.");
            service.actualizar(idEditar, nombre, tipo, horas);
            okMsg = "Unidad modificada correctamente.";
            mostrarOk = true;
        } catch (Exception e) {
            String msg = e.getMessage();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, msg != null ? msg : "Error al modificar la unidad.", null));
        }
        return null;
    }

    public Integer getIdEditar() { return idEditar; }
    public void setIdEditar(Integer idEditar) { this.idEditar = idEditar; }
    public Integer getIdSeleccionado() { return idSeleccionado; }
    public void setIdSeleccionado(Integer idSeleccionado) { this.idSeleccionado = idSeleccionado; }
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
