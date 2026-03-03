package com.example.personnel_service.controller;

import com.example.personnel_service.entity.Document;
import com.example.personnel_service.service.DocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/personnel/documents")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getDocumentById(id));
    }

    @PostMapping
    public ResponseEntity<List<Document>> createDocuments(@RequestBody List<Document> documents) {
        return ResponseEntity.status(HttpStatus.CREATED).body(documentService.addDocument(documents));
    }

    @PutMapping
    public ResponseEntity<List<Document>> updateDocuments(@RequestBody List<Document> documents) {
        return ResponseEntity.ok(documentService.updateDocument(documents));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Document> softDeleteDocument(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.softDeleteDocument(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Upload a file and create a Document record.
     * POST /api/personnel/documents/upload
     */
    @PostMapping("/upload")
    public ResponseEntity<Document> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "note", required = false) String note,
            @RequestParam(value = "issuedBy", required = false) String issuedBy) throws IOException {
        Document doc = documentService.uploadFile(file, note, issuedBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(doc);
    }

    /**
     * Download a stored file by filename.
     * GET /api/personnel/documents/files/{filename}
     */
    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        Resource resource = documentService.loadFile(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
