package com.innosistemas.service;

import com.innosistemas.entity.Documento;
import com.innosistemas.repository.DocumentoRepository;
import com.innosistemas.repository.VersionDocumentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.multipart.MultipartFile;

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
        Documento doc = documentoService.saveDocumento("titulo", "ruta", 1, 1);
        assertNotNull(doc);
    }
}
