package com.innosistemas.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {
    @Test
    void testConstructorAndGetters() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setActivo(true);
        assertEquals(1L, usuario.getId());
        assertEquals("Juan", usuario.getNombre());
        assertTrue(usuario.isActivo());
    }
    // Puedes agregar más pruebas para equals, hashCode, etc.
}
