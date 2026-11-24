package com.avantflix.avantflix_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avantflix.avantflix_backend.model.Rol;

public interface RolRepository extends JpaRepository<Rol,Long>{

    Optional<Rol> findByRolIgnoreCase(String rol);

}
