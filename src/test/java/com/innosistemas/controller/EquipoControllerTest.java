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
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipoControllerTest {
    @Mock
    private EquipoService equipoService;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private EquipoController equipoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEquipoSinUsuarios() {
        when(authentication.getName()).thenReturn("admin@test.com");
        Equipo equipo = new Equipo();
        equipo.setNombre("Equipo Test");
        when(equipoService.createEquipoConUsuarios(anyString(), anyString(), anyList())).thenReturn(equipo);
        ResponseEntity<Equipo> response = equipoController.createEquipo("Equipo Test", "Descripción", Collections.emptyList(), authentication);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(equipoService, times(1)).createEquipoConUsuarios(anyString(), anyString(), anyList());
    }

    @Test
    void testCreateEquipoConUsuarios() {
        when(authentication.getName()).thenReturn("admin@test.com");
        Equipo equipo = new Equipo();
        equipo.setNombre("Equipo Test");
        when(equipoService.createEquipoConUsuarios(anyString(), anyString(), anyList())).thenReturn(equipo);
        ResponseEntity<Equipo> response = equipoController.createEquipo("Equipo Test", "Descripción", Arrays.asList("user1@test.com", "user2@test.com"), authentication);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Equipo Test", response.getBody().getNombre());
    }
}
