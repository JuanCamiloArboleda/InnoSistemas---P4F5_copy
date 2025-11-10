package com.innosistemas.controller;

import com.innosistemas.service.EquipoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

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
    void testCreateEquipo() {
        when(equipoService.createEquipoConUsuarios(anyString(), anyString(), anyList())).thenReturn(null);
        ResponseEntity<?> response = equipoController.createEquipo("nombre", "desc", Collections.emptyList(), authentication);
        assertNotNull(response);
    }
}
