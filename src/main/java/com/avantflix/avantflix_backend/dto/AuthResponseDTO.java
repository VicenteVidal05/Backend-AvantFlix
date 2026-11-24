package com.avantflix.avantflix_backend.dto;

public record AuthResponseDTO(
    
        String token,
        Long idUsuario,
        String nombre,
        String apellidoP,
        String correo,
        String rol,
        Long idCarrito
) {}
