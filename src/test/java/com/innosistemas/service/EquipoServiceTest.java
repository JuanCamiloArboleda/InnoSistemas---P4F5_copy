package com.innosistemas.service;

import com.innosistemas.entity.Equipo;
import com.innosistemas.entity.Usuario;
import com.innosistemas.entity.UsuarioEquipo;
import com.innosistemas.repository.EquipoRepository;
import com.innosistemas.repository.UsuarioEquipoRepository;
import com.innosistemas.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipoServiceTest {
    @Mock
    private EquipoRepository equipoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private UsuarioEquipoRepository usuarioEquipoRepository;
    @InjectMocks
    private EquipoService equipoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEquipoConUsuariosSinUsuarios() {
        // Arrange
        when(equipoRepository.save(any(Equipo.class))).thenReturn(new Equipo());

        // Act
        Equipo equipo = equipoService.createEquipoConUsuarios("nombre", "desc", Collections.emptyList());

        // Assert
        assertNotNull(equipo);
        verify(equipoRepository, times(1)).save(any(Equipo.class));
        verify(usuarioEquipoRepository, never()).save(any(UsuarioEquipo.class));
    }

    @Test
    void testCreateEquipoConUsuariosConUsuario() {
        // Arrange
        Equipo equipoMock = new Equipo();
        equipoMock.setId(10);
        when(equipoRepository.save(any(Equipo.class))).thenReturn(equipoMock);

        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(20);
        when(usuarioRepository.findByCorreo("correo@ejemplo.com"))
                .thenReturn(Optional.of(usuarioMock));

        when(usuarioEquipoRepository.save(any(UsuarioEquipo.class)))
                .thenReturn(new UsuarioEquipo());

        // Act
        Equipo equipo = equipoService.createEquipoConUsuarios(
                "nombre",
                "desc",
                Collections.singletonList("correo@ejemplo.com")
        );

        // Assert
        assertNotNull(equipo);
        verify(usuarioRepository, times(1)).findByCorreo("correo@ejemplo.com");
        verify(usuarioEquipoRepository, times(1)).save(any(UsuarioEquipo.class));
    }

    @Test
    void testCreateEquipoConUsuariosUsuarioNoEncontrado() {
        // Arrange
        Equipo equipoMock = new Equipo();
        equipoMock.setId(10);
        when(equipoRepository.save(any(Equipo.class))).thenReturn(equipoMock);
        when(usuarioRepository.findByCorreo("noexiste@ejemplo.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            equipoService.createEquipoConUsuarios(
                    "nombre",
                    "desc",
                    Collections.singletonList("noexiste@ejemplo.com")
            );
        });
        assertTrue(ex.getMessage().contains("Usuario no encontrado"));
    }
} // ✅ Cierre de la clase añadido correctamente
