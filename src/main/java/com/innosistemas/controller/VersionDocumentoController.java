package com.innosistemas.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.innosistemas.entity.Documento;
import com.innosistemas.entity.Usuario;
import com.innosistemas.entity.VersionDocumento;
import com.innosistemas.repository.DocumentoRepository;
import com.innosistemas.repository.UsuarioRepository;
import com.innosistemas.service.VersionDocumentoService;
import com.innosistemas.security.AuthorizationService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/versionDocumento")
public class VersionDocumentoController {
    private final VersionDocumentoService versionDocumentoService;
    private final DocumentoRepository documentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuthorizationService authorizationService;

    public VersionDocumentoController(VersionDocumentoService versionDocumentoService,
            DocumentoRepository documentoRepository, UsuarioRepository usuarioRepository, AuthorizationService authorizationService) {
        this.versionDocumentoService = versionDocumentoService;
        this.documentoRepository = documentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.authorizationService = authorizationService;
    }

    @PostMapping("/{documentoId}/versiones")
    public ResponseEntity<?> obtenerVersionesDocumento(@PathVariable Integer documentoId, Authentication authentication) {
        // Primero verificamos que el usuario tenga permiso para ver las versiones del
        // documento
        Documento doc = documentoRepository.findById(documentoId)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));
        String correo = authentication.getName();
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!authorizationService.tieneAccesoAProyecto(usuario.getId(), doc.getProyectoId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No tienes permiso para ver las versiones de este documento.");
        } else {
            // Usuario autorizado, procedemos a obtener las versiones
            List<VersionDocumento> versiones = versionDocumentoService.obtenerVersionesDocumento(documentoId);
            return ResponseEntity.ok(versiones);
        }

    }

}
