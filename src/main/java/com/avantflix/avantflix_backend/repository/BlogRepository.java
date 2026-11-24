package com.avantflix.avantflix_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avantflix.avantflix_backend.model.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    List<Blog> findByUsuarioIdUsuario(Long idUsuario);
    List<Blog> findByTagIdTag(Long idTag);
    
}
