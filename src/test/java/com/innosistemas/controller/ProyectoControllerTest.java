package com.innosistemas.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProyectoControllerTest {
    @Test
    void testControllerInstance() {
        com.innosistemas.service.ProyectoService mockService = org.mockito.Mockito.mock(com.innosistemas.service.ProyectoService.class);
        ProyectoController controller = new ProyectoController(mockService);
        assertNotNull(controller);
    }
}
