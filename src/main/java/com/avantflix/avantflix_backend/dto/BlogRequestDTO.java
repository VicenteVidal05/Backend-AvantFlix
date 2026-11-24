package com.avantflix.avantflix_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BlogRequestDTO(

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100, message = "El título no puede superar los 100 caracteres")
    String titulo,

    @NotBlank(message = "La descripción es obligatoria")
    String descripcion,

    @NotNull(message = "El ID del tag es obligatorio")
    Long idTag,

    @NotNull(message = "El ID del usuario es obligatorio")
    Long idUsuario
) {}
