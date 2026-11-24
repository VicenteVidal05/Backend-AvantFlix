package com.avantflix.avantflix_backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.avantflix.avantflix_backend.dto.CarritoRequestDTO;
import com.avantflix.avantflix_backend.dto.CarritoResponseDTO;
import com.avantflix.avantflix_backend.model.Carrito;
import com.avantflix.avantflix_backend.model.Usuario;
import com.avantflix.avantflix_backend.repository.CarritoRepository;
import com.avantflix.avantflix_backend.repository.UsuarioRepository;

@Service
@Transactional(readOnly = true)
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final UsuarioRepository usuarioRepository;

    public CarritoService(CarritoRepository carritoRepository, UsuarioRepository usuarioRepository) {
        this.carritoRepository = carritoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private CarritoResponseDTO mapearADTO(Carrito carrito) {
        String nombreCompleto = carrito.getUsuario().getNombre() + " " + carrito.getUsuario().getApellidoP();
        return new CarritoResponseDTO(
                carrito.getIdCarrito(),
                carrito.getUsuario().getIdUsuario(),
                nombreCompleto
        );
    }

    public List<CarritoResponseDTO> listarTodos() {
        return carritoRepository.findAll().stream()
                .map(this::mapearADTO)
                .toList();
    }

    public CarritoResponseDTO obtenerPorId(Long id) {
        return carritoRepository.findById(id)
                .map(this::mapearADTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado"));
    }

    public CarritoResponseDTO obtenerPorUsuario(Long idUsuario) {
        return carritoRepository.findByUsuarioIdUsuario(idUsuario)
                .map(this::mapearADTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado para este usuario"));
    }

    @Transactional
    public CarritoResponseDTO crearCarrito(CarritoRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (carritoRepository.existsByUsuarioIdUsuario(dto.idUsuario())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario ya tiene un carrito asignado");
        }

        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);

        return mapearADTO(carritoRepository.save(carrito));
    }

    @Transactional
    public CarritoResponseDTO actualizarCarrito(Long id, CarritoRequestDTO dto) {
        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado"));

        Usuario usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        carrito.setUsuario(usuario);

        return mapearADTO(carritoRepository.save(carrito));
    }

    @Transactional
    public void eliminarCarrito(Long id) {
        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado"));
        carritoRepository.delete(carrito);
    }
}
