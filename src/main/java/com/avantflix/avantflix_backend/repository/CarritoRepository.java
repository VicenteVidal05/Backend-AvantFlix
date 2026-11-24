package com.avantflix.avantflix_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avantflix.avantflix_backend.model.Carrito;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    Optional<Carrito> findByUsuarioIdUsuario(Long idUsuario);
    boolean existsByUsuarioIdUsuario(Long idUsuario);
    
}
