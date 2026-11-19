package com.avantflix.avantflix_backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.avantflix.avantflix_backend.dto.ComunaRequestDTO;
import com.avantflix.avantflix_backend.dto.ComunaResponseDTO;
import com.avantflix.avantflix_backend.model.Comuna;
import com.avantflix.avantflix_backend.model.Region;
import com.avantflix.avantflix_backend.repository.ComunaRepository;
import com.avantflix.avantflix_backend.repository.RegionRepository;

@Service
public class ComunaService {

    private final ComunaRepository comunaRepository;
    private final RegionRepository regionRepository;

    public ComunaService(ComunaRepository comunaRepository, RegionRepository regionRepository) {
        this.comunaRepository = comunaRepository;
        this.regionRepository = regionRepository;
    }

    private ComunaResponseDTO mapearADTO(Comuna c) {
        return new ComunaResponseDTO(
                c.getIdComuna(),
                c.getComuna(),
                c.getRegion().getIdRegion(),
                c.getRegion().getRegion());
    }

    public List<ComunaResponseDTO> listarTodos() {
        return comunaRepository.findAll().stream()
                .map(this::mapearADTO)
                .toList();
    }

    public ComunaResponseDTO obtenerPorId(Long id) {
        return comunaRepository.findById(id)
                .map(this::mapearADTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comuna no encontrada"));
    }

    public ComunaResponseDTO guardar(ComunaRequestDTO dto) {
        Region region = regionRepository.findById(dto.idRegion())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Región no encontrada"));

        Comuna comuna = new Comuna();
        comuna.setComuna(dto.comuna());
        comuna.setRegion(region);

        Comuna comunaGuardada = comunaRepository.save(comuna);
        return mapearADTO(comunaGuardada);
    }

    public ComunaResponseDTO actualizar(Long id, ComunaRequestDTO dto) {
        Comuna comuna = comunaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comuna no encontrada"));

        Region region = regionRepository.findById(dto.idRegion())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Región no encontrada"));

        comuna.setComuna(dto.comuna());
        comuna.setRegion(region);

        Comuna comunaActualizada = comunaRepository.save(comuna);
        return mapearADTO(comunaActualizada);
    }

    public void eliminar(Long id) {
        if (!comunaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comuna no encontrada");
        }
        comunaRepository.deleteById(id);
    }

}
