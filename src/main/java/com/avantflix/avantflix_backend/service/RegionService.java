package com.avantflix.avantflix_backend.service;

import com.avantflix.avantflix_backend.model.Region;
import com.avantflix.avantflix_backend.repository.RegionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RegionService {

    private final RegionRepository regionRepository;

    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    public List<Region> listarTodas() {
        return regionRepository.findAll();
    }

    public Optional<Region> obtenerPorId( Long id){
        return regionRepository.findById(id);
    }

    public Region guardar(Region region){
        return regionRepository.save(region);
    }

    public boolean eliminar(Long id){
        if (!regionRepository.existsById(id)){
            return false;
        }
        regionRepository.deleteById(id);
        return true;
    }
}