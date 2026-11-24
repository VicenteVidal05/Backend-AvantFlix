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

import com.avantflix.avantflix_backend.model.Tag;
import com.avantflix.avantflix_backend.service.TagService;

@RestController
@RequestMapping("/api/v1/tags")
@CrossOrigin(origins = "http://localhost:5173")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<Tag>> listarTodos() {
        return ResponseEntity.ok(tagService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Tag> crearTag(@RequestBody Tag tag) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.crearTag(tag));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tag> actualizarTag(@PathVariable Long id, @RequestBody Tag tag) {
        return ResponseEntity.ok(tagService.actualizarTag(id, tag));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTag(@PathVariable Long id) {
        tagService.eliminarTag(id);
        return ResponseEntity.noContent().build();
    }
}
