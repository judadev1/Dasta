package juda.dev.dastaprueba1.service;

import juda.dev.dastaprueba1.entity.Tienda;
import juda.dev.dastaprueba1.repository.TiendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TiendaService {

    // Repositorio para acceso a datos de tiendas
    private final TiendaRepository tiendaRepository;

    /**
     * Obtiene la lista completa de tiendas disponibles
     */
    @Transactional(readOnly = true)
    public List<Tienda> getAllTiendas() {
        return tiendaRepository.findAll();
    }

    /**
     * Busca una tienda específica por su ID
     * @throws EntityNotFoundException si no encuentra la tienda
     */
    @Transactional(readOnly = true)
    public Tienda findById(Integer idTienda) {
        return tiendaRepository.findById(idTienda)
                .orElseThrow(() -> new EntityNotFoundException("Tienda no encontrada con ID: " + idTienda));
    }

}