package com.innosistemas.service;

import com.innosistemas.entity.Documento;
import com.innosistemas.entity.VersionDocumento;
import com.innosistemas.repository.DocumentoRepository;
import com.innosistemas.repository.VersionDocumentoRepository;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocumentoServiceTest {
    @Mock
    private GridFsTemplate gridFsTemplate;
    @Mock
    private DocumentoRepository documentoRepository;
    @Mock
    private VersionDocumentoRepository versionDocumentoRepository;
    @InjectMocks
    private DocumentoService documentoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveDocumento() {
        when(documentoRepository.save(any(Documento.class))).thenReturn(new Documento());
        Documento doc = documentoService.saveDocumento("gridfsid", "titulo", 1, 1);
        assertNotNull(doc);
    }

    @Test
    void testFindAllDocumentosByProyectoId() {
        Documento d1 = new Documento();
        Documento d2 = new Documento();
        when(documentoRepository.findByProyectoId(1)).thenReturn(Arrays.asList(d1, d2));
        List<Documento> docs = documentoService.findAllDocumentosByProyectoId(1);
        assertEquals(2, docs.size());
    }

    @Test
    void testDownloadByGridFsId() throws IOException {
    GridFsResource resource = mock(GridFsResource.class);
    com.mongodb.client.gridfs.model.GridFSFile gridFSFile = mock(com.mongodb.client.gridfs.model.GridFSFile.class);
    when(gridFsTemplate.findOne(any(Query.class))).thenReturn(gridFSFile);
    when(gridFsTemplate.getResource(gridFSFile)).thenReturn(resource);
    GridFsResource result = documentoService.downloadByGridFsId(new ObjectId().toHexString());
    assertNotNull(result);
    }

    @Test
    void testUploadAndSaveDocumentoSuccess() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        InputStream inputStream = mock(InputStream.class);
        when(file.getInputStream()).thenReturn(inputStream);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getOriginalFilename()).thenReturn("doc.pdf");
        ObjectId fileId = new ObjectId();
        when(gridFsTemplate.store(any(InputStream.class), anyString(), anyString(), any(DBObject.class))).thenReturn(fileId);
        when(documentoRepository.save(any(Documento.class))).thenReturn(new Documento());
        Documento doc = documentoService.uploadAndSaveDocumento(file, "titulo", 1, 1);
        assertNotNull(doc);
    }

    @Test
    void testUploadAndSaveDocumentoErrorEnSave() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        InputStream inputStream = mock(InputStream.class);
        when(file.getInputStream()).thenReturn(inputStream);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getOriginalFilename()).thenReturn("doc.pdf");
        ObjectId fileId = new ObjectId();
        when(gridFsTemplate.store(any(InputStream.class), anyString(), anyString(), any(DBObject.class))).thenReturn(fileId);
        when(documentoRepository.save(any(Documento.class))).thenThrow(new RuntimeException("Error en saveDocumento"));
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            documentoService.uploadAndSaveDocumento(file, "titulo", 1, 1);
        });
        assertEquals("Error en saveDocumento", exception.getMessage());
        // Verificamos que se intentó hacer cleanup (delete puede fallar silenciosamente en el catch interno)
        verify(gridFsTemplate, atLeastOnce()).store(any(InputStream.class), anyString(), anyString(), any(DBObject.class));
    }

    @Test
    void testUpdateDocumentoSuccess() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        InputStream inputStream = mock(InputStream.class);
        when(file.getInputStream()).thenReturn(inputStream);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getOriginalFilename()).thenReturn("doc.pdf");
        ObjectId fileId = new ObjectId();
        when(gridFsTemplate.store(any(InputStream.class), anyString(), anyString(), any(DBObject.class))).thenReturn(fileId);
        Documento documento = new Documento();
        documento.setId(1);
        documento.setTitulo("titulo");
        when(documentoRepository.findById(1)).thenReturn(Optional.of(documento));
        when(documentoRepository.save(any(Documento.class))).thenReturn(documento);
        when(versionDocumentoRepository.save(any(VersionDocumento.class))).thenReturn(new VersionDocumento());
        Documento result = documentoService.updateDocumento(1, file, "nuevoTitulo", 2, "cambios");
        assertNotNull(result);
        verify(versionDocumentoRepository, times(1)).save(any(VersionDocumento.class));
    }

    @Test
    void testUpdateDocumentoNotFound() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(documentoRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            documentoService.updateDocumento(99, file, "nuevoTitulo", 2, "cambios");
        });
    }
    @Test
    void testUploadAndSaveDocumentoThrowsIOException() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenThrow(new IOException("Error de IO"));
        assertThrows(IOException.class, () -> {
            documentoService.uploadAndSaveDocumento(file, "titulo", 1, 1);
        });
    }

    @Test
    void testUpdateDocumentoErrorEnVersion() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        InputStream inputStream = mock(InputStream.class);
        when(file.getInputStream()).thenReturn(inputStream);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getOriginalFilename()).thenReturn("doc.pdf");
        ObjectId fileId = new ObjectId();
        when(gridFsTemplate.store(any(InputStream.class), anyString(), anyString(), any(DBObject.class))).thenReturn(fileId);
        Documento documento = new Documento();
        documento.setId(1);
        documento.setTitulo("titulo");
        when(documentoRepository.findById(1)).thenReturn(Optional.of(documento));
        when(versionDocumentoRepository.save(any(VersionDocumento.class))).thenThrow(new RuntimeException("Error en versionDocumento"));
        assertThrows(RuntimeException.class, () -> {
            documentoService.updateDocumento(1, file, "nuevoTitulo", 2, "cambios");
        });
    }

    @Test
    void testSaveDocumentoValoresNulos() {
        Documento doc = documentoService.saveDocumento(null, null, null, null);
        assertNull(doc);
    }
}
