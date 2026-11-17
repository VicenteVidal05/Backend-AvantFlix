package com.avantflix.avantflix_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avantflix.avantflix_backend.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

}
