package ui;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import mx.avanti.desarrollo.service.UnidadService;
import mx.desarrollo.entity.Unidad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Named("unidadUI")
@ViewScoped
public class UnidadBeanUI implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UnidadService service = new UnidadService();

    private Integer idSeleccionado;
    private Integer idEditar;
    private String nombre;
    private String tipo;
    private Integer horas;
    private String filtro;
    private List<Unidad> unidades = new ArrayList<>();
    private boolean mostrarOk;
    private String okMsg;

    @PostConstruct
    public void init() {
        buscar();
    }

    public List<String> getTipos() {
        return Arrays.asList("CLASE", "TALLER", "LABORATORIO");
    }

    public void buscar() {
        try {
            unidades = service.buscarPorNombre(filtro);
            mostrarOk = false;
        } catch (Exception ex) {
            addError("Error al consultar: " + ex.getMessage());
        }
    }

    public void onPickUnidad() {
        if (idSeleccionado == null) {
            idEditar = null;
            nombre = null;
            tipo = null;
            horas = null;
            return;
        }
        try {
            Unidad u = service.obtenerPorId(idSeleccionado);
            if (u != null) {
                idEditar = u.getId();
                nombre = u.getNombre();
                tipo = u.getTipo();
                horas = u.getHoras();
            }
        } catch (Exception ex) {
            addError("No se pudo cargar la unidad seleccionada.");
        }
    }

    public String guardar() {
        try {
            if (idEditar == null) {
                service.crear(nombre, tipo, horas);
                okMsg = "Unidad guardada correctamente";
            } else {
                service.actualizar(idEditar, nombre, tipo, horas);
                okMsg = "Unidad actualizada correctamente";
            }
            mostrarOk = true;
            limpiarEdicion();
            buscar();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            addError(ex.getMessage());
        } catch (Exception ex) {
            addError("Error inesperado al guardar.");
        }
        return null;
    }

    public String eliminar() {
        try {
            if (idSeleccionado == null) {
                addError("Selecciona una unidad.");
                return null;
            }
            service.eliminar(idSeleccionado);
            okMsg = "Eliminado correctamente";
            mostrarOk = true;
            idSeleccionado = null;
            buscar();
        } catch (IllegalStateException | IllegalArgumentException ex) {
            addError(ex.getMessage());
        } catch (Exception ex) {
            addError("Error inesperado al eliminar.");
        }
        return null;
    }

    public void cerrarOk() {
        mostrarOk = false;
        okMsg = null;
    }

    private void limpiarEdicion() {
        idEditar = null;
        nombre = null;
        tipo = null;
        horas = null;
    }

    private void addError(String m) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, m, null));
    }

    public Integer getIdSeleccionado() { return idSeleccionado; }
    public void setIdSeleccionado(Integer idSeleccionado) { this.idSeleccionado = idSeleccionado; }
    public Integer getIdEditar() { return idEditar; }
    public void setIdEditar(Integer idEditar) { this.idEditar = idEditar; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Integer getHoras() { return horas; }
    public void setHoras(Integer horas) { this.horas = horas; }
    public String getFiltro() { return filtro; }
    public void setFiltro(String filtro) { this.filtro = filtro; }
    public List<Unidad> getUnidades() { return unidades; }
    public void setUnidades(List<Unidad> unidades) { this.unidades = unidades; }
    public boolean isMostrarOk() { return mostrarOk; }
    public void setMostrarOk(boolean mostrarOk) { this.mostrarOk = mostrarOk; }
    public String getOkMsg() { return okMsg; }
    public void setOkMsg(String okMsg) { this.okMsg = okMsg; }
}
