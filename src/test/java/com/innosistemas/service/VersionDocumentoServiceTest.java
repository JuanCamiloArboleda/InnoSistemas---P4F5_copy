package com.innosistemas.service;

import com.innosistemas.entity.VersionDocumento;
import com.innosistemas.repository.VersionDocumentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VersionDocumentoServiceTest {
    @Mock
    private VersionDocumentoRepository versionDocumentoRepository;
    @InjectMocks
    private VersionDocumentoService versionDocumentoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerVersionesDocumento() {
        VersionDocumento v1 = new VersionDocumento();
        VersionDocumento v2 = new VersionDocumento();
        when(versionDocumentoRepository.findByDocumentoIdOrderByFechaVersionDesc(1)).thenReturn(Arrays.asList(v1, v2));
        List<VersionDocumento> versiones = versionDocumentoService.obtenerVersionesDocumento(1);
        assertEquals(2, versiones.size());
    }
}
