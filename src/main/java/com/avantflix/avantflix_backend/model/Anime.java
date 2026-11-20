package com.avantflix.avantflix_backend.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "Anime")
public class Anime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAnime;

    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name = "temporada", nullable = false, length = 45)
    private String temporada;

    @Column(name = "genero", nullable = false, length = 100)
    private String genero;

    @Column(name = "sinopsis", columnDefinition = "TEXT")
    private String sinopsis;

    @Column(name = "imagenUrl", length = 255)
    private String imagenUrl;

    @Column(name = "episodios", nullable = false)
    private Integer episodios;

    @Column(name = "calificacion", nullable = false)
    private Double calificacion;

    @ManyToMany
    @JoinTable(
        name = "AnimeProducto",
        joinColumns = @JoinColumn(name = "idAnime"),
        inverseJoinColumns = @JoinColumn(name = "idProducto")
    )
    private List<Producto> productosRelacionados = new ArrayList<>();
}