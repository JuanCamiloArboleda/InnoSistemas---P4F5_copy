package com.innosistemas.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DocumentoTest {
    @Test
    void testConstructorAndGetters() {
        // Arrange:
        Documento doc = new Documento();
        doc.setId(1);
        doc.setTitulo("Doc1.pdf");
        doc.setRutaDoc("/docs/Doc1.pdf");
        doc.setProyectoId(2);
        doc.setUsuarioId(3);

        // Assert:
        assertEquals(1, doc.getId());
        assertEquals("Doc1.pdf", doc.getTitulo());
        assertEquals("/docs/Doc1.pdf", doc.getRutaDoc());
        assertEquals(2, doc.getProyectoId());
        assertEquals(3, doc.getUsuarioId());
    }
}
