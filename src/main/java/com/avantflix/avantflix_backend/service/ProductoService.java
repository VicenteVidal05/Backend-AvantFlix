package com.avantflix.avantflix_backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.avantflix.avantflix_backend.dto.ProductoRequestDTO;
import com.avantflix.avantflix_backend.dto.ProductoResponseDTO;
import com.avantflix.avantflix_backend.model.Categoria;
import com.avantflix.avantflix_backend.model.Producto;
import com.avantflix.avantflix_backend.repository.CategoriaRepository;
import com.avantflix.avantflix_backend.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    private ProductoResponseDTO mapearADTO(Producto p) {
        return new ProductoResponseDTO(
                p.getIdProducto(),
                p.getNombre(),
                p.getPrecio(),
                p.getStock(),
                p.getCategoria().getCategoria(),
                p.getImagenUrl(),
                p.getDescripcion(),
                p.getEsDigital());
    }

    public List<ProductoResponseDTO> listarTodos() {
        return productoRepository.findAll().stream()
                .map(this::mapearADTO)
                .toList();
    }

    public ProductoResponseDTO obtenerPorId(Long id) {
    return productoRepository.findById(id)
            .map(this::mapearADTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
}

    public ProductoResponseDTO crear(ProductoRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.idCategoria())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));

        Producto producto = new Producto();
        producto.setNombre(dto.nombre());
        producto.setPrecio(dto.precio());
        producto.setStock(dto.esDigital() ? 0 : dto.stock()); // si es digital, stock 0
        producto.setCategoria(categoria);
        producto.setImagenUrl(dto.imagenUrl());
        producto.setDescripcion(dto.descripcion());
        producto.setEsDigital(dto.esDigital());

        Producto productoGuardado = productoRepository.save(producto);
        return mapearADTO(productoGuardado);
    }

    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        Categoria categoria = categoriaRepository.findById(dto.idCategoria())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));

        producto.setNombre(dto.nombre());
        producto.setPrecio(dto.precio());
        producto.setStock(dto.esDigital() ? 0 : dto.stock());
        producto.setCategoria(categoria);
        producto.setImagenUrl(dto.imagenUrl());
        producto.setDescripcion(dto.descripcion());
        producto.setEsDigital(dto.esDigital());

        Producto productoActualizado = productoRepository.save(producto);
        return mapearADTO(productoActualizado);
    }

    public void eliminar(Long id) {
    if (!productoRepository.existsById(id)) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
    }
    productoRepository.deleteById(id);
}

}