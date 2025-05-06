package juda.dev.dastaprueba1.service;

import juda.dev.dastaprueba1.entity.Delivery;
import juda.dev.dastaprueba1.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    // Repositorio para acceso a datos de repartidores
    private final DeliveryRepository deliveryRepository;

    /**
     * Obtiene la lista de todos los repartidores registrados
     * @return Lista de repartidores activos en el sistema
     */
    @Transactional(readOnly = true)
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }
}