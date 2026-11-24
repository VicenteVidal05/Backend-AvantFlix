package com.avantflix.avantflix_backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CarritoProductoRequestDTO(
    @NotNull(message = "El ID del carrito es obligatorio")
    Long idCarrito,

    @NotNull(message = "El ID del producto es obligatorio")
    Long idProducto,

    @Min(value = 1, message = "La cantidad mínima es 1")
    Integer cantidad
) {}
