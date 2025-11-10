package com.innosistemas.controller;

import com.innosistemas.entity.Documento;
import com.innosistemas.entity.Usuario;
import com.innosistemas.repository.DocumentoRepository;
import com.innosistemas.repository.UsuarioRepository;
import com.innosistemas.security.AuthorizationService;
import com.innosistemas.service.DocumentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocumentoControllerTest {
    @Mock
    private DocumentoService documentoService;
    @Mock
    private DocumentoRepository documentoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private AuthorizationService authorizationService;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private DocumentoController documentoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadDocumento() throws IOException {
        when(authentication.getName()).thenReturn("user@test.com");
        Usuario usuario = new Usuario();
        usuario.setId(1);
        when(usuarioRepository.findByCorreo("user@test.com")).thenReturn(Optional.of(usuario));
        when(authorizationService.tieneAccesoAProyecto(1, 1)).thenReturn(true);
        MultipartFile file = mock(MultipartFile.class);
        Documento documento = new Documento();
        when(documentoService.uploadAndSaveDocumento(file, "titulo", 1, 1)).thenReturn(documento);
        ResponseEntity<?> response = documentoController.uploadDocumento(file, "titulo", 1, authentication);
        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void testGetDocumentosPorProyecto() {
        when(authentication.getName()).thenReturn("user@test.com");
        Usuario usuario = new Usuario();
        usuario.setId(1);
        when(usuarioRepository.findByCorreo("user@test.com")).thenReturn(Optional.of(usuario));
        when(authorizationService.tieneAccesoAProyecto(1, 1)).thenReturn(true);
        Documento doc1 = new Documento();
        Documento doc2 = new Documento();
        when(documentoService.findAllDocumentosByProyectoId(1)).thenReturn(Arrays.asList(doc1, doc2));
        ResponseEntity<?> response = documentoController.getDocumentosPorProyecto(1, authentication);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof List);
    }

    @Test
    void testGetDocumentosPorProyectoSinAcceso() {
        when(authentication.getName()).thenReturn("user@test.com");
        Usuario usuario = new Usuario();
        usuario.setId(1);
        when(usuarioRepository.findByCorreo("user@test.com")).thenReturn(Optional.of(usuario));
        when(authorizationService.tieneAccesoAProyecto(1, 1)).thenReturn(false);
        ResponseEntity<?> response = documentoController.getDocumentosPorProyecto(1, authentication);
        assertNotNull(response);
        assertEquals(403, response.getStatusCodeValue());
    }

    @Test
    void testDownloadDocumento() throws IOException {
        when(authentication.getName()).thenReturn("user@test.com");
        Usuario usuario = new Usuario();
        usuario.setId(1);
        when(usuarioRepository.findByCorreo("user@test.com")).thenReturn(Optional.of(usuario));
        Documento documento = new Documento();
        documento.setProyectoId(1);
        documento.setRutaDoc("gridfsid");
        when(documentoRepository.findById(1)).thenReturn(Optional.of(documento));
        when(authorizationService.tieneAccesoAProyecto(1, 1)).thenReturn(true);
        GridFsResource resource = mock(GridFsResource.class);
        when(resource.getContentType()).thenReturn("application/pdf");
        when(resource.getFilename()).thenReturn("documento.pdf");
        when(documentoService.downloadByGridFsId("gridfsid")).thenReturn(resource);
        ResponseEntity<?> response = documentoController.downloadDocumento(1, authentication);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }
}
