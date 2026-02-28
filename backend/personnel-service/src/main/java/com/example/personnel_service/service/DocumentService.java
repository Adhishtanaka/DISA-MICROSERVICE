package com.example.personnel_service.service;

import com.example.personnel_service.entity.Document;
import com.example.personnel_service.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Document entities.
 * Provides business logic for Document CRUD operations including soft delete functionality.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@Service
public class DocumentService {
    private final DocumentRepository repository;

    /**
     * Constructs a new DocumentService with the specified repository.
     * 
     * @param repository the DocumentRepository used for data access
     */
    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves all documents from the database.
     * 
     * @return list of all Document entities
     */
    public List<Document> getAllDocuments() {
        return repository.findAll();
    }

    /**
     * Retrieves a document by its unique identifier.
     * 
     * @param id the unique identifier of the document
     * @return the Document entity with the specified ID
     * @throws RuntimeException if document with the given ID is not found
     */
    public Document getDocumentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    /**
     * Adds one or more new document records to the database.
     * 
     * @param documents list of Document entities to be saved
     * @return list of saved Document entities with generated IDs
     */
    public List<Document> addDocument(List<Document> documents) {
        return repository.saveAll(documents);
    }

    /**
     * Updates one or more existing document records in the database.
     * 
     * @param documents list of Document entities with updated information
     * @return list of updated Document entities
     */
    public List<Document> updateDocument(List<Document> documents) {
        return repository.saveAll(documents);
    }

    /**
     * Performs a soft delete by marking a document as disabled.
     * The record remains in the database but is flagged as inactive.
     * 
     * @param id the unique identifier of the document to soft delete
     * @return the updated Document entity with isDisabled set to true
     * @throws RuntimeException if document with the given ID is not found
     */
    public Document softDeleteDocument(Long id) {
        Document document = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        document.setDisabled(true);
        return repository.save(document);
    }

    /**
     * Permanently deletes a document record from the database.
     * 
     * @param id the unique identifier of the document to delete
     * @throws RuntimeException if document with the given ID is not found
     */
    public void deleteDocument(Long id) {
        Document document = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        repository.delete(document);
    }
}
