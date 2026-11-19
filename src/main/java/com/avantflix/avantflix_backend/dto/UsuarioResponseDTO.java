package com.avantflix.avantflix_backend.dto;

public record UsuarioResponseDTO(

    Long idUsuario,
    String nombre,
    String apellidoP,
    String apellidoM,
    String correo,
    String direccion,
    String rol,
    String comuna
) {}
