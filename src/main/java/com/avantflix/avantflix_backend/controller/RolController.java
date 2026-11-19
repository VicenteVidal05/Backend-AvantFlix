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

import com.avantflix.avantflix_backend.model.Rol;
import com.avantflix.avantflix_backend.service.RolService;

@RestController
@RequestMapping("/api/v1/roles")
@CrossOrigin(origins = "http://localhost:5173")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public ResponseEntity<List<Rol>> listarTodos() {
        return ResponseEntity.ok(rolService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rol> obtenerPorId(@PathVariable Long id) {
        return rolService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Rol> guardar(@RequestBody Rol rol) {
        Rol nuevoRol = rolService.guardar(rol);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRol);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!rolService.eliminar(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rol> actualizar(@PathVariable Long id, @RequestBody Rol rol) {
        return rolService.actualizar(id, rol)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
