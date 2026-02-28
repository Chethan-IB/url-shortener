package com.portfolio.urlshortener.controller;

import com.portfolio.urlshortener.dto.ShortenRequest;
import com.portfolio.urlshortener.dto.ShortenResponse;
import com.portfolio.urlshortener.dto.UrlStatsResponse;
import com.portfolio.urlshortener.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    /**
     * POST /api/shorten
     * Body: { "originalUrl": "https://...", "customAlias": "my-link", "expiresInDays": 7 }
     * Returns the short URL and metadata.
     */
    @PostMapping("/api/shorten")
    public ResponseEntity<ShortenResponse> shorten(@Valid @RequestBody ShortenRequest request) {
        ShortenResponse response = urlService.shorten(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /{code}
     * Redirects the browser to the original URL (HTTP 302).
     */
    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        String originalUrl = urlService.resolve(code);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }

    /**
     * GET /api/stats/{code}
     * Returns click count, creation date, expiry, and last accessed time.
     */
    @GetMapping("/api/stats/{code}")
    public ResponseEntity<UrlStatsResponse> stats(@PathVariable String code) {
        UrlStatsResponse stats = urlService.getStats(code);
        return ResponseEntity.ok(stats);
    }

    /**
     * Global error handler for service-level exceptions.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }
}
