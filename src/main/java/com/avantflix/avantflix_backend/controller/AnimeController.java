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

import com.avantflix.avantflix_backend.dto.AnimeRequestDTO;
import com.avantflix.avantflix_backend.dto.AnimeResponseDTO;
import com.avantflix.avantflix_backend.service.AnimeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/animes")
@CrossOrigin(origins = "http://localhost:5173")
public class AnimeController {

    private final AnimeService animeService;

    public AnimeController(AnimeService animeService) {
        this.animeService = animeService;
    }

    @GetMapping
    public ResponseEntity<List<AnimeResponseDTO>> listarTodos() {
        return ResponseEntity.ok(animeService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimeResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(animeService.obtenerPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<AnimeResponseDTO>> buscarPorTitulo(@RequestParam String titulo) {
        return ResponseEntity.ok(animeService.buscarPorTitulo(titulo));
    }

    @GetMapping("/genero")
    public ResponseEntity<List<AnimeResponseDTO>> buscarPorGenero(@RequestParam String genero) {
        return ResponseEntity.ok(animeService.buscarPorGenero(genero));
    }

    @PostMapping
    public ResponseEntity<AnimeResponseDTO> guardar(@Valid @RequestBody AnimeRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(animeService.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimeResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody AnimeRequestDTO dto) {
        return ResponseEntity.ok(animeService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        animeService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}