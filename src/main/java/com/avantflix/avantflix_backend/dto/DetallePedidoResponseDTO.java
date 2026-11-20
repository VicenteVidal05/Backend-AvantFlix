package com.avantflix.avantflix_backend.dto;

public record DetallePedidoResponseDTO(
    
    Long idDetallePedido,
    Long idProducto,
    String nombreProducto,
    Integer cantidad,
    Integer total,
    Integer iva
) {}