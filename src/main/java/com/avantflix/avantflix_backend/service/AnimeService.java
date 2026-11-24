package com.avantflix.avantflix_backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.avantflix.avantflix_backend.dto.AnimeRequestDTO;
import com.avantflix.avantflix_backend.dto.AnimeResponseDTO;
import com.avantflix.avantflix_backend.dto.ProductoResponseDTO;
import com.avantflix.avantflix_backend.model.Anime;
import com.avantflix.avantflix_backend.model.Producto;
import com.avantflix.avantflix_backend.repository.AnimeRepository;
import com.avantflix.avantflix_backend.repository.ProductoRepository;

@Service
@Transactional(readOnly = true)
public class AnimeService {

    private final AnimeRepository animeRepository;
    private final ProductoRepository productoRepository;

    public AnimeService(AnimeRepository animeRepository, ProductoRepository productoRepository) {
        this.animeRepository = animeRepository;
        this.productoRepository = productoRepository;
    }

    private AnimeResponseDTO mapearADTO(Anime a) {
        List<ProductoResponseDTO> productosDTO = a.getProductosRelacionados().stream()
                .map(p -> new ProductoResponseDTO(
                        p.getIdProducto(),
                        p.getNombre(),
                        p.getPrecio(),
                        p.getDescuento(),
                        p.getStock(),
                        p.getCategoria().getCategoria(),
                        p.getImagenUrl(),
                        p.getDescripcion()))
                .toList();

        return new AnimeResponseDTO(
                a.getIdAnime(),
                a.getTitulo(),
                a.getTemporada(),
                a.getGenero(),
                a.getSinopsis(),
                a.getImagenUrl(),
                a.getEpisodios(),
                a.getCalificacion(),
                productosDTO);
    }

    private List<Producto> resolverProductos(List<Long> idsProductos) {
        if (idsProductos == null || idsProductos.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> idsUnicos = idsProductos.stream().distinct().toList();
        List<Producto> productos = productoRepository.findAllById(idsUnicos);

        if (productos.size() != idsUnicos.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Uno o más productos relacionados no existen");
        }
        return productos;
    }

    public List<AnimeResponseDTO> listarTodos() {
        return animeRepository.findAll().stream()
                .map(this::mapearADTO)
                .toList();
    }

    public AnimeResponseDTO obtenerPorId(Long id) {
        return animeRepository.findById(id)
                .map(this::mapearADTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime no encontrado"));
    }

    public List<AnimeResponseDTO> buscarPorTitulo(String titulo) {
        return animeRepository.findByTituloContainingIgnoreCase(titulo).stream()
                .map(this::mapearADTO)
                .toList();
    }

    public List<AnimeResponseDTO> buscarPorGenero(String genero) {
        return animeRepository.findByGeneroContainingIgnoreCase(genero).stream()
                .map(this::mapearADTO)
                .toList();
    }

    @Transactional
    public AnimeResponseDTO guardar(AnimeRequestDTO dto) {
        Anime anime = new Anime();
        anime.setTitulo(dto.titulo());
        anime.setTemporada(dto.temporada());
        anime.setGenero(dto.genero());
        anime.setSinopsis(dto.sinopsis());
        anime.setImagenUrl(dto.imagenUrl());
        anime.setEpisodios(dto.episodios());
        anime.setCalificacion(dto.calificacion());
        anime.setProductosRelacionados(resolverProductos(dto.idsProductos()));

        return mapearADTO(animeRepository.save(anime));
    }

    @Transactional
    public AnimeResponseDTO actualizar(Long id, AnimeRequestDTO dto) {
        Anime anime = animeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime no encontrado"));

        anime.setTitulo(dto.titulo());
        anime.setTemporada(dto.temporada());
        anime.setGenero(dto.genero());
        anime.setSinopsis(dto.sinopsis());
        anime.setImagenUrl(dto.imagenUrl());
        anime.setEpisodios(dto.episodios());
        anime.setCalificacion(dto.calificacion());
        anime.setProductosRelacionados(resolverProductos(dto.idsProductos()));

        return mapearADTO(animeRepository.save(anime));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!animeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime no encontrado");
        }
        animeRepository.deleteById(id);
    }
}