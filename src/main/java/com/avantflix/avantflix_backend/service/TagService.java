package com.avantflix.avantflix_backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.avantflix.avantflix_backend.model.Tag;
import com.avantflix.avantflix_backend.repository.TagRepository;

@Service
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> listarTodos() {
        return tagRepository.findAll();
    }

    public Tag obtenerPorId(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag no encontrado"));
    }

    @Transactional
    public Tag crearTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    public Tag actualizarTag(Long id, Tag tagActualizado) {
        Tag tag = obtenerPorId(id);
        tag.setTag(tagActualizado.getTag());
        return tagRepository.save(tag);
    }

    @Transactional
    public void eliminarTag(Long id) {
        Tag tag = obtenerPorId(id);
        tagRepository.delete(tag);
    }
}
