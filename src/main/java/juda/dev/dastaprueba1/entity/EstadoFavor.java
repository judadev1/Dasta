package juda.dev.dastaprueba1.entity;

/**
 * Estados posibles de un favor:
 * SOLICITADO - Cliente crea la solicitud
 * ASIGNADO - Se asigna a un repartidor
 * EN_PROCESO - Repartidor comienza el servicio
 * ENTREGADO - Servicio completado
 * CANCELADO - Servicio cancelado
 */
public enum EstadoFavor {
    SOLICITADO, ASIGNADO, EN_PROCESO, ENTREGADO, CANCELADO
}