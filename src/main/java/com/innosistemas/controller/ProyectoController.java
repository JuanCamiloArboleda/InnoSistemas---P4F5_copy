package com.innosistemas.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import com.innosistemas.service.ProyectoService;
import com.innosistemas.entity.Proyecto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/proyecto")
public class ProyectoController {
    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @PostMapping("/crearProyecto")
    public ResponseEntity<Proyecto> createProyecto(@RequestParam String nombre, @RequestParam String descripcion, @RequestParam Integer equipoId) {
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(nombre);
        proyecto.setDescripcion(descripcion);
        proyecto.setEquipoId(equipoId);
        return ResponseEntity.ok(proyectoService.saveProyecto(proyecto));
    }
}
