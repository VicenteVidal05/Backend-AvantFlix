package com.avantflix.avantflix_backend.dto;

import org.hibernate.validator.constraints.URL;

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

    @URL(message = "El formato de la URL de imagen no es valido")
    @Size(max = 500, message = "La URL es demasiado larga")
    String imagenUrl,
    
    @Size(max = 1000, message = "La descripcion no puede superar los 1000 caracteres")
    String descripcion,

    @NotNull(message = "Debe indicar si el producto es digital")
    Boolean esDigital
) {}
