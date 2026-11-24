package com.avantflix.avantflix_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avantflix.avantflix_backend.dto.DetallePedidoRequestDTO;
import com.avantflix.avantflix_backend.dto.DetallePedidoResponseDTO;
import com.avantflix.avantflix_backend.service.DetallePedidoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/detalles-pedido")
@CrossOrigin(origins = "http://localhost:5173")
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    public DetallePedidoController(DetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    @GetMapping
    public ResponseEntity<List<DetallePedidoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(detallePedidoService.listarTodos());
    }

    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<List<DetallePedidoResponseDTO>> listarPorPedido(@PathVariable Long idPedido) {
        return ResponseEntity.ok(detallePedidoService.listarPorPedido(idPedido));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetallePedidoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(detallePedidoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<DetallePedidoResponseDTO> crearDetalle(
            @Valid @RequestBody DetallePedidoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(detallePedidoService.crearDetalle(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetallePedidoResponseDTO> actualizarDetalle(
            @PathVariable Long id,
            @Valid @RequestBody DetallePedidoRequestDTO dto) {
        return ResponseEntity.ok(detallePedidoService.actualizarDetalle(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDetalle(@PathVariable Long id) {
        detallePedidoService.eliminarDetalle(id);
        return ResponseEntity.noContent().build();
    }
}
