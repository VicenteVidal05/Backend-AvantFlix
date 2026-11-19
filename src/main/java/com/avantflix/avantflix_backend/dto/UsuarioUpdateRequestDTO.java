package com.avantflix.avantflix_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateRequestDTO(

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 45, message = "El nombre no puede superar los 45 caracteres")
    String nombre,

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(max = 45, message = "El apellido paterno no puede superar los 45 caracteres")
    String apellidoP,

    @NotBlank(message = "El apellido materno es obligatorio")
    @Size(max = 45, message = "El apellido materno no puede superar los 45 caracteres")
    String apellidoM,

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo no es válido")
    @Size(max = 100, message = "El correo no puede superar los 100 caracteres")
    String correo,

    @Size(min = 6, max = 255, message = "Si desea cambiar la contraseña, debe tener entre 6 y 255 caracteres")
    String clave,

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 45, message = "La dirección no puede superar los 45 caracteres")
    String direccion,

    @NotNull(message = "Debe indicar un rol")
    Long idRol,

    @NotNull(message = "Debe indicar una comuna")
    Long idComuna

) {}
