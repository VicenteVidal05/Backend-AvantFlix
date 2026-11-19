package com.avantflix.avantflix_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avantflix.avantflix_backend.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

    boolean existsByCorreo(String correo);

}
