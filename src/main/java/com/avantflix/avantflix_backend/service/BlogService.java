package com.avantflix.avantflix_backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.avantflix.avantflix_backend.dto.BlogRequestDTO;
import com.avantflix.avantflix_backend.dto.BlogResponseDTO;
import com.avantflix.avantflix_backend.model.Blog;
import com.avantflix.avantflix_backend.model.Tag;
import com.avantflix.avantflix_backend.model.Usuario;
import com.avantflix.avantflix_backend.repository.BlogRepository;
import com.avantflix.avantflix_backend.repository.TagRepository;
import com.avantflix.avantflix_backend.repository.UsuarioRepository;

@Service
@Transactional(readOnly = true)
public class BlogService {

    private final BlogRepository blogRepository;
    private final TagRepository tagRepository;
    private final UsuarioRepository usuarioRepository;

    public BlogService(BlogRepository blogRepository, TagRepository tagRepository,
            UsuarioRepository usuarioRepository) {
        this.blogRepository = blogRepository;
        this.tagRepository = tagRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private BlogResponseDTO mapearADTO(Blog blog) {
        String nombreCompleto = blog.getUsuario().getNombre() + " " + blog.getUsuario().getApellidoP();

        return new BlogResponseDTO(
                blog.getIdBlog(),
                blog.getTitulo(),
                blog.getDescripcion(),
                blog.getTag().getIdTag(),
                blog.getTag().getTag(),
                blog.getUsuario().getIdUsuario(),
                nombreCompleto
        );
    }

    public List<BlogResponseDTO> listarTodos() {
        return blogRepository.findAll().stream()
                .map(this::mapearADTO)
                .toList();
    }

    public BlogResponseDTO obtenerPorId(Long id) {
        return blogRepository.findById(id)
                .map(this::mapearADTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog no encontrado"));
    }

    public List<BlogResponseDTO> listarPorUsuario(Long idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        return blogRepository.findByUsuarioIdUsuario(idUsuario).stream()
                .map(this::mapearADTO)
                .toList();
    }

    public List<BlogResponseDTO> listarPorTag(Long idTag) {
        if (!tagRepository.existsById(idTag)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag no encontrado");
        }
        return blogRepository.findByTagIdTag(idTag).stream()
                .map(this::mapearADTO)
                .toList();
    }

    @Transactional
    public BlogResponseDTO crearBlog(BlogRequestDTO dto) {
        Tag tag = tagRepository.findById(dto.idTag())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag no encontrado"));

        Usuario usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Blog blog = new Blog();
        blog.setTitulo(dto.titulo());
        blog.setDescripcion(dto.descripcion());
        blog.setTag(tag);
        blog.setUsuario(usuario);

        return mapearADTO(blogRepository.save(blog));
    }

    @Transactional
    public BlogResponseDTO actualizarBlog(Long id, BlogRequestDTO dto) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog no encontrado"));

        Tag tag = tagRepository.findById(dto.idTag())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag no encontrado"));

        Usuario usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        blog.setTitulo(dto.titulo());
        blog.setDescripcion(dto.descripcion());
        blog.setTag(tag);
        blog.setUsuario(usuario);

        return mapearADTO(blogRepository.save(blog));
    }

    @Transactional
    public void eliminarBlog(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog no encontrado"));
        blogRepository.delete(blog);
    }
}
