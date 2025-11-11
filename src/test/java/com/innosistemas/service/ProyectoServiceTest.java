package com.innosistemas.service;

import com.innosistemas.entity.Proyecto;
import com.innosistemas.repository.ProyectoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProyectoServiceTest {
    @Mock
    private ProyectoRepository proyectoRepository;
    @InjectMocks
    private ProyectoService proyectoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveProyecto() {
        // Arrange:
        Proyecto p = new Proyecto();
        when(proyectoRepository.save(p)).thenReturn(p);

        // Act:
        Proyecto result = proyectoService.saveProyecto(p);

        // Assert:
        assertEquals(p, result);
    }
}
