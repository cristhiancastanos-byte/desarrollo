package mx.desarrollo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "profesor",
        uniqueConstraints = @UniqueConstraint(name = "uk_profesor_rfc", columnNames = "rfc"))
public class Profesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idprofesor")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "apellidop", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "apellidom", nullable = false, length = 50)
    private String apellidoMaterno;

    @Column(name = "rfc", nullable = false, length = 13)
    private String rfc; // siempre en MAYÚSCULAS

    // === Relación al usuario para login ===
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "idusuario", unique = true, nullable = false)
    private Usuario usuario;

    // ===== Getters / Setters =====
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }

    public String getApellidoMaterno() { return apellidoMaterno; }
    public void setApellidoMaterno(String apellidoMaterno) { this.apellidoMaterno = apellidoMaterno; }

    public String getRfc() { return rfc; }
    public void setRfc(String rfc) { this.rfc = rfc; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
