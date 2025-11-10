package com.innosistemas.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioControllerTest {
    @Test
    void testControllerInstance() {
        com.innosistemas.repository.UsuarioRepository mockRepo = org.mockito.Mockito.mock(com.innosistemas.repository.UsuarioRepository.class);
        UsuarioController controller = new UsuarioController(mockRepo);
        assertNotNull(controller);
    }
}
