package juda.dev.dastaprueba1.repository;

import juda.dev.dastaprueba1.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de repartidores.
 * Proporciona operaciones básicas de base de datos para 
 * crear, leer, actualizar y eliminar registros de repartidores.
 */
@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {}