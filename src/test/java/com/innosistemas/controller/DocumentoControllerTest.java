package com.innosistemas.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DocumentoControllerTest {
    @Test
    void testControllerInstance() {
        com.innosistemas.service.DocumentoService mockService = org.mockito.Mockito.mock(com.innosistemas.service.DocumentoService.class);
        com.innosistemas.repository.DocumentoRepository mockDocRepo = org.mockito.Mockito.mock(com.innosistemas.repository.DocumentoRepository.class);
        com.innosistemas.repository.UsuarioRepository mockUserRepo = org.mockito.Mockito.mock(com.innosistemas.repository.UsuarioRepository.class);
        com.innosistemas.security.AuthorizationService mockAuthService = org.mockito.Mockito.mock(com.innosistemas.security.AuthorizationService.class);
        DocumentoController controller = new DocumentoController(mockService, mockDocRepo, mockUserRepo, mockAuthService);
        assertNotNull(controller);
    }
}
