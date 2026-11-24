package com.avantflix.avantflix_backend.dto;

public record CarritoProductoResponseDTO(
    
    Long idCarrito,
    Long idProducto,
    String nombreProducto,
    Integer precioUnitario,
    Integer cantidad,
    Integer subtotal
) {}
