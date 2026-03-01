package com.portfolio.urlshortener.dto;

import java.time.LocalDateTime;

public class UrlStatsResponse {

    private String shortCode;
    private String shortUrl;
    private String originalUrl;
    private Long clickCount;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime lastAccessedAt;
    private boolean expired;

    public UrlStatsResponse(String shortCode, String shortUrl, String originalUrl,
                            Long clickCount, LocalDateTime createdAt,
                            LocalDateTime expiresAt, LocalDateTime lastAccessedAt,
                            boolean expired) {
        this.shortCode = shortCode;
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
        this.clickCount = clickCount;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.lastAccessedAt = lastAccessedAt;
        this.expired = expired;
    }

    public String getShortCode() { return shortCode; }
    public String getShortUrl() { return shortUrl; }
    public String getOriginalUrl() { return originalUrl; }
    public Long getClickCount() { return clickCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public LocalDateTime getLastAccessedAt() { return lastAccessedAt; }
    public boolean isExpired() { return expired; }
}
