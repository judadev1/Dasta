package juda.dev.dastaprueba1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.Set;

/**
 * Entidad que representa a un repartidor en el sistema.
 * Almacena su información de contacto y mantiene registro 
 * de los favores que realiza y las calificaciones que recibe.
 */
@Entity
@Table(name = "delivery")
@Data
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer iddelivery;

    @Column(length = 100)
    private String nombre1;
    @Column(length = 100)
    private String nombre2;
    @Column(length = 100)
    private String apellido1;
    @Column(length = 100)
    private String apellido2;

    @Column(length = 20, unique = true)
    private String telefono;
    @Column(length = 100, unique = true)
    private String correo;

    @OneToMany(mappedBy = "delivery", fetch = FetchType.LAZY)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Set<Favor> favoresAsignados;

    @OneToMany(mappedBy = "delivery", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Set<Calificacion> calificacionesRecibidas;
}