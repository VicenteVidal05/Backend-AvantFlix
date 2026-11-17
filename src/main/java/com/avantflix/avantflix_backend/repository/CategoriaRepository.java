package com.avantflix.avantflix_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avantflix.avantflix_backend.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
