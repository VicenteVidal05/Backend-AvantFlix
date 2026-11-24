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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.avantflix.avantflix_backend.dto.CarritoProductoRequestDTO;
import com.avantflix.avantflix_backend.dto.CarritoProductoResponseDTO;
import com.avantflix.avantflix_backend.service.CarritoProductoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/carrito-productos")
@CrossOrigin(origins = "http://localhost:5173")
public class CarritoProductoController {

    private final CarritoProductoService carritoProductoService;

    public CarritoProductoController(CarritoProductoService carritoProductoService) {
        this.carritoProductoService = carritoProductoService;
    }

    @GetMapping
    public ResponseEntity<List<CarritoProductoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(carritoProductoService.listarTodos());
    }

    @GetMapping("/carrito/{idCarrito}")
    public ResponseEntity<List<CarritoProductoResponseDTO>> listarPorCarrito(@PathVariable Long idCarrito) {
        return ResponseEntity.ok(carritoProductoService.listarPorCarrito(idCarrito));
    }

    @GetMapping("/carrito/{idCarrito}/producto/{idProducto}")
    public ResponseEntity<CarritoProductoResponseDTO> obtenerPorId(
            @PathVariable Long idCarrito,
            @PathVariable Long idProducto) {
        return ResponseEntity.ok(carritoProductoService.obtenerPorId(idCarrito, idProducto));
    }

    @PostMapping
    public ResponseEntity<CarritoProductoResponseDTO> agregarProducto(
            @Valid @RequestBody CarritoProductoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carritoProductoService.agregarOActualizarProducto(dto));
    }

    @PutMapping("/carrito/{idCarrito}/producto/{idProducto}")
    public ResponseEntity<CarritoProductoResponseDTO> actualizarCantidad(
            @PathVariable Long idCarrito,
            @PathVariable Long idProducto,
            @RequestParam(defaultValue = "1") Integer cantidad) {
        return ResponseEntity.ok(carritoProductoService.actualizarCantidad(idCarrito, idProducto, cantidad));
    }

    @DeleteMapping("/carrito/{idCarrito}/producto/{idProducto}")
    public ResponseEntity<Void> eliminarProducto(
            @PathVariable Long idCarrito,
            @PathVariable Long idProducto) {
        carritoProductoService.eliminarProductoDelCarrito(idCarrito, idProducto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/carrito/{idCarrito}")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long idCarrito) {
        carritoProductoService.vaciarCarrito(idCarrito);
        return ResponseEntity.noContent().build();
    }
}
