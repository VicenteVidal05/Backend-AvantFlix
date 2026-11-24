package com.avantflix.avantflix_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 45)
    String nombre,

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(max = 45)
    String apellidoP,

    @NotBlank(message = "El apellido materno es obligatorio")
    @Size(max = 45)
    String apellidoM,

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo no es valido")
    @Size(max = 100)
    String correo,

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(max = 255)
    String clave,

    @NotBlank(message = "La direccion es obligatorio")
    @Size(max = 150)
    String direccion,

    Long idRol,

    @NotNull(message = "Debe indicar una comuna")
    Long idComuna
) {}
