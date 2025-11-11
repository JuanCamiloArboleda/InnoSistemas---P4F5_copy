package com.innosistemas.controller;

import com.innosistemas.entity.Proyecto;
import com.innosistemas.service.ProyectoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProyectoControllerTest {
    @Mock
    private ProyectoService proyectoService;
    @InjectMocks
    private ProyectoController proyectoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProyecto() {
        // Arrange:
        String nombre = "Proyecto Test";
        String descripcion = "Descripción de prueba";
        Integer equipoId = 42;
        Proyecto proyectoMock = new Proyecto();
        proyectoMock.setId(1);
        proyectoMock.setNombre(nombre);
        proyectoMock.setDescripcion(descripcion);
        proyectoMock.setEquipoId(equipoId);
        when(proyectoService.saveProyecto(any(Proyecto.class))).thenReturn(proyectoMock);

        // Act:
        ResponseEntity<Proyecto> response = proyectoController.createProyecto(nombre, descripcion, equipoId);

        // Assert:
        assertNotNull(response);
        assertEquals(1, response.getBody().getId());
        assertEquals(nombre, response.getBody().getNombre());
        assertEquals(descripcion, response.getBody().getDescripcion());
        assertEquals(equipoId, response.getBody().getEquipoId());
        verify(proyectoService, times(1)).saveProyecto(any(Proyecto.class));
    }
    @Test
    void testCreateProyectoParametrosNulos() {
        // Arrange:
        when(proyectoService.saveProyecto(any(Proyecto.class))).thenReturn(null);

        // Act:
        ResponseEntity<Proyecto> response = proyectoController.createProyecto(null, null, null);

        // Assert:
        assertNull(response.getBody());
        verify(proyectoService, times(1)).saveProyecto(any(Proyecto.class));
    }
}
