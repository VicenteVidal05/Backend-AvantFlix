package com.avantflix.avantflix_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.avantflix.avantflix_backend.model.Rol;
import com.avantflix.avantflix_backend.repository.RolRepository;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository){
        this.rolRepository = rolRepository;
    }

    public List<Rol> listarTodos() {
        return rolRepository.findAll();
    }

    public Optional<Rol> obtenerPorId( Long id){
        return rolRepository.findById(id);
    }

    public Rol guardar(Rol rol){
        return rolRepository.save(rol);
    }

    public boolean eliminar(Long id){
        if (!rolRepository.existsById(id)){
            return false;
        }
        rolRepository.deleteById(id);
        return true;
    }

    public Optional<Rol> actualizar(Long id, Rol datosNuevos){

        return rolRepository.findById(id).map(rolExistente -> {
            rolExistente.setRol(datosNuevos.getRol());
            return rolRepository.save(rolExistente);
        });

    }

}
