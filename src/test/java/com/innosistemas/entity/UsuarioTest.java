package com.innosistemas.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {
    @Test
    void testConstructorAndGetters() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombres("Juan");
        usuario.setApellidos("Pérez");
        usuario.setContrasenia("1234");
        usuario.setRol("ADMIN");
        assertEquals(1, usuario.getId());
        assertEquals("Juan", usuario.getNombres());
        assertEquals("Pérez", usuario.getApellidos());
        assertEquals("1234", usuario.getContrasenia());
        assertEquals("ADMIN", usuario.getRol());
    }
    // Puedes agregar más pruebas para equals, hashCode, etc.
}
