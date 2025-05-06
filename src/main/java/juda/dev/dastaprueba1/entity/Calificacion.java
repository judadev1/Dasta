package juda.dev.dastaprueba1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.Date;

/**
 * Entidad que representa la calificación de un servicio.
 * Almacena la evaluación que un usuario hace sobre un favor completado,
 * incluyendo una nota numérica, comentarios y referencias al favor,
 * delivery y persona que califica.
 */
@Entity
@Table(name = "calificacion", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"idfavor"}, name = "uk_calificacion_favor")
})
@Data
public class Calificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idcalificacion;

    @Column(nullable = false)
    private Byte nota;

    @Lob
    private String comentario;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion", nullable = false, insertable = false, updatable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iddelivery", nullable = false)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Delivery delivery;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idfavor", nullable = false, unique = true)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Favor favor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idpersona", nullable = false)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Persona persona;
}