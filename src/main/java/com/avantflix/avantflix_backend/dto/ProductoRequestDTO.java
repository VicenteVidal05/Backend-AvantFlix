package com.avantflix.avantflix_backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductoRequestDTO(
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    String nombre,

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    Integer precio,

    @Min(value = 0, message = "El descuento no puede ser negativo")
    @Max(value = 100, message = "El descuento no puede superar el 100%")
    Integer descuento,

    @NotNull
    @Min(0)
    Integer stock,

    @NotNull(message = "Debe indicar una categoría")
    Long idCategoria,

    String imagenUrl,
    String descripcion,

    @NotNull
    Boolean esDigital
) {}