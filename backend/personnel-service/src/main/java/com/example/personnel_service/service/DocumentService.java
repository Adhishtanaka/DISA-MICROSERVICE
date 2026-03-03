package com.example.personnel_service.service;

import com.example.personnel_service.entity.Document;
import com.example.personnel_service.repository.DocumentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {
    private final DocumentRepository repository;
    private final Path uploadDir;

    public DocumentService(DocumentRepository repository,
                           @Value("${file.upload-dir:./uploads}") String uploadPath) {
        this.repository = repository;
        this.uploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
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
        // Delete physical file if it exists
        if (document.getUrl() != null && document.getUrl().startsWith("/api/personnel/documents/")) {
            try {
                String filename = document.getUrl().substring(document.getUrl().lastIndexOf('/') + 1);
                Path filePath = uploadDir.resolve(filename);
                Files.deleteIfExists(filePath);
            } catch (IOException ignored) {
            }
        }
        repository.delete(document);
    }

    /**
     * Stores an uploaded file and creates a Document record.
     */
    public Document uploadFile(MultipartFile file, String note, String issuedBy) throws IOException {
        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf('.'));
        }
        String storedName = UUID.randomUUID() + extension;

        Path targetPath = uploadDir.resolve(storedName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        Document doc = new Document();
        doc.setName(originalName);
        doc.setUrl("/api/personnel/documents/files/" + storedName);
        doc.setNote(note != null ? note : "");
        doc.setIssuedBy(issuedBy != null ? issuedBy : "");
        doc.setIssueDate(LocalDateTime.now());

        return repository.save(doc);
    }

    /**
     * Loads a stored file as a Resource for download.
     */
    public Resource loadFile(String filename) {
        try {
            Path filePath = uploadDir.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
            throw new RuntimeException("File not found: " + filename);
        } catch (MalformedURLException e) {
            throw new RuntimeException("File not found: " + filename, e);
        }
    }
}
