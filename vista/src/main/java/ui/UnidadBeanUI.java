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

    private Integer idEditar;
    private Integer idSeleccionado;
    private String nombre;
    private String tipo;
    private Integer horas;

    private String filtro;
    private List<Unidad> unidades = new ArrayList<>();

    private boolean mostrarOk = false;
    private String okMsg = "Operación exitosa";

    private final UnidadService service = new UnidadService();

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
            if (unidades == null || unidades.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "No hay unidades registradas", null));
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al consultar: " + ex.getMessage(), null));
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
            limpiarCampos();
            buscar();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, okMsg, null));
            return null;
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
            return null;
        }
    }

    public void onPickUnidad() {
        try {
            if (idSeleccionado == null) return;
            Unidad u = service.buscarPorId(idSeleccionado);
            if (u != null) {
                idEditar = u.getId();
                nombre = u.getNombre();
                tipo = u.getTipo();
                horas = u.getHoras();
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "No se pudo cargar la unidad: " + ex.getMessage(), null));
        }
    }

    public String guardarEdicion() {
        try {
            Integer id = (idEditar != null) ? idEditar : idSeleccionado;
            if (id == null) throw new IllegalArgumentException("Selecciona una unidad.");
            service.actualizar(id, nombre, tipo, horas);
            okMsg = "Unidad actualizada correctamente";
            mostrarOk = true;
            limpiarCampos();
            buscar();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, okMsg, null));
            return null;
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
            return null;
        }
    }

    private void limpiarCampos() {
        idEditar = null;
        idSeleccionado = null;
        nombre = null;
        tipo = null;
        horas = null;
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

    public String getFiltro() { return filtro; }
    public void setFiltro(String filtro) { this.filtro = filtro; }

    public List<Unidad> getUnidades() { return unidades; }
    public void setUnidades(List<Unidad> unidades) { this.unidades = unidades; }

    public boolean isMostrarOk() { return mostrarOk; }
    public void setMostrarOk(boolean mostrarOk) { this.mostrarOk = mostrarOk; }

    public String getOkMsg() { return okMsg; }
    public void setOkMsg(String okMsg) { this.okMsg = okMsg; }
}
