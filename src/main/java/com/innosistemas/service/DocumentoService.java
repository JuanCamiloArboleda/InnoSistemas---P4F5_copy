package com.innosistemas.service;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.List;
import java.sql.Date;
import java.io.IOException;
import com.innosistemas.entity.Documento;
import com.innosistemas.entity.VersionDocumento;
import com.innosistemas.repository.DocumentoRepository;
import com.innosistemas.repository.VersionDocumentoRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;

import jakarta.transaction.Transactional;

@Service
public class DocumentoService {

    private final GridFsTemplate gridFsTemplate;
    private final DocumentoRepository documentoRepository;
    private final VersionDocumentoRepository versionDocumentoRepository;

    public DocumentoService(GridFsTemplate gridFsTemplate,
                            DocumentoRepository documentoRepository,
                            VersionDocumentoRepository versionDocumentoRepository) {
        this.gridFsTemplate = gridFsTemplate;
        this.documentoRepository = documentoRepository;
        this.versionDocumentoRepository = versionDocumentoRepository;
    }

    // Sube el archivo a GridFS y guarda el documento en PostgreSQL
    public Documento uploadAndSaveDocumento(MultipartFile file, String titulo, Integer proyectoId, Integer usuarioId)
            throws IOException {
        ObjectId fileId;
        try (InputStream inputStream = file.getInputStream()) {
            DBObject metadata = new BasicDBObject();
            metadata.put("contentType", file.getContentType());
            metadata.put("originalFilename", file.getOriginalFilename());
            fileId = gridFsTemplate.store(inputStream, file.getOriginalFilename(), file.getContentType(), metadata);
        }

        String gridFsFileId = fileId.toHexString();

        try {
            return saveDocumento(gridFsFileId, titulo, proyectoId, usuarioId);
        } catch (RuntimeException ex) {
            try {
                gridFsTemplate.delete(new Query(Criteria.where("_id").is(fileId)));
            } catch (Exception cleanupEx) {
                // log cleanupEx
            }
            throw ex;
        }
    }

    // Guarda el documento en PostgreSQL
    public Documento saveDocumento(String gridFsFileId, String titulo, Integer proyectoId, Integer usuarioId) {
        Documento documento = new Documento();
        documento.setTitulo(titulo);
        documento.setProyectoId(proyectoId);
        documento.setUsuarioId(usuarioId);
        documento.setRutaDoc(gridFsFileId);
        documento.setFechaModificacion(new Date(System.currentTimeMillis()));
        return documentoRepository.save(documento);
    }

    // Actualiza un documento existente y crea una nueva versión
    @Transactional
    public Documento updateDocumento(Integer documentoId, MultipartFile file, String nuevoTitulo,
                                     Integer usuarioId, String detallesCambios)
            throws IOException {
        Documento documentoAct = documentoRepository.findById(documentoId)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado con ID: " + documentoId));

        // Guarda la versión actual antes de actualizar el documento
        VersionDocumento versionDoc = new VersionDocumento();
        versionDoc.setDocumentoId(documentoId);
        versionDoc.setTituloDocumento(documentoAct.getTitulo());
        versionDoc.setFechaVersion(new Date(System.currentTimeMillis()));
        versionDoc.setDetallesCambios(detallesCambios);
        versionDocumentoRepository.save(versionDoc);

        ObjectId fileId;
        try (InputStream inputStream = file.getInputStream()) {
            DBObject metadata = new BasicDBObject();
            metadata.put("contentType", file.getContentType());
            metadata.put("originalFilename", file.getOriginalFilename());
            fileId = gridFsTemplate.store(inputStream, file.getOriginalFilename(), file.getContentType(), metadata);
        }

        documentoAct.setTitulo(nuevoTitulo);
        documentoAct.setRutaDoc(fileId.toHexString());
        documentoAct.setFechaModificacion(new Date(System.currentTimeMillis()));
        documentoAct.setUsuarioId(usuarioId);

        return documentoRepository.save(documentoAct);
    }

    // Obtiene todos los documentos asociados a un proyecto
    public List<Documento> findAllDocumentosByProyectoId(Integer proyectoId) {
        return documentoRepository.findByProyectoId(proyectoId);
    }

    // Descarga el archivo de GridFS por su ID
    public GridFsResource downloadByGridFsId(String gridFsId) throws IOException {
        GridFSFile gridFsFile = gridFsTemplate.findOne(
                new Query(Criteria.where("_id").is(new ObjectId(gridFsId)))
        );

        if (gridFsFile == null) {
            throw new IOException("Archivo no encontrado en GridFS con ID: " + gridFsId);
        }

        return gridFsTemplate.getResource(gridFsFile);
    }
}
