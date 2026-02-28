package com.portfolio.urlshortener.service;

import com.portfolio.urlshortener.dto.ShortenRequest;
import com.portfolio.urlshortener.dto.ShortenResponse;
import com.portfolio.urlshortener.dto.UrlStatsResponse;
import com.portfolio.urlshortener.model.Url;
import com.portfolio.urlshortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 6;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Transactional
    public ShortenResponse shorten(ShortenRequest request) {
        // Determine the short code (custom alias or auto-generated)
        String shortCode;
        if (request.getCustomAlias() != null && !request.getCustomAlias().isBlank()) {
            shortCode = request.getCustomAlias();
            if (urlRepository.existsByShortCode(shortCode)) {
                throw new IllegalArgumentException("Custom alias '" + shortCode + "' is already taken.");
            }
        } else {
            shortCode = generateUniqueCode();
        }

        // Build the entity
        Url url = new Url();
        url.setOriginalUrl(request.getOriginalUrl());
        url.setShortCode(shortCode);

        if (request.getExpiresInDays() != null && request.getExpiresInDays() > 0) {
            url.setExpiresAt(LocalDateTime.now().plusDays(request.getExpiresInDays()));
        }

        urlRepository.save(url);

        return new ShortenResponse(
                shortCode,
                baseUrl + "/" + shortCode,
                request.getOriginalUrl(),
                url.getExpiresAt()
        );
    }

    @Transactional
    public String resolve(String shortCode) {
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Short URL not found: " + shortCode));

        // Check expiry
        if (url.getExpiresAt() != null && url.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("This short link has expired.");
        }

        // Increment click count and update last accessed time
        url.setClickCount(url.getClickCount() + 1);
        url.setLastAccessedAt(LocalDateTime.now());
        urlRepository.save(url);

        return url.getOriginalUrl();
    }

    @Transactional(readOnly = true)
    public UrlStatsResponse getStats(String shortCode) {
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Short URL not found: " + shortCode));

        boolean expired = url.getExpiresAt() != null && url.getExpiresAt().isBefore(LocalDateTime.now());

        return new UrlStatsResponse(
                shortCode,
                baseUrl + "/" + shortCode,
                url.getOriginalUrl(),
                url.getClickCount(),
                url.getCreatedAt(),
                url.getExpiresAt(),
                url.getLastAccessedAt(),
                expired
        );
    }

    // Generate a random unique 6-char alphanumeric code
    private String generateUniqueCode() {
        Random random = new Random();
        String code;
        do {
            StringBuilder sb = new StringBuilder(CODE_LENGTH);
            for (int i = 0; i < CODE_LENGTH; i++) {
                sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
            }
            code = sb.toString();
        } while (urlRepository.existsByShortCode(code));
        return code;
    }
}
