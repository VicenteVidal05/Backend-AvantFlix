package com.avantflix.avantflix_backend.dto;

import jakarta.validation.constraints.NotNull;

public record CarritoRequestDTO(

    @NotNull(message = "El ID del usuario es obligatorio")
    Long idUsuario
) {}
