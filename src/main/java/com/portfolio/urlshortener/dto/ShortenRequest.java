package com.portfolio.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ShortenRequest {

    @NotBlank(message = "URL must not be blank")
    @Pattern(
        regexp = "^(https?://).*",
        message = "URL must start with http:// or https://"
    )
    private String originalUrl;

    // Optional: custom alias (e.g. /my-github)
    @Pattern(
        regexp = "^[a-zA-Z0-9_-]{3,20}$",
        message = "Alias must be 3–20 characters (letters, numbers, - or _)"
    )
    private String customAlias;

    // Optional: expiry in days (null = never expires)
    private Integer expiresInDays;

    // Getters and Setters

    public String getOriginalUrl() { return originalUrl; }
    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }

    public String getCustomAlias() { return customAlias; }
    public void setCustomAlias(String customAlias) { this.customAlias = customAlias; }

    public Integer getExpiresInDays() { return expiresInDays; }
    public void setExpiresInDays(Integer expiresInDays) { this.expiresInDays = expiresInDays; }
}
