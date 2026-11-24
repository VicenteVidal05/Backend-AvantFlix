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

import com.avantflix.avantflix_backend.dto.BlogRequestDTO;
import com.avantflix.avantflix_backend.dto.BlogResponseDTO;
import com.avantflix.avantflix_backend.service.BlogService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/blogs")
@CrossOrigin(origins = "http://localhost:5173")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    public ResponseEntity<List<BlogResponseDTO>> listarTodos() {
        return ResponseEntity.ok(blogService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(blogService.obtenerPorId(id));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<BlogResponseDTO>> listarPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(blogService.listarPorUsuario(idUsuario));
    }

    @GetMapping("/tag/{idTag}")
    public ResponseEntity<List<BlogResponseDTO>> listarPorTag(@PathVariable Long idTag) {
        return ResponseEntity.ok(blogService.listarPorTag(idTag));
    }

    @PostMapping
    public ResponseEntity<BlogResponseDTO> crearBlog(@Valid @RequestBody BlogRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blogService.crearBlog(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogResponseDTO> actualizarBlog(@PathVariable Long id,
            @Valid @RequestBody BlogRequestDTO dto) {
        return ResponseEntity.ok(blogService.actualizarBlog(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarBlog(@PathVariable Long id) {
        blogService.eliminarBlog(id);
        return ResponseEntity.noContent().build();
    }
}
