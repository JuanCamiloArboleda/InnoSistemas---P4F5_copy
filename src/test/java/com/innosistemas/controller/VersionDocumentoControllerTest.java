package com.innosistemas.controller;

import com.innosistemas.entity.Documento;
import com.innosistemas.entity.Usuario;
import com.innosistemas.entity.VersionDocumento;
import com.innosistemas.repository.DocumentoRepository;
import com.innosistemas.repository.UsuarioRepository;
import com.innosistemas.service.VersionDocumentoService;
import com.innosistemas.security.AuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VersionDocumentoControllerTest {
    @Mock
    VersionDocumentoService versionDocumentoService;
    @Mock
    DocumentoRepository documentoRepository;
    @Mock
    UsuarioRepository usuarioRepository;
    @Mock
    AuthorizationService authorizationService;
    @InjectMocks
    VersionDocumentoController versionDocumentoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerVersionesDocumentoUsuarioAutorizado() {
        Integer documentoId = 1;
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user@test.com");
        Usuario usuario = new Usuario();
        usuario.setId(10);
        usuario.setCorreo("user@test.com");
        Documento doc = new Documento();
        doc.setId(documentoId);
        doc.setProyectoId(2);
        when(documentoRepository.findById(documentoId)).thenReturn(Optional.of(doc));
        when(usuarioRepository.findByCorreo("user@test.com")).thenReturn(Optional.of(usuario));
        when(authorizationService.tieneAccesoAProyecto(10, 2)).thenReturn(true);
        VersionDocumento v1 = new VersionDocumento();
        VersionDocumento v2 = new VersionDocumento();
        when(versionDocumentoService.obtenerVersionesDocumento(documentoId)).thenReturn(Arrays.asList(v1, v2));

        ResponseEntity<?> response = versionDocumentoController.obtenerVersionesDocumento(documentoId, authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof List);
        verify(versionDocumentoService, times(1)).obtenerVersionesDocumento(documentoId);
    }

    @Test
    void testObtenerVersionesDocumentoUsuarioNoAutorizado() {
        Integer documentoId = 1;
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user@test.com");
        Usuario usuario = new Usuario();
        usuario.setId(10);
        usuario.setCorreo("user@test.com");
        Documento doc = new Documento();
        doc.setId(documentoId);
        doc.setProyectoId(2);
        when(documentoRepository.findById(documentoId)).thenReturn(Optional.of(doc));
        when(usuarioRepository.findByCorreo("user@test.com")).thenReturn(Optional.of(usuario));
        when(authorizationService.tieneAccesoAProyecto(10, 2)).thenReturn(false);

        ResponseEntity<?> response = versionDocumentoController.obtenerVersionesDocumento(documentoId, authentication);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("No tienes permiso para ver las versiones de este documento.", response.getBody());
        verify(versionDocumentoService, never()).obtenerVersionesDocumento(any());
    }
}
