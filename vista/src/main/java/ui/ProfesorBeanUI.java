package ui;

import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import mx.avanti.desarrollo.service.ProfesorService;

import java.io.Serializable;

@Named("profesorUI")
@ViewScoped
public class ProfesorBeanUI implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String rfc;
    private String correo;     // NUEVO

    private boolean mostrarOk;
    private String okMsg;

    private final ProfesorService service = new ProfesorService();

    public String guardar() {
        mostrarOk = false;
        okMsg = null;
        try {
            String tempPass = service.crearProfesorYUsuario(nombre, apellidoPaterno, apellidoMaterno, rfc, correo);
            okMsg = "Profesor registrado correctamente. Contraseña temporal: " + tempPass;
            mostrarOk = true;
            limpiar();
        } catch (IllegalArgumentException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error inesperado al guardar el profesor.", null));
        }
        return null;
    }

    private void limpiar() {
        nombre = apellidoPaterno = apellidoMaterno = rfc = correo = "";
    }

    // ===== Getters / Setters =====
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }
    public String getApellidoMaterno() { return apellidoMaterno; }
    public void setApellidoMaterno(String apellidoMaterno) { this.apellidoMaterno = apellidoMaterno; }
    public String getRfc() { return rfc; }
    public void setRfc(String rfc) { this.rfc = rfc; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public boolean isMostrarOk() { return mostrarOk; }
    public String getOkMsg() { return okMsg; }
}
