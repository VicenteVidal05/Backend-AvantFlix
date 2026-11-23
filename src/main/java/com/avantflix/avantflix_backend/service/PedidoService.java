package com.avantflix.avantflix_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.avantflix.avantflix_backend.dto.DetallePedidoRequestDTO;
import com.avantflix.avantflix_backend.dto.DetallePedidoResponseDTO;
import com.avantflix.avantflix_backend.dto.PedidoRequestDTO;
import com.avantflix.avantflix_backend.dto.PedidoResponseDTO;
import com.avantflix.avantflix_backend.model.DetallePedido;
import com.avantflix.avantflix_backend.model.Pedido;
import com.avantflix.avantflix_backend.model.Producto;
import com.avantflix.avantflix_backend.model.Usuario;
import com.avantflix.avantflix_backend.repository.PedidoRepository;
import com.avantflix.avantflix_backend.repository.ProductoRepository;
import com.avantflix.avantflix_backend.repository.UsuarioRepository;

@Service
@Transactional(readOnly = true)
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository, UsuarioRepository usuarioRepository,
            ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    private PedidoResponseDTO mapearADTO(Pedido pedido) {
        List<DetallePedidoResponseDTO> detallesDTO = pedido.getDetalles().stream()
                .map(d -> new DetallePedidoResponseDTO(
                        d.getIdDetallePedido(),
                        d.getProducto().getIdProducto(),
                        d.getProducto().getNombre(),
                        d.getCantidad(),
                        d.getTotal(),
                        d.getIva()))
                .toList();

        String nombreCompleto = pedido.getUsuario().getNombre() + " " + pedido.getUsuario().getApellidoP();

        return new PedidoResponseDTO(
                pedido.getIdPedido(),
                pedido.getFecha(),
                pedido.getTotal(),
                pedido.getUsuario().getIdUsuario(),
                nombreCompleto,
                detallesDTO);
    }

    public List<PedidoResponseDTO> listarTodos() {
        return pedidoRepository.findAll().stream()
                .map(this::mapearADTO)
                .toList();
    }

    public PedidoResponseDTO obtenerPorId(Long id) {
        return pedidoRepository.findById(id)
                .map(this::mapearADTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
    }

    public List<PedidoResponseDTO> listarPorUsuario(Long idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        return pedidoRepository.findByUsuarioIdUsuario(idUsuario).stream()
                .map(this::mapearADTO)
                .toList();
    }

    @Transactional
    public PedidoResponseDTO actualizarPedido(Long id, PedidoRequestDTO dto) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));

        Usuario usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        for (DetallePedido detalleAnterior : pedido.getDetalles()) {
            Producto prodAnterior = detalleAnterior.getProducto();
            prodAnterior.setStock(prodAnterior.getStock() + detalleAnterior.getCantidad());
            productoRepository.save(prodAnterior);
        }

        pedido.getDetalles().clear();
        pedido.setUsuario(usuario);

        int totalPedido = 0;

        for (DetallePedidoRequestDTO item : dto.detalles()) {
            Producto producto = productoRepository.findById(item.idProducto())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Producto no encontrado con ID: " + item.idProducto()));

            if (producto.getStock() < item.cantidad()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Stock insuficiente para el producto: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - item.cantidad());
            productoRepository.save(producto);

            int descuento = producto.getDescuento() != null ? producto.getDescuento() : 0;
            int precioUnitarioFinal = producto.getPrecio() - (producto.getPrecio() * descuento / 100);
            int subtotalLinea = precioUnitarioFinal * item.cantidad();
            int ivaLinea = (int) Math.round(subtotalLinea - (subtotalLinea / 1.19));

            DetallePedido nuevoDetalle = new DetallePedido();
            nuevoDetalle.setProducto(producto);
            nuevoDetalle.setCantidad(item.cantidad());
            nuevoDetalle.setTotal(subtotalLinea);
            nuevoDetalle.setIva(ivaLinea);

            pedido.agregarDetalle(nuevoDetalle);
            totalPedido += subtotalLinea;
        }

        pedido.setTotal(totalPedido);
        Pedido pedidoActualizado = pedidoRepository.save(pedido);
        return mapearADTO(pedidoActualizado);
    }

    @Transactional
    public PedidoResponseDTO crearPedido(PedidoRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFecha(LocalDateTime.now());

        int totalPedido = 0;

        for (DetallePedidoRequestDTO item : dto.detalles()) {
            Producto producto = productoRepository.findById(item.idProducto())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Producto no encontrado con ID: " + item.idProducto()));

            if (producto.getStock() < item.cantidad()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Stock insuficiente para el producto: " + producto.getNombre());
            }
            producto.setStock(producto.getStock() - item.cantidad());
            productoRepository.save(producto);

            int descuento = producto.getDescuento() != null ? producto.getDescuento() : 0;
            int precioUnitarioFinal = producto.getPrecio() - (producto.getPrecio() * descuento / 100);
            int subtotalLinea = precioUnitarioFinal * item.cantidad();
            int ivaLinea = (int) Math.round(subtotalLinea - (subtotalLinea / 1.19));

            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(producto);
            detalle.setCantidad(item.cantidad());
            detalle.setTotal(subtotalLinea);
            detalle.setIva(ivaLinea);

            pedido.agregarDetalle(detalle);
            totalPedido += subtotalLinea;
        }

        pedido.setTotal(totalPedido);
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        return mapearADTO(pedidoGuardado);
    }

    @Transactional
    public void eliminarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));

        for (DetallePedido detalle : pedido.getDetalles()) {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);
        }

        pedidoRepository.delete(pedido);
    }

}