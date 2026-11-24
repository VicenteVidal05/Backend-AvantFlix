package com.avantflix.avantflix_backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.avantflix.avantflix_backend.dto.UsuarioRequestDTO;
import com.avantflix.avantflix_backend.dto.UsuarioResponseDTO;
import com.avantflix.avantflix_backend.dto.UsuarioUpdateRequestDTO;
import com.avantflix.avantflix_backend.model.Carrito;
import com.avantflix.avantflix_backend.model.Comuna;
import com.avantflix.avantflix_backend.model.Rol;
import com.avantflix.avantflix_backend.model.Usuario;
import com.avantflix.avantflix_backend.repository.CarritoRepository;
import com.avantflix.avantflix_backend.repository.ComunaRepository;
import com.avantflix.avantflix_backend.repository.RolRepository;
import com.avantflix.avantflix_backend.repository.UsuarioRepository;

@Service
@Transactional(readOnly = true)
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final ComunaRepository comunaRepository;
    private final CarritoRepository carritoRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, ComunaRepository comunaRepository,
            RolRepository rolRepository, CarritoRepository carritoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.comunaRepository = comunaRepository;
        this.rolRepository = rolRepository;
        this.carritoRepository = carritoRepository;
    }

    private UsuarioResponseDTO mapearADTO(Usuario u) {
        return new UsuarioResponseDTO(
                u.getIdUsuario(),
                u.getNombre(),
                u.getApellidoP(),
                u.getApellidoM(),
                u.getCorreo(),
                u.getDireccion(),
                u.getRol().getRol(),
                u.getComuna().getComuna());
    }

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::mapearADTO)
                .toList();
    }

    public UsuarioResponseDTO obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(this::mapearADTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    @Transactional
    public UsuarioResponseDTO crear(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByCorreo(dto.correo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya está registrado");
        }

        Rol rol = rolRepository.findById(dto.idRol())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado"));

        Comuna comuna = comunaRepository.findById(dto.idComuna())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comuna no encontrada"));

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.nombre());
        usuario.setApellidoP(dto.apellidoP());
        usuario.setApellidoM(dto.apellidoM());
        usuario.setCorreo(dto.correo());
        usuario.setClave(dto.clave()); // cambiar por: passwordEncoder.encode(dto.clave()) al agregar Security
        usuario.setDireccion(dto.direccion());
        usuario.setRol(rol);
        usuario.setComuna(comuna);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        Carrito nuevoCarrito = new Carrito();
        nuevoCarrito.setUsuario(usuarioGuardado);
        carritoRepository.save(nuevoCarrito);

        return mapearADTO(usuarioGuardado);
    }

    @Transactional
    public UsuarioResponseDTO actualizar(Long id, UsuarioUpdateRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (!usuario.getCorreo().equals(dto.correo()) && usuarioRepository.existsByCorreo(dto.correo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya está en uso por otro usuario");
        }

        Rol rol = rolRepository.findById(dto.idRol())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado"));

        Comuna comuna = comunaRepository.findById(dto.idComuna())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comuna no encontrada"));

        usuario.setNombre(dto.nombre());
        usuario.setApellidoP(dto.apellidoP());
        usuario.setApellidoM(dto.apellidoM());
        usuario.setCorreo(dto.correo());
        usuario.setDireccion(dto.direccion());
        usuario.setRol(rol);
        usuario.setComuna(comuna);

        if (dto.clave() != null && !dto.clave().isBlank()) {
            usuario.setClave(dto.clave()); // Cambiar por passwordEncoder.encode(dto.clave()) al agregar Security
        }

        return mapearADTO(usuarioRepository.save(usuario));
    }

    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        carritoRepository.findByUsuarioIdUsuario(id)
                .ifPresent(carritoRepository::delete);

        usuarioRepository.delete(usuario);
    }

}