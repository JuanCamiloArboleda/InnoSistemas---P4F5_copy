package com.innosistemas.controller;

import com.innosistemas.entity.Usuario;
import com.innosistemas.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
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

    /** 1️⃣ Obtener info de estudiante existente */
    @Test
        void testGetEstudianteInfoExistente() {
                // Arrange:
                Principal principal = mock(Principal.class);
                when(principal.getName()).thenReturn("estudiante@test.com");
                Usuario usuario = new Usuario();
                usuario.setCorreo("estudiante@test.com");
                usuario.setContrasenia("password123");
                when(usuarioRepository.findByCorreo("estudiante@test.com"))
                                .thenReturn(Optional.of(usuario));

                // Act:
                ResponseEntity<Usuario> response = usuarioController.getEstudianteInfo(principal);

                // Assert:
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertEquals("estudiante@test.com", response.getBody().getCorreo());
                assertNull(response.getBody().getContrasenia(), "La contraseña debe ocultarse");
                verify(usuarioRepository, times(1)).findByCorreo("estudiante@test.com");
        }

    /** 2️⃣ Obtener info de estudiante no existente */
    @Test
    void testGetEstudianteInfoNoExistente() {
        // Arrange:
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("noexiste@test.com");
        when(usuarioRepository.findByCorreo("noexiste@test.com"))
                .thenReturn(Optional.empty());

        // Act:
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> usuarioController.getEstudianteInfo(principal));

        // Assert:
        assertEquals("Estudiante no encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findByCorreo("noexiste@test.com");
    }

    /** 3️⃣ Obtener info de administrador existente */
    @Test
        void testGetAdminInfoExistente() {
                // Arrange:
                Principal principal = mock(Principal.class);
                when(principal.getName()).thenReturn("admin@test.com");
                Usuario usuario = new Usuario();
                usuario.setCorreo("admin@test.com");
                usuario.setContrasenia("hash");
                when(usuarioRepository.findByCorreo("admin@test.com"))
                                .thenReturn(Optional.of(usuario));

                // Act:
                ResponseEntity<Usuario> response = usuarioController.getAdminInfo(principal);

                // Assert:
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertEquals("admin@test.com", response.getBody().getCorreo());
                assertNull(response.getBody().getContrasenia());
                verify(usuarioRepository, times(1)).findByCorreo("admin@test.com");
        }

    /** 4️⃣ Obtener info de administrador no existente */
    @Test
    void testGetAdminInfoNoExistente() {
        // Arrange:
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("noadmin@test.com");
        when(usuarioRepository.findByCorreo("noadmin@test.com"))
                .thenReturn(Optional.empty());

        // Act:
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> usuarioController.getAdminInfo(principal));

        // Assert:
        assertEquals("Admin no encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findByCorreo("noadmin@test.com");
    }

    /** 5️⃣ Obtener info de profesor existente */
    @Test
        void testGetProfesorInfoExistente() {
                // Arrange:
                Principal principal = mock(Principal.class);
                when(principal.getName()).thenReturn("profesor@test.com");
                Usuario usuario = new Usuario();
                usuario.setCorreo("profesor@test.com");
                usuario.setContrasenia("12345");
                when(usuarioRepository.findByCorreo("profesor@test.com"))
                                .thenReturn(Optional.of(usuario));

                // Act:
                ResponseEntity<Usuario> response = usuarioController.getProfesorInfo(principal);

                // Assert:
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());
                assertEquals("profesor@test.com", response.getBody().getCorreo());
                assertNull(response.getBody().getContrasenia());
                verify(usuarioRepository, times(1)).findByCorreo("profesor@test.com");
        }

    /** 6️⃣ Obtener info de profesor no existente (extra defensivo) */
    @Test
    void testGetProfesorInfoNoExistente() {
        // Arrange:
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("noprofesor@test.com");
        when(usuarioRepository.findByCorreo("noprofesor@test.com"))
                .thenReturn(Optional.empty());

        // Act:
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> usuarioController.getProfesorInfo(principal));

        // Assert:
        assertEquals("Profesor no encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findByCorreo("noprofesor@test.com");
    }
        @Test
        void testGetEstudianteInfoUsuarioNoExiste() {
            // Arrange:
            Principal principal = mock(Principal.class);
            when(principal.getName()).thenReturn("noexiste@test.com");
            when(usuarioRepository.findByCorreo("noexiste@test.com")).thenReturn(Optional.empty());

            // Act:
            RuntimeException exception = assertThrows(RuntimeException.class, () -> usuarioController.getEstudianteInfo(principal));

            // Assert:
            assertEquals("Estudiante no encontrado", exception.getMessage());
            verify(usuarioRepository, times(1)).findByCorreo("noexiste@test.com");
        }
}
