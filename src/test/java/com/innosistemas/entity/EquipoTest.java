package com.innosistemas.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EquipoTest {
    @Test
    void testConstructorAndGetters() {
        Equipo equipo = new Equipo();
        equipo.setId(1);
        equipo.setNombre("Equipo A");
        equipo.setDescripcion("Equipo de desarrollo");
        assertEquals(1, equipo.getId());
        assertEquals("Equipo A", equipo.getNombre());
        assertEquals("Equipo de desarrollo", equipo.getDescripcion());
    }
}
