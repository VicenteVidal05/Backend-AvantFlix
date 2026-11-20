package com.avantflix.avantflix_backend.dto;

import java.util.List;

public record AnimeResponseDTO(
    
    Long idAnime,
    String titulo,
    String temporada,
    String genero,
    String sinopsis,
    String imagenUrl,
    Integer episodios,
    Double calificacion,
    List<ProductoResponseDTO> productosRelacionados
) {}