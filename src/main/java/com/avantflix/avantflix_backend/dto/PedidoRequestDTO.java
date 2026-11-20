package com.avantflix.avantflix_backend.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PedidoRequestDTO(
    
    @NotNull(message = "El ID del usuario es obligatorio")
    Long idUsuario,

    @NotEmpty(message = "El pedido debe tener al menos un producto")
    @Valid
    List<DetallePedidoRequestDTO> detalles
) {}