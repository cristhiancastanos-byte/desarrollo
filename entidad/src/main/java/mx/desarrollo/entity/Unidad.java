package mx.desarrollo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "unidad")
public class Unidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idunidad")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    // CLASE | TALLER | LABORATORIO (texto en BD)
    @Column(name = "tipo", nullable = false, length = 15)
    private String tipo;

    // 0 a 4 horas
    @Column(name = "horas", nullable = false)
    private Integer horas;

    // ===== Getters/Setters =====
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Integer getHoras() { return horas; }
    public void setHoras(Integer horas) { this.horas = horas; }
}
