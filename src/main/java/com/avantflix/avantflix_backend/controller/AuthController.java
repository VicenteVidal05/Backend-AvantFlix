package com.avantflix.avantflix_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.avantflix.avantflix_backend.dto.AuthResponseDTO;
import com.avantflix.avantflix_backend.dto.LoginRequestDTO;
import com.avantflix.avantflix_backend.dto.UsuarioRequestDTO;
import com.avantflix.avantflix_backend.dto.UsuarioResponseDTO;
import com.avantflix.avantflix_backend.model.Carrito;
import com.avantflix.avantflix_backend.model.Usuario;
import com.avantflix.avantflix_backend.repository.CarritoRepository;
import com.avantflix.avantflix_backend.repository.UsuarioRepository;
import com.avantflix.avantflix_backend.security.JwtService;
import com.avantflix.avantflix_backend.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final CarritoRepository carritoRepository;
    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public AuthController(
            AuthenticationManager authenticationManager,
            UsuarioRepository usuarioRepository,
            CarritoRepository carritoRepository,
            UsuarioService usuarioService,
            JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.carritoRepository = carritoRepository;
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.correo(), dto.clave())
        );

        Usuario usuario = usuarioRepository.findByCorreo(dto.correo())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        String token = jwtService.generarToken(usuario);
        Long idCarrito = carritoRepository.findByUsuarioIdUsuario(usuario.getIdUsuario())
                .map(Carrito::getIdCarrito)
                .orElse(null);

        return ResponseEntity.ok(new AuthResponseDTO(
                token,
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getApellidoP(),
                usuario.getCorreo(),
                usuario.getRol().getRol(),
                idCarrito
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO creado = usuarioService.crear(dto);

        Usuario usuario = usuarioRepository.findById(creado.idUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        String token = jwtService.generarToken(usuario);
        Long idCarrito = carritoRepository.findByUsuarioIdUsuario(usuario.getIdUsuario())
                .map(Carrito::getIdCarrito)
                .orElse(null);

        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponseDTO(
                token,
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getApellidoP(),
                usuario.getCorreo(),
                usuario.getRol().getRol(),
                idCarrito
        ));
    }
}
