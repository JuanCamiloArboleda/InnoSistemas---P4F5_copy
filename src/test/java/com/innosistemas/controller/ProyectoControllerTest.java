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
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto Test");
        proyecto.setDescripcion("Descripción Test");
        proyecto.setEquipoId(1);
        when(proyectoService.saveProyecto(any(Proyecto.class))).thenReturn(proyecto);
        ResponseEntity<Proyecto> response = proyectoController.createProyecto("Proyecto Test", "Descripción Test", 1);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Proyecto Test", response.getBody().getNombre());
    }
}
