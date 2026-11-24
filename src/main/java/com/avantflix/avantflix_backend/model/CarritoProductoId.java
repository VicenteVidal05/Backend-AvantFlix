package com.avantflix.avantflix_backend.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class CarritoProductoId implements Serializable {

    @Column(name = "idCarrito")
    private Long idCarrito;

    @Column(name = "idProducto")
    private Long idProducto;
}
