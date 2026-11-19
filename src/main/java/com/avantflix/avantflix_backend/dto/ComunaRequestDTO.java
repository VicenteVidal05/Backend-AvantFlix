package com.avantflix.avantflix_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ComunaRequestDTO(

    @NotBlank(message = "La comuna es obigatoria")
    @Size(max = 45)
    String comuna,

    @NotNull(message = "Debe indicar una Region")
    Long idRegion
) {}
