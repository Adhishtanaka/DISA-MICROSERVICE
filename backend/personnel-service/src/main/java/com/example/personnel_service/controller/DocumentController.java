package com.example.personnel_service.controller;

import com.example.personnel_service.entity.Document;
import com.example.personnel_service.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
