package com.avantflix.avantflix_backend.dto;

public record ProductoResponseDTO(

    Long idProducto,
    String nombre,
    Integer precio,
    Integer stock,
    Integer descuento,
    String nombreCategoria,
    String imagenUrl,
    String descripcion,
    Boolean esDigital
) {}
