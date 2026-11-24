package com.avantflix.avantflix_backend.dto;

public record BlogResponseDTO(
    Long idBlog,
    String titulo,
    String descripcion,
    Long idTag,
    String nombreTag,
    Long idUsuario,
    String nombreUsuario
) {}
