package com.avantflix.avantflix_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avantflix.avantflix_backend.model.DetallePedido;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    List<DetallePedido> findByPedidoIdPedido(Long idPedido);
}
