package com.avantflix.avantflix_backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.avantflix.avantflix_backend.dto.DetallePedidoRequestDTO;
import com.avantflix.avantflix_backend.dto.DetallePedidoResponseDTO;
import com.avantflix.avantflix_backend.model.DetallePedido;
import com.avantflix.avantflix_backend.model.Pedido;
import com.avantflix.avantflix_backend.model.Producto;
import com.avantflix.avantflix_backend.repository.DetallePedidoRepository;
import com.avantflix.avantflix_backend.repository.PedidoRepository;
import com.avantflix.avantflix_backend.repository.ProductoRepository;

@Service
@Transactional(readOnly = true)
public class DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public DetallePedidoService(
            DetallePedidoRepository detallePedidoRepository,
            PedidoRepository pedidoRepository,
            ProductoRepository productoRepository) {
        this.detallePedidoRepository = detallePedidoRepository;
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    private DetallePedidoResponseDTO mapearADTO(DetallePedido detalle) {
        return new DetallePedidoResponseDTO(
                detalle.getIdDetallePedido(),
                detalle.getProducto().getIdProducto(),
                detalle.getPedido().getIdPedido(),
                detalle.getProducto().getNombre(),
                detalle.getCantidad(),
                detalle.getTotal(),
                detalle.getIva());
    }

    public List<DetallePedidoResponseDTO> listarTodos() {
        return detallePedidoRepository.findAll()
                .stream()
                .map(this::mapearADTO)
                .toList();
    }

    public List<DetallePedidoResponseDTO> listarPorPedido(Long idPedido) {
        return detallePedidoRepository.findByPedidoIdPedido(idPedido)
                .stream()
                .map(this::mapearADTO)
                .toList();
    }

    public DetallePedidoResponseDTO obtenerPorId(Long id) {
        DetallePedido detalle = detallePedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle de pedido no encontrado"));
        return mapearADTO(detalle);
    }

    @Transactional
    public DetallePedidoResponseDTO crearDetalle(DetallePedidoRequestDTO dto) {
        if (dto.idPedido() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del pedido es obligatorio");
        }

        Pedido pedido = pedidoRepository.findById(dto.idPedido())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));

        Producto producto = productoRepository.findById(dto.idProducto())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        if (producto.getStock() < dto.cantidad()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Stock insuficiente para el producto: " + producto.getNombre());
        }

        producto.setStock(producto.getStock() - dto.cantidad());
        productoRepository.save(producto);

        int[] montos = calcularMontos(producto, dto.cantidad());

        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(pedido);
        detalle.setProducto(producto);
        detalle.setCantidad(dto.cantidad());
        detalle.setTotal(montos[0]);
        detalle.setIva(montos[1]);

        DetallePedido guardado = detallePedidoRepository.save(detalle);

        pedido.setTotal(pedido.getTotal() + montos[0]);
        pedidoRepository.save(pedido);

        return mapearADTO(guardado);
    }


    @Transactional
    public DetallePedidoResponseDTO actualizarDetalle(Long id, DetallePedidoRequestDTO dto) {
        DetallePedido detalle = detallePedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle de pedido no encontrado"));

        Pedido pedido = detalle.getPedido();
        int totalAnteriorDetalle = detalle.getTotal();

        Producto productoAnterior = detalle.getProducto();
        productoAnterior.setStock(productoAnterior.getStock() + detalle.getCantidad());
        productoRepository.save(productoAnterior);

        Producto nuevoProducto = productoRepository.findById(dto.idProducto())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        if (nuevoProducto.getStock() < dto.cantidad()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Stock insuficiente para el producto: " + nuevoProducto.getNombre());
        }

        nuevoProducto.setStock(nuevoProducto.getStock() - dto.cantidad());
        productoRepository.save(nuevoProducto);

        int[] montos = calcularMontos(nuevoProducto, dto.cantidad());

        detalle.setProducto(nuevoProducto);
        detalle.setCantidad(dto.cantidad());
        detalle.setTotal(montos[0]);
        detalle.setIva(montos[1]);

        DetallePedido actualizado = detallePedidoRepository.save(detalle);

        pedido.setTotal((pedido.getTotal() - totalAnteriorDetalle) + montos[0]);
        pedidoRepository.save(pedido);

        return mapearADTO(actualizado);
    }

    @Transactional
    public void eliminarDetalle(Long id) {
        DetallePedido detalle = detallePedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle de pedido no encontrado"));

        Producto producto = detalle.getProducto();
        producto.setStock(producto.getStock() + detalle.getCantidad());
        productoRepository.save(producto);

        Pedido pedido = detalle.getPedido();
        pedido.setTotal(pedido.getTotal() - detalle.getTotal());
        pedidoRepository.save(pedido);

        detallePedidoRepository.delete(detalle);
    }

    private int[] calcularMontos(Producto producto, int cantidad) {
        int descuento = producto.getDescuento() != null ? producto.getDescuento() : 0;
        int precioUnitarioFinal = producto.getPrecio() - (producto.getPrecio() * descuento / 100);
        int subtotal = precioUnitarioFinal * cantidad;
        int iva = (int) Math.round(subtotal - (subtotal / 1.19));
        return new int[] { subtotal, iva };
    }

}
