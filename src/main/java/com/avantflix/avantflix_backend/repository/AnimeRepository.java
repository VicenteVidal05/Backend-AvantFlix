package com.avantflix.avantflix_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avantflix.avantflix_backend.model.Anime;

public interface AnimeRepository extends JpaRepository<Anime, Long> {

    List<Anime> findByTituloContainingIgnoreCase(String titulo);
    List<Anime> findByGeneroContainingIgnoreCase(String genero);
    
}