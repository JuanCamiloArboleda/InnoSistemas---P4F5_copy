package com.innosistemas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import com.innosistemas.entity.Usuario;
import com.innosistemas.repository.UsuarioRepository;
import java.security.Principal;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
    private UsuarioRepository usuarioRepository;

    UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * endpoint accesible sólo por estudiantes.
     */
    @PreAuthorize("hasRole('ESTUDIANTE')")
    @GetMapping("/estudiante/me")
    public ResponseEntity<Usuario> getEstudianteInfo(Principal principal) {
        Usuario u = usuarioRepository.findByCorreo(principal.getName())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
        u.setContrasenia(null); // No mostramos la contraseña aunque este hasheada
        return ResponseEntity.ok(u);
    }

    /**
     * endpoint accesible sólo por administradores.
     */
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/admin/me")
    public ResponseEntity<Usuario> getAdminInfo(Principal principal) {
        Usuario u = usuarioRepository.findByCorreo(principal.getName())
                .orElseThrow(() -> new RuntimeException("Admin no encontrado"));
        u.setContrasenia(null); // No mostramos la contraseña aunque este hasheada
        return ResponseEntity.ok(u);
    }

    /**
     * endpoint accesible solo por profesores.
     */
    @PreAuthorize("hasRole('PROFESOR')")
    @GetMapping("/profesor/me")
    public ResponseEntity<Usuario> getProfesorInfo(Principal principal) {
        Usuario u = usuarioRepository.findByCorreo(principal.getName())
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));
        u.setContrasenia(null); // No mostramos la contraseña aunque este hasheada
        return ResponseEntity.ok(u);
    }
}
