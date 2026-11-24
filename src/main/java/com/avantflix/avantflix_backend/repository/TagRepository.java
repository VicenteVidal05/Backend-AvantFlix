package com.avantflix.avantflix_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avantflix.avantflix_backend.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

}
