package com.avantflix.avantflix_backend.dto;

public record ProductoResponseDTO(

    Long idProducto,
    String nombre,
    Integer precio,
    Integer descuento,
    Integer stock,
    String nombreCategoria,
    String imagenUrl,
    String descripcion,
    Boolean esDigital
) {}
