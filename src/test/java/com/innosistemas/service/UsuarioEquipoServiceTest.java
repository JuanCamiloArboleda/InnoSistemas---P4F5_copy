package com.innosistemas.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioEquipoServiceTest {
    @Test
    void testInstancia() {
        UsuarioEquipoService service = new UsuarioEquipoService();
        assertNotNull(service);
    }
}
