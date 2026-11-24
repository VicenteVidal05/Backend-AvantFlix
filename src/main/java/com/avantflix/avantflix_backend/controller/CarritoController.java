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

import com.avantflix.avantflix_backend.dto.CarritoRequestDTO;
import com.avantflix.avantflix_backend.dto.CarritoResponseDTO;
import com.avantflix.avantflix_backend.service.CarritoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/carritos")
@CrossOrigin(origins = "http://localhost:5173")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping
    public ResponseEntity<List<CarritoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(carritoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarritoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(carritoService.obtenerPorId(id));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<CarritoResponseDTO> obtenerPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(carritoService.obtenerPorUsuario(idUsuario));
    }

    @PostMapping
    public ResponseEntity<CarritoResponseDTO> crearCarrito(@Valid @RequestBody CarritoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carritoService.crearCarrito(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarritoResponseDTO> actualizarCarrito(@PathVariable Long id,
            @Valid @RequestBody CarritoRequestDTO dto) {
        return ResponseEntity.ok(carritoService.actualizarCarrito(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCarrito(@PathVariable Long id) {
        carritoService.eliminarCarrito(id);
        return ResponseEntity.noContent().build();
    }
}
