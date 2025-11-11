package com.innosistemas.controller;

import com.innosistemas.entity.Documento;
import com.innosistemas.entity.Usuario;
import com.innosistemas.service.DocumentoService;
import com.innosistemas.repository.DocumentoRepository;
import com.innosistemas.repository.UsuarioRepository;
import com.innosistemas.security.AuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocumentoControllerTest {
    @Test
    void testUploadDocumentoThrowsException() throws Exception {
        // Arrange
        MultipartFile file = mock(MultipartFile.class);
        String titulo = "DocTest";
        Integer proyectoId = 1;
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user@test.com");
        Usuario usuario = new Usuario();
        usuario.setId(10);
        usuario.setCorreo("user@test.com");
        when(usuarioRepository.findByCorreo("user@test.com")).thenReturn(java.util.Optional.of(usuario));
        when(authorizationService.tieneAccesoAProyecto(10, proyectoId)).thenReturn(true);
        when(documentoService.uploadAndSaveDocumento(file, titulo, proyectoId, 10)).thenThrow(new RuntimeException("Error interno"));

        // Act
        ResponseEntity<?> response = documentoController.uploadDocumento(file, titulo, proyectoId, authentication);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateDocumentoUsuarioNoAutorizado() throws Exception {
        // Arrange
        Integer documentoId = 1;
        MultipartFile file = mock(MultipartFile.class);
        String nuevoTitulo = "Nuevo";
        String detallesCambios = "Detalles";
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user@test.com");
        Documento documento = new Documento();
        documento.setId(documentoId);
        documento.setProyectoId(2);
        when(documentoRepository.findById(documentoId)).thenReturn(java.util.Optional.of(documento));
        Usuario usuario = new Usuario();
        usuario.setId(10);
        usuario.setCorreo("user@test.com");
        when(usuarioRepository.findByCorreo("user@test.com")).thenReturn(java.util.Optional.of(usuario));
        when(authorizationService.tieneAccesoAProyecto(10, 2)).thenReturn(false);

        // Act
        ResponseEntity<?> response = documentoController.updateDocumento(documentoId, file, nuevoTitulo, detallesCambios, authentication);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateDocumentoThrowsException() throws Exception {
        // Arrange
        Integer documentoId = 1;
        MultipartFile file = mock(MultipartFile.class);
        String nuevoTitulo = "Nuevo";
        String detallesCambios = "Detalles";
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user@test.com");
        Documento documento = new Documento();
        documento.setId(documentoId);
        documento.setProyectoId(2);
        when(documentoRepository.findById(documentoId)).thenReturn(java.util.Optional.of(documento));
        Usuario usuario = new Usuario();
        usuario.setId(10);
        usuario.setCorreo("user@test.com");
        when(usuarioRepository.findByCorreo("user@test.com")).thenReturn(java.util.Optional.of(usuario));
        when(authorizationService.tieneAccesoAProyecto(10, 2)).thenReturn(true);
        when(documentoService.updateDocumento(documentoId, file, nuevoTitulo, 10, detallesCambios)).thenThrow(new RuntimeException("Error interno"));

        // Act
        ResponseEntity<?> response = documentoController.updateDocumento(documentoId, file, nuevoTitulo, detallesCambios, authentication);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetDocumentosPorProyectoUsuarioNoAutorizado() {
        // Arrange
        Integer proyectoId = 1;
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user@test.com");
        Usuario usuario = new Usuario();
        usuario.setId(10);
        usuario.setCorreo("user@test.com");
        when(usuarioRepository.findByCorreo("user@test.com")).thenReturn(java.util.Optional.of(usuario));
        when(authorizationService.tieneAccesoAProyecto(10, proyectoId)).thenReturn(false);

        // Act
        ResponseEntity<?> response = documentoController.getDocumentosPorProyecto(proyectoId, authentication);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetDocumentosPorProyectoThrowsException() {
        // Arrange
        Integer proyectoId = 1;
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user@test.com");
        Usuario usuario = new Usuario();
        usuario.setId(10);
        usuario.setCorreo("user@test.com");
        when(usuarioRepository.findByCorreo("user@test.com")).thenReturn(java.util.Optional.of(usuario));
        when(authorizationService.tieneAccesoAProyecto(10, proyectoId)).thenReturn(true);
        when(documentoService.findAllDocumentosByProyectoId(proyectoId)).thenThrow(new RuntimeException("Error interno"));

        // Act
        ResponseEntity<?> response = documentoController.getDocumentosPorProyecto(proyectoId, authentication);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }
    @Mock
    DocumentoService documentoService;
    @Mock
    DocumentoRepository documentoRepository;
    @Mock
    UsuarioRepository usuarioRepository;
    @Mock
    AuthorizationService authorizationService;
    @InjectMocks
    DocumentoController documentoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadDocumentoUsuarioAutorizado() throws Exception {
        // Arrange:
        MultipartFile file = mock(MultipartFile.class);
        String titulo = "DocTest";
        Integer proyectoId = 1;
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user@test.com");
        Usuario usuario = new Usuario();
        usuario.setId(10);
        usuario.setCorreo("user@test.com");
        when(usuarioRepository.findByCorreo("user@test.com")).thenReturn(java.util.Optional.of(usuario));
        when(authorizationService.tieneAccesoAProyecto(10, proyectoId)).thenReturn(true);
        Documento docMock = new Documento();
        docMock.setId(5);
        docMock.setTitulo(titulo);
        when(documentoService.uploadAndSaveDocumento(file, titulo, proyectoId, 10)).thenReturn(docMock);

        // Act:
        ResponseEntity<?> response = documentoController.uploadDocumento(file, titulo, proyectoId, authentication);

        // Assert:
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody() instanceof Documento);
        assertEquals(5, ((Documento) response.getBody()).getId());
        verify(documentoService, times(1)).uploadAndSaveDocumento(file, titulo, proyectoId, 10);
    }

    @Test
    void testUploadDocumentoUsuarioNoAutorizado() throws Exception {
        // Arrange:
        MultipartFile file = mock(MultipartFile.class);
        String titulo = "DocTest";
        Integer proyectoId = 1;
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user@test.com");
        Usuario usuario = new Usuario();
        usuario.setId(10);
        usuario.setCorreo("user@test.com");
        when(usuarioRepository.findByCorreo("user@test.com")).thenReturn(java.util.Optional.of(usuario));
        when(authorizationService.tieneAccesoAProyecto(10, proyectoId)).thenReturn(false);

        // Act:
        ResponseEntity<?> response = documentoController.uploadDocumento(file, titulo, proyectoId, authentication);

    // Assert:
    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertNull(response.getBody());
    verify(documentoService, never()).uploadAndSaveDocumento(any(), any(), any(), any());
    }
}
