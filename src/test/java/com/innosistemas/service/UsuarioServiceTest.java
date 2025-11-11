package com.innosistemas.service;

import com.innosistemas.entity.Usuario;
import com.innosistemas.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllUsuarios() {
        // Arrange:
        Usuario u1 = new Usuario();
        Usuario u2 = new Usuario();
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(u1, u2));

        // Act:
        List<Usuario> usuarios = usuarioService.findAllUsuarios();

        // Assert:
        assertEquals(2, usuarios.size());
    }

    @Test
    void testFindUsuarioByIdFound() {
        // Arrange:
        Usuario u = new Usuario();
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(u));

        // Act:
        Optional<Usuario> result = usuarioService.findUsuarioById(1);

        // Assert:
        assertTrue(result.isPresent());
    }

    @Test
    void testFindUsuarioByIdNotFound() {
        // Arrange:
        when(usuarioRepository.findById(2)).thenReturn(Optional.empty());

        // Act:
        Optional<Usuario> result = usuarioService.findUsuarioById(2);

        // Assert:
        assertFalse(result.isPresent());
    }

    @Test
    void testSaveUsuario() {
        // Arrange:
        Usuario u = new Usuario();
        when(usuarioRepository.save(u)).thenReturn(u);

        // Act:
        Usuario result = usuarioService.saveUsuario(u);

        // Assert:
        assertEquals(u, result);
    }

    @Test
    void testDeleteUsuarioById() {
        // Arrange:
        doNothing().when(usuarioRepository).deleteById(1);

        // Act:
        usuarioService.deleteUsuarioById(1);

        // Assert:
        verify(usuarioRepository, times(1)).deleteById(1);
    }
}
