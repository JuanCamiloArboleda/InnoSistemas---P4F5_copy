package com.innosistemas.controller;

import com.innosistemas.entity.Equipo;
import com.innosistemas.service.EquipoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipoControllerTest {
    @Mock
    private EquipoService equipoService;

    @InjectMocks
    private EquipoController equipoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEquipoIncluyeUsuarioActual() {
        String nombre = "Equipo Alpha";
        String descripcion = "Equipo de pruebas";
        List<String> correosUsuarios = Arrays.asList("user1@test.com", "user2@test.com");
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("admin@test.com");

        Equipo equipoMock = new Equipo();
        equipoMock.setId(1);
        equipoMock.setNombre(nombre);
        equipoMock.setDescripcion(descripcion);
        // El equipoService debe recibir la lista con el usuario actual incluido
        List<String> expectedCorreos = Arrays.asList("user1@test.com", "user2@test.com", "admin@test.com");
        when(equipoService.createEquipoConUsuarios(eq(nombre), eq(descripcion), eq(expectedCorreos))).thenReturn(equipoMock);

        ResponseEntity<Equipo> response = equipoController.createEquipo(nombre, descripcion, correosUsuarios, authentication);
        assertNotNull(response);
        assertEquals(1, response.getBody().getId());
        assertEquals(nombre, response.getBody().getNombre());
        assertEquals(descripcion, response.getBody().getDescripcion());
        verify(equipoService, times(1)).createEquipoConUsuarios(eq(nombre), eq(descripcion), eq(expectedCorreos));
    }
}
