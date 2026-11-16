package com.avantflix.avantflix_backend.controller;

import com.avantflix.avantflix_backend.model.Region;
import com.avantflix.avantflix_backend.service.RegionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/regiones")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping
    public ResponseEntity<List<Region>> listarTodas() {
        return ResponseEntity.ok(regionService.listarTodas());
    }

    // Aquí te toca escribir a ti:
    // 1. @GetMapping("/{id}") -> si el Optional está presente devuelve 200 OK, si no, 404 Not Found.
    // 2. @PostMapping -> recibe @RequestBody Region region, guarda y devuelve 201 Created.
    // 3. @DeleteMapping("/{id}") -> elimina por ID y devuelve 204 No Content.

    @GetMapping("/{id}")
    public ResponseEntity<Region> obtenerPorId(@PathVariable Long id){
        return regionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Region> guardar(@RequestBody Region region){
        Region nuevaRegion = regionService.guardar(region);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaRegion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        regionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}