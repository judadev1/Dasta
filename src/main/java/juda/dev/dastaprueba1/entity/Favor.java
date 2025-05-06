package juda.dev.dastaprueba1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Representa una solicitud de servicio o "favor" en el sistema.
 * Gestiona la información del pedido incluyendo quién lo solicita,
 * quién lo entrega, la tienda origen, productos solicitados, 
 * estado actual y su calificación final.
 * Por defecto inicia en estado SOLICITADO.
 */
@Entity
@Table(name = "favor")
@Data
public class Favor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idfavor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idpersona", nullable = false)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iddelivery")
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Delivery delivery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idtienda")
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Tienda tienda;

    @Lob
    private String descripcion;

    @Column(length = 255)
    private String direccion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoFavor estado = EstadoFavor.SOLICITADO;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion", nullable = false, insertable = false, updatable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date fechaCreacion;

    @OneToMany(mappedBy = "favor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Set<FavorProducto> itemsProducto = new HashSet<>();

    @OneToOne(mappedBy = "favor", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Calificacion calificacion;
}