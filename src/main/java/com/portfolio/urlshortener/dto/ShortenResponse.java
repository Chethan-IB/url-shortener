package com.portfolio.urlshortener.dto;

import java.time.LocalDateTime;

public class ShortenResponse {

    private String shortCode;
    private String shortUrl;
    private String originalUrl;
    private LocalDateTime expiresAt;

    public ShortenResponse(String shortCode, String shortUrl, String originalUrl, LocalDateTime expiresAt) {
        this.shortCode = shortCode;
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
        this.expiresAt = expiresAt;
    }

    public String getShortCode() { return shortCode; }
    public String getShortUrl() { return shortUrl; }
    public String getOriginalUrl() { return originalUrl; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
}
