package juda.dev.dastaprueba1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.math.BigDecimal;
import java.util.Set;

/**
 * Representa los productos disponibles en las tiendas.
 * Cada producto tiene un precio establecido y está asociado 
 * a una tienda específica. Puede ser solicitado en múltiples favores.
 */
@Entity
@Table(name = "productos")
@Data
public class Productos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idproductos;

    @Column(length = 150)
    private String nombre;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idtienda", nullable = false)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Tienda tienda;

    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Set<FavorProducto> lineasDeFavor;
}