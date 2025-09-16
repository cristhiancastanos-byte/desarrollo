package ui;

import helper.LoginHelper;
import helper.LoginHelper.AuthResult;
import helper.LoginHelper.TokenBundle;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.desarrollo.entity.Usuario;

import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Named("loginUI")
@SessionScoped
public class LoginBeanUI implements Serializable {

    private Usuario usuario;
    private Usuario usuarioLogueado;
    private final LoginHelper helper = new LoginHelper();

    @PostConstruct
    public void init() { usuario = new Usuario(); }

    public void login() throws IOException {
        AuthResult r = helper.login(usuario.getCorreo(), usuario.getContrasena());

        switch (r.status) {
            case OK -> {
                Usuario us = r.usuario;
                this.usuarioLogueado = us;

                var ext = FacesContext.getCurrentInstance().getExternalContext();
                ext.getSessionMap().put("usuarioLogueado", us);

                // === NUEVO: emitir token y setear cookie ===
                TokenBundle tb = helper.emitirToken(us);
                HttpServletRequest req = (HttpServletRequest) ext.getRequest();
                HttpServletResponse resp = (HttpServletResponse) ext.getResponse();

                String path = req.getContextPath();
                if (path == null || path.isEmpty()) path = "/";

                long maxAge = Duration.between(ZonedDateTime.now(ZoneId.of("UTC")),
                        tb.expira.atZone(ZoneId.of("UTC"))).getSeconds();

                Cookie c = new Cookie(LoginHelper.COOKIE_NAME, tb.token);
                c.setHttpOnly(true);
                c.setSecure(req.isSecure());
                c.setPath(path);
                c.setMaxAge((int) Math.max(0, maxAge));
                resp.addCookie(c);
                // SameSite (no hay setter en Servlet): añadir header adicional
                resp.addHeader("Set-Cookie",
                        LoginHelper.COOKIE_NAME + "=" + tb.token +
                                "; Max-Age=" + maxAge +
                                "; Path=" + path +
                                "; HttpOnly" +
                                (req.isSecure() ? "; Secure" : "") +
                                "; SameSite=Lax");

                String rol = helper.esAdmin(us) ? "Administrador" : "Profesor";
                String destino = helper.esAdmin(us) ? "/admin/index.xhtml" : "/profesor/index.xhtml";

                ext.getFlash().put("msgBien", String.format(LoginHelper.MSG_BIENVENIDO, rol));
                ext.redirect(ext.getRequestContextPath() + destino);
            }
            case LOCKED, INVALID -> FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_WARN, r.mensaje, "")
            );
        }
    }

    public void logout() throws IOException {
        var ext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest req = (HttpServletRequest) ext.getRequest();
        HttpServletResponse resp = (HttpServletResponse) ext.getResponse();

        // Limpiar token en BD si hay usuario
        if (usuarioLogueado != null) {
            new mx.avanti.desarrollo.dao.UsuarioDAO().clearToken(usuarioLogueado.getId());
        }
        // Borrar cookie
        String path = req.getContextPath();
        if (path == null || path.isEmpty()) path = "/";
        Cookie bye = new Cookie(LoginHelper.COOKIE_NAME, "");
        bye.setHttpOnly(true);
        bye.setSecure(req.isSecure());
        bye.setPath(path);
        bye.setMaxAge(0);
        resp.addCookie(bye);
        resp.addHeader("Set-Cookie", LoginHelper.COOKIE_NAME + "=; Max-Age=0; Path=" + path +
                "; HttpOnly" + (req.isSecure() ? "; Secure" : "") + "; SameSite=Lax");

        // invalidar sesión
        ext.invalidateSession();
        ext.redirect(ext.getRequestContextPath() + "/login.xhtml?expired=1");
    }

    // ===== Getters/Setters =====
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Usuario getUsuarioLogueado() { return usuarioLogueado; }
    public void setUsuarioLogueado(Usuario usuarioLogueado) { this.usuarioLogueado = usuarioLogueado; }
    public boolean isAdmin() { return new LoginHelper().esAdmin(usuarioLogueado); }
    public String getCorreo() { return usuarioLogueado != null ? usuarioLogueado.getCorreo() : ""; }
}
