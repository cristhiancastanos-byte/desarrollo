package mx.desarrollo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "asignacion",
        uniqueConstraints = @UniqueConstraint(name = "uk_asig_prof_uni_hor",
                columnNames = {"profesor_id", "unidad_id", "horario"}))
public class Asignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idasignacion")
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id", referencedColumnName = "idprofesor", nullable = false)
    private Profesor profesor;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "unidad_id", referencedColumnName = "idunidad", nullable = false)
    private Unidad unidad;

    @Column(name = "horario", nullable = false, length = 50)
    private String horario;

    // ===== Getters/Setters =====
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Profesor getProfesor() { return profesor; }
    public void setProfesor(Profesor profesor) { this.profesor = profesor; }

    public Unidad getUnidad() { return unidad; }
    public void setUnidad(Unidad unidad) { this.unidad = unidad; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }
}
