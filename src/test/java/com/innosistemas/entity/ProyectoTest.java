package com.innosistemas.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProyectoTest {
    @Test
    void testConstructorAndGetters() {
        // Arrange:
        Proyecto proyecto = new Proyecto();
        proyecto.setId(1);
        proyecto.setNombre("Proyecto X");
        proyecto.setDescripcion("Proyecto de prueba");
        proyecto.setEquipoId(2);

    // ...

        // Assert:
        assertEquals(1, proyecto.getId());
        assertEquals("Proyecto X", proyecto.getNombre());
        assertEquals("Proyecto de prueba", proyecto.getDescripcion());
        assertEquals(2, proyecto.getEquipoId());
    }
}
