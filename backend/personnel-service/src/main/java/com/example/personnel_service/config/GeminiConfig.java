package com.example.personnel_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for the Gemini AI model integration.
 * Binds properties from application.yaml with prefix "model.gemini.api".
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@Configuration
@ConfigurationProperties(prefix = "model.gemini.api")
public class GeminiConfig {

    private String url;
    private String key;

    /**
     * Gets the Gemini API URL.
     * 
     * @return the API URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the Gemini API URL.
     * 
     * @param url the API URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets the Gemini API key.
     * 
     * @return the API key
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the Gemini API key.
     * 
     * @param key the API key
     */
    public void setKey(String key) {
        this.key = key;
    }
}
