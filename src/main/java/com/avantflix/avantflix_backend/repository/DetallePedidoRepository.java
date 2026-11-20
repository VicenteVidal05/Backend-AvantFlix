package com.avantflix.avantflix_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.avantflix.avantflix_backend.model.DetallePedido;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    
}