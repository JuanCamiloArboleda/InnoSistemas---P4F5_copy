package com.innosistemas.controller;

import com.innosistemas.entity.Usuario;
import com.innosistemas.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEstudianteInfo() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("estudiante@test.com");
        Usuario usuario = new Usuario();
        usuario.setCorreo("estudiante@test.com");
        usuario.setContrasenia("hashedPassword");
        when(usuarioRepository.findByCorreo("estudiante@test.com")).thenReturn(Optional.of(usuario));
        ResponseEntity<Usuario> response = usuarioController.getEstudianteInfo(principal);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNull(response.getBody().getContrasenia());
    }

    @Test
    void testGetEstudianteInfoNotFound() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("noexiste@test.com");
        when(usuarioRepository.findByCorreo("noexiste@test.com")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            usuarioController.getEstudianteInfo(principal);
        });
    }

    @Test
    void testGetAdminInfo() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("admin@test.com");
        Usuario usuario = new Usuario();
        usuario.setCorreo("admin@test.com");
        usuario.setContrasenia("hashedPassword");
        when(usuarioRepository.findByCorreo("admin@test.com")).thenReturn(Optional.of(usuario));
        ResponseEntity<Usuario> response = usuarioController.getAdminInfo(principal);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNull(response.getBody().getContrasenia());
    }

    @Test
    void testGetProfesorInfo() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("profesor@test.com");
        Usuario usuario = new Usuario();
        usuario.setCorreo("profesor@test.com");
        usuario.setContrasenia("hashedPassword");
        when(usuarioRepository.findByCorreo("profesor@test.com")).thenReturn(Optional.of(usuario));
        ResponseEntity<Usuario> response = usuarioController.getProfesorInfo(principal);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNull(response.getBody().getContrasenia());
    }
}
