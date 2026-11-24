package com.avantflix.avantflix_backend.dto;

public record DetallePedidoResponseDTO(

    Long idDetallePedido,
    Long idProducto,
    Long idPedido,
    String nombreProducto,
    Integer cantidad,
    Integer total,
    Integer iva
) {}