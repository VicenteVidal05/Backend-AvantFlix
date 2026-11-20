package com.avantflix.avantflix_backend.dto;

import java.util.List;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AnimeRequestDTO(
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100)
    String titulo,

    @NotBlank(message = "La temporada es obligatoria")
    @Size(max = 45)
    String temporada,

    @NotBlank(message = "El género es obligatorio")
    @Size(max = 100)
    String genero,

    String sinopsis,

    @Size(max = 255)
    String imagenUrl,

    @NotNull(message = "La cantidad de episodios es obligatoria")
    @Min(value = 1, message = "Debe tener al menos 1 episodio")
    Integer episodios,

    @NotNull(message = "La calificación es obligatoria")
    @DecimalMin(value = "0.0", message = "La calificación mínima es 0.0")
    @DecimalMax(value = "10.0", message = "La calificación máxima es 10.0")
    Double calificacion,

    List<Long> idsProductos
) {}