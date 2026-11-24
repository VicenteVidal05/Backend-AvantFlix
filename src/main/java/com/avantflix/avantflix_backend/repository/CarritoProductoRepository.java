package com.avantflix.avantflix_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avantflix.avantflix_backend.model.CarritoProducto;
import com.avantflix.avantflix_backend.model.CarritoProductoId;

public interface CarritoProductoRepository extends JpaRepository<CarritoProducto, CarritoProductoId> {

    List<CarritoProducto> findByCarritoIdCarrito(Long idCarrito);
    void deleteByCarritoIdCarrito(Long idCarrito);
    
}
