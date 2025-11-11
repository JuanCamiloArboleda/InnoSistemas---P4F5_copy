package com.innosistemas.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.security.core.Authentication;
import com.innosistemas.entity.Usuario;
import com.innosistemas.entity.Documento;
import com.innosistemas.repository.DocumentoRepository;
import com.innosistemas.repository.UsuarioRepository;
import com.innosistemas.service.DocumentoService;
import com.innosistemas.security.AuthorizationService;
import io.jsonwebtoken.io.IOException;

@RestController
@RequestMapping("/api/documento")
public class DocumentoController {
    private static final String USUARIO_NO_ENCONTRADO = "Usuario no encontrado";
    private final DocumentoService documentoService;
    private final DocumentoRepository documentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuthorizationService authorizationService;

    public DocumentoController(DocumentoService documentoService, DocumentoRepository documentoRepository,
            UsuarioRepository usuarioRepository, AuthorizationService authorizationService) {
        this.documentoService = documentoService;
        this.documentoRepository = documentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.authorizationService = authorizationService;
    }

    @PostMapping(path = "/subirDocumento", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Documento> uploadDocumento(@RequestParam("file") MultipartFile file, @RequestParam("titulo") String titulo,
        @RequestParam("proyectoId") Integer proyectoId, Authentication authentication) {
        // Primero verificamos que el usuario tenga permiso para subir el documento al proyecto
        String correo = authentication.getName();
    Usuario usuario = usuarioRepository.findByCorreo(correo).orElseThrow(() -> new RuntimeException(USUARIO_NO_ENCONTRADO));
        
        if (!authorizationService.tieneAccesoAProyecto(usuario.getId(), proyectoId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            // Usuario autorizado, procedemos a subir el documento
            try {
                Documento docSaved = documentoService.uploadAndSaveDocumento(file, titulo, proyectoId, usuario.getId());
                return ResponseEntity.status(HttpStatus.CREATED).body(docSaved);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @PostMapping(path = "/{documentoId}/actualizarDocumento", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Documento> updateDocumento(@PathVariable Integer documentoId, @RequestParam("file") MultipartFile file,
        @RequestParam("nuevoTitulo") String nuevoTitulo, @RequestParam("detallesCambios") String detallesCambios, Authentication authentication) {
        // Verificamos que el usuario tenga permiso para editar el documento al proyecto
    Documento documentoAct = documentoRepository.findById(documentoId).orElseThrow(() -> new RuntimeException("Documento no encontrado"));
        String correo = authentication.getName();
    Usuario usuario = usuarioRepository.findByCorreo(correo).orElseThrow(() -> new RuntimeException(USUARIO_NO_ENCONTRADO));
        
        if (!authorizationService.tieneAccesoAProyecto(usuario.getId(), documentoAct.getProyectoId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            // Usuario autorizado, procedemos a subir el documento
            try {
                Documento docUpdated= documentoService.updateDocumento(documentoId, file, nuevoTitulo, usuario.getId(), detallesCambios);
                return ResponseEntity.ok(docUpdated);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }
    

    @GetMapping("/{proyectoId}/documentosPorProyecto")
    public ResponseEntity<List<Documento>> getDocumentosPorProyecto(@PathVariable Integer proyectoId, Authentication authentication) {
        // Primero verificamos que el usuario tenga permiso para obtener documentos del proyecto
        String correo = authentication.getName();
    Usuario usuario = usuarioRepository.findByCorreo(correo).orElseThrow(() -> new RuntimeException(USUARIO_NO_ENCONTRADO));

        if (!authorizationService.tieneAccesoAProyecto(usuario.getId(), proyectoId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            // Usuario autorizado, procedemos a obtener los documentos del proyecto
            try {
                List<Documento> documentos = documentoService.findAllDocumentosByProyectoId(proyectoId);
                return ResponseEntity.ok(documentos);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @GetMapping("/{documentoId}/descargarDocumento")
    public ResponseEntity<GridFsResource> downloadDocumento(@PathVariable Integer documentoId, Authentication authentication)
            throws IOException, java.io.IOException {
        // Primero verificamos que el usuario tenga permiso para descargar el documento
    Documento doc = documentoRepository.findById(documentoId).orElseThrow(() -> new RuntimeException("Documento no encontrado"));
        String correo = authentication.getName();
    Usuario usuario = usuarioRepository.findByCorreo(correo).orElseThrow(() -> new RuntimeException(USUARIO_NO_ENCONTRADO));

        if (!authorizationService.tieneAccesoAProyecto(usuario.getId(), doc.getProyectoId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            // Usuario autorizado, procedemos a descargar el archivo desde GridFS
            String gridFsId = doc.getRutaDoc();
            GridFsResource resource = documentoService.downloadByGridFsId(gridFsId);
            String contentType = resource.getContentType();
            String filename = resource.getFilename();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + filename + "\"");

            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        }
    }
}
