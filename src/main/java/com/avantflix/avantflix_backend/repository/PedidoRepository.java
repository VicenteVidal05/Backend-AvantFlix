package com.avantflix.avantflix_backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.avantflix.avantflix_backend.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuarioIdUsuario(Long idUsuario);

}