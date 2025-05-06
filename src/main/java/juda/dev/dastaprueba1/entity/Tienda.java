package juda.dev.dastaprueba1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.Set;

/**
 * Representa una tienda en el sistema.
 * Cada tienda tiene un nombre único y mantiene su propio catálogo 
 * de productos. Los favores pueden estar asociados a productos 
 * específicos de una tienda.
 */
@Entity
@Table(name = "tienda")
@Data
public class Tienda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idtienda;

    @Column(length = 150, unique = true)
    private String nombre;

    @OneToMany(mappedBy = "tienda", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Set<Productos> productos;

    @OneToMany(mappedBy = "tienda", fetch = FetchType.LAZY)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Set<Favor> favoresAsociados;
}