package com.avantflix.avantflix_backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DetallePedidoRequestDTO(

    Long idPedido,
    
    @NotNull(message = "El ID del producto es obligatorio")
    Long idProducto,

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima es 1")
    Integer cantidad
) {}
