package com.innosistemas.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioEquipoServiceTest {
    @Test
    void testInstancia() {
        // Arrange:
        // No hay preparación especial

        // Act:
        UsuarioEquipoService service = new UsuarioEquipoService();

        // Assert:
        assertNotNull(service);
    }
}
