package com.example.personnel_service.controller;

import com.example.personnel_service.entity.Document;
import com.example.personnel_service.service.DocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.personnel_service.exception.GlobalExceptionHandler;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DocumentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DocumentService documentService;

    @InjectMocks
    private DocumentController documentController;

    private ObjectMapper objectMapper;
    private Document doc1;
    private Document doc2;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(documentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        doc1 = new Document();
        doc1.setId(1L);
        doc1.setName("Medical Certificate");
        doc1.setUrl("https://docs.example.com/cert1.pdf");
        doc1.setNote("Annual medical check");
        doc1.setIssuedBy("City Hospital");
        doc1.setDisabled(false);

        doc2 = new Document();
        doc2.setId(2L);
        doc2.setName("Training Certificate");
        doc2.setUrl("https://docs.example.com/cert2.pdf");
        doc2.setNote("First aid training");
        doc2.setIssuedBy("Red Cross");
        doc2.setDisabled(false);
    }

    @Test
    void getAllDocuments_ShouldReturnList() throws Exception {
        when(documentService.getAllDocuments()).thenReturn(Arrays.asList(doc1, doc2));

        mockMvc.perform(get("/api/personnel/documents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Medical Certificate"));

        verify(documentService).getAllDocuments();
    }

    @Test
    void getDocumentById_ShouldReturnDocument() throws Exception {
        when(documentService.getDocumentById(1L)).thenReturn(doc1);

        mockMvc.perform(get("/api/personnel/documents/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Medical Certificate"))
                .andExpect(jsonPath("$.issuedBy").value("City Hospital"));

        verify(documentService).getDocumentById(1L);
    }

    @Test
    void getDocumentById_NotFound_ShouldReturn500() throws Exception {
        when(documentService.getDocumentById(99L)).thenThrow(new RuntimeException("Document not found"));

        mockMvc.perform(get("/api/personnel/documents/99"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createDocuments_ShouldReturnCreated() throws Exception {
        List<Document> docs = Arrays.asList(doc1, doc2);
        when(documentService.addDocument(any())).thenReturn(docs);

        mockMvc.perform(post("/api/personnel/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(docs)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(2));

        verify(documentService).addDocument(any());
    }

    @Test
    void updateDocuments_ShouldReturnUpdated() throws Exception {
        doc1.setNote("Updated note");
        List<Document> docs = Arrays.asList(doc1);
        when(documentService.updateDocument(any())).thenReturn(docs);

        mockMvc.perform(put("/api/personnel/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(docs)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].note").value("Updated note"));

        verify(documentService).updateDocument(any());
    }

    @Test
    void softDeleteDocument_ShouldReturnDisabled() throws Exception {
        doc1.setDisabled(true);
        when(documentService.softDeleteDocument(1L)).thenReturn(doc1);

        mockMvc.perform(patch("/api/personnel/documents/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.disabled").value(true));

        verify(documentService).softDeleteDocument(1L);
    }

    @Test
    void deleteDocument_ShouldReturnNoContent() throws Exception {
        doNothing().when(documentService).deleteDocument(1L);

        mockMvc.perform(delete("/api/personnel/documents/1"))
                .andExpect(status().isNoContent());

        verify(documentService).deleteDocument(1L);
    }
}
