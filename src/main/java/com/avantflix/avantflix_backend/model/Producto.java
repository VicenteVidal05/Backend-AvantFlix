package com.avantflix.avantflix_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
@Entity 
@Table(name = "Producto")
public class Producto {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "precio", nullable = false)
    private Integer precio;

    @Column(name = "descuento", nullable = false)
    private Integer descuento;

    @Column(name = "stock", nullable = false)
    private Integer stock;
    
    @ManyToOne
    @JoinColumn(name = "idCategoria", nullable = false)
    private Categoria categoria;

    @Column(name = "imagenUrl", nullable = true, length = 255)
    private String imagenUrl;

    @Column(name = "descripcion", nullable = true, columnDefinition = "TEXT")
    private String descripcion;

}
