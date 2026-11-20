package com.avantflix.avantflix_backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
    
    Long idPedido,
    LocalDateTime fecha,
    Integer total,
    Long idUsuario,
    String nombreUsuario,
    List<DetallePedidoResponseDTO> detalles
) {}