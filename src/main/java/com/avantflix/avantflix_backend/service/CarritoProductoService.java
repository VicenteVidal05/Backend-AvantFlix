package com.avantflix.avantflix_backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.avantflix.avantflix_backend.dto.CarritoProductoRequestDTO;
import com.avantflix.avantflix_backend.dto.CarritoProductoResponseDTO;
import com.avantflix.avantflix_backend.model.Carrito;
import com.avantflix.avantflix_backend.model.CarritoProducto;
import com.avantflix.avantflix_backend.model.CarritoProductoId;
import com.avantflix.avantflix_backend.model.Producto;
import com.avantflix.avantflix_backend.repository.CarritoProductoRepository;
import com.avantflix.avantflix_backend.repository.CarritoRepository;
import com.avantflix.avantflix_backend.repository.ProductoRepository;

@Service
@Transactional(readOnly = true)
public class CarritoProductoService {

    private final CarritoProductoRepository carritoProductoRepository;
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;

    public CarritoProductoService(
            CarritoProductoRepository carritoProductoRepository,
            CarritoRepository carritoRepository,
            ProductoRepository productoRepository) {
        this.carritoProductoRepository = carritoProductoRepository;
        this.carritoRepository = carritoRepository;
        this.productoRepository = productoRepository;
    }

    private CarritoProductoResponseDTO mapearADTO(CarritoProducto item) {
        Producto producto = item.getProducto();
        int descuento = producto.getDescuento() != null ? producto.getDescuento() : 0;
        int precioFinal = producto.getPrecio() - (producto.getPrecio() * descuento / 100);
        int subtotal = precioFinal * item.getCantidad();

        return new CarritoProductoResponseDTO(
                item.getCarrito().getIdCarrito(),
                producto.getIdProducto(),
                producto.getNombre(),
                precioFinal,
                item.getCantidad(),
                subtotal
        );
    }

    public List<CarritoProductoResponseDTO> listarTodos() {
        return carritoProductoRepository.findAll().stream()
                .map(this::mapearADTO)
                .toList();
    }

    public List<CarritoProductoResponseDTO> listarPorCarrito(Long idCarrito) {
        if (!carritoRepository.existsById(idCarrito)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado");
        }
        return carritoProductoRepository.findByCarritoIdCarrito(idCarrito).stream()
                .map(this::mapearADTO)
                .toList();
    }

    public CarritoProductoResponseDTO obtenerPorId(Long idCarrito, Long idProducto) {
        CarritoProductoId id = new CarritoProductoId(idCarrito, idProducto);
        CarritoProducto item = carritoProductoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado en el carrito"));
        return mapearADTO(item);
    }

    @Transactional
    public CarritoProductoResponseDTO agregarOActualizarProducto(CarritoProductoRequestDTO dto) {
        Carrito carrito = carritoRepository.findById(dto.idCarrito())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado"));

        Producto producto = productoRepository.findById(dto.idProducto())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        int cantidadAAgregar = (dto.cantidad() != null) ? dto.cantidad() : 1;

        CarritoProductoId id = new CarritoProductoId(dto.idCarrito(), dto.idProducto());
        CarritoProducto item = carritoProductoRepository.findById(id).orElse(null);

        if (item != null) {
            int nuevaCantidad = item.getCantidad() + cantidadAAgregar;
            if (producto.getStock() < nuevaCantidad) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Stock insuficiente para el producto: " + producto.getNombre());
            }
            item.setCantidad(nuevaCantidad);
        } else {
            if (producto.getStock() < cantidadAAgregar) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Stock insuficiente para el producto: " + producto.getNombre());
            }
            item = new CarritoProducto();
            item.setId(id);
            item.setCarrito(carrito);
            item.setProducto(producto);
            item.setCantidad(cantidadAAgregar);
        }

        return mapearADTO(carritoProductoRepository.save(item));
    }

    @Transactional
    public CarritoProductoResponseDTO actualizarCantidad(Long idCarrito, Long idProducto, Integer cantidad) {
        int nuevaCantidad = (cantidad != null) ? cantidad : 1;
        if (nuevaCantidad < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad mínima es 1");
        }

        CarritoProductoId id = new CarritoProductoId(idCarrito, idProducto);
        CarritoProducto item = carritoProductoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado en el carrito"));

        if (item.getProducto().getStock() < nuevaCantidad) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Stock insuficiente para el producto: " + item.getProducto().getNombre());
        }

        item.setCantidad(nuevaCantidad);
        return mapearADTO(carritoProductoRepository.save(item));
    }

    @Transactional
    public void eliminarProductoDelCarrito(Long idCarrito, Long idProducto) {
        CarritoProductoId id = new CarritoProductoId(idCarrito, idProducto);
        CarritoProducto item = carritoProductoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado en el carrito"));
        carritoProductoRepository.delete(item);
    }

    @Transactional
    public void vaciarCarrito(Long idCarrito) {
        if (!carritoRepository.existsById(idCarrito)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado");
        }
        carritoProductoRepository.deleteByCarritoIdCarrito(idCarrito);
    }
}
