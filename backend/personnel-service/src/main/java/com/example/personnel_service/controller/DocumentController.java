package com.example.personnel_service.controller;

import com.example.personnel_service.entity.Document;
import com.example.personnel_service.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Document entities.
 * Provides endpoints for CRUD operations on personnel document records.
 * Base path: /api/personnel/documents
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@RestController
@RequestMapping("/api/personnel/documents")
public class DocumentController {
    private final DocumentService documentService;

    /**
     * Constructs a new DocumentController with the specified DocumentService.
     * 
     * @param documentService the service used to handle Document business logic
     */
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * Retrieves all documents from the database.
     * 
     * @return ResponseEntity containing a list of all Document entities
     */
    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    /**
     * Retrieves a specific document by its unique identifier.
     * 
     * @param id the unique identifier of the document to retrieve
     * @return ResponseEntity containing the Document entity with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getDocumentById(id));
    }

    /**
     * Creates one or more new document records.
     * 
     * @param documents list of Document entities to create
     * @return ResponseEntity containing the created Document entities with HTTP status 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<List<Document>> createDocuments(@RequestBody List<Document> documents) {
        return ResponseEntity.status(HttpStatus.CREATED).body(documentService.addDocument(documents));
    }

    /**
     * Updates one or more existing document records.
     * 
     * @param documents list of Document entities with updated information
     * @return ResponseEntity containing the updated Document entities
     */
    @PutMapping
    public ResponseEntity<List<Document>> updateDocuments(@RequestBody List<Document> documents) {
        return ResponseEntity.ok(documentService.updateDocument(documents));
    }

    /**
     * Performs a soft delete on a document by marking it as disabled.
     * The document record remains in the database but is flagged as inactive.
     * 
     * @param id the unique identifier of the document to soft delete
     * @return ResponseEntity containing the soft-deleted Document entity
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Document> softDeleteDocument(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.softDeleteDocument(id));
    }

    /**
     * Permanently deletes a document record from the database.
     * 
     * @param id the unique identifier of the document to delete
     * @return ResponseEntity with HTTP status 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}
