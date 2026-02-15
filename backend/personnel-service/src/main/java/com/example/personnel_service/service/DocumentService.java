package com.example.personnel_service.service;

import com.example.personnel_service.entity.Document;
import com.example.personnel_service.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {
    private final DocumentRepository repository;

    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }

    public List<Document> getAllDocuments() {
        return repository.findAll();
    }

    public Document getDocumentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    public List<Document> addDocument(List<Document> documents) {
        return repository.saveAll(documents);
    }

    public List<Document> updateDocument(List<Document> documents) {
        return repository.saveAll(documents);
    }

    public Document softDeleteDocument(Long id) {
        Document document = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        document.setDisabled(true);
        return repository.save(document);
    }

    public void deleteDocument(Long id) {
        Document document = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        repository.delete(document);
    }
}
