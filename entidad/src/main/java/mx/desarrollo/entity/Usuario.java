package mx.desarrollo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private Integer id;

    @Column(name = "correo", nullable = false, unique = true, length = 100)
    private String correo;

    @Column(name = "contrasena", nullable = false, length = 100)
    private String contrasena;

    // 0 = profesor, 1 = admin
    @Column(name = "es_admin", nullable = false)
    private Integer esAdmin = 0;

    // Seguridad por intentos
    @Column(name = "intentos_fallidos", nullable = false)
    private Integer intentosFallidos = 0;

    @Column(name = "bloqueado_hasta")
    private LocalDateTime bloqueadoHasta;

    // --- NUEVO: token absoluto
    @Column(name = "session_token", length = 64)
    private String sessionToken;

    @Column(name = "token_expira")
    private LocalDateTime tokenExpira;

    // ===== Getters/Setters =====
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Integer getEsAdmin() { return esAdmin; }
    public void setEsAdmin(Integer esAdmin) { this.esAdmin = esAdmin; }

    public Integer getIntentosFallidos() { return intentosFallidos; }
    public void setIntentosFallidos(Integer intentosFallidos) { this.intentosFallidos = intentosFallidos; }

    public LocalDateTime getBloqueadoHasta() { return bloqueadoHasta; }
    public void setBloqueadoHasta(LocalDateTime bloqueadoHasta) { this.bloqueadoHasta = bloqueadoHasta; }

    public String getSessionToken() { return sessionToken; }
    public void setSessionToken(String sessionToken) { this.sessionToken = sessionToken; }

    public LocalDateTime getTokenExpira() { return tokenExpira; }
    public void setTokenExpira(LocalDateTime tokenExpira) { this.tokenExpira = tokenExpira; }
}
