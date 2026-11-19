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

import com.avantflix.avantflix_backend.dto.ComunaRequestDTO;
import com.avantflix.avantflix_backend.dto.ComunaResponseDTO;
import com.avantflix.avantflix_backend.service.ComunaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/comunas")
@CrossOrigin(origins = "http://localhost:5173")
public class ComunaController {

    private final ComunaService comunaService;

    public ComunaController(ComunaService comunaService) {
        this.comunaService = comunaService;
    }

    @GetMapping
    public ResponseEntity<List<ComunaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(comunaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComunaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(comunaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ComunaResponseDTO> guardar(@Valid @RequestBody ComunaRequestDTO dto) {
        ComunaResponseDTO nuevaComuna = comunaService.guardar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaComuna);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComunaResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ComunaRequestDTO dto){
        ComunaResponseDTO comunaActualizada = comunaService.actualizar(id, dto);
        return ResponseEntity.ok(comunaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        comunaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
