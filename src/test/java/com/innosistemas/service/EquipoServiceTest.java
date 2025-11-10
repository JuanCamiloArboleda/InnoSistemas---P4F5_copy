package com.innosistemas.service;

import com.innosistemas.entity.Equipo;
import com.innosistemas.repository.EquipoRepository;
import com.innosistemas.repository.UsuarioEquipoRepository;
import com.innosistemas.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

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
    void testCreateEquipoConUsuarios() {
        when(equipoRepository.save(any(Equipo.class))).thenReturn(new Equipo());
        Equipo equipo = equipoService.createEquipoConUsuarios("nombre", "desc", Collections.emptyList());
        assertNotNull(equipo);
    }
}
