package com.portfolio.backend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Public endpoint to download the CV PDF.
 */
@RestController
@RequestMapping("/api/public/cv")
public class CvController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CvController.class);

    private final String cvFilePath;

    public CvController(@Value("${cv.file-path:}") String cvFilePath) {
        this.cvFilePath = cvFilePath;
    }

    @GetMapping
    public ResponseEntity<Resource> downloadCv() throws IOException {
        if (cvFilePath == null || cvFilePath.isBlank()) {
            return ResponseEntity.notFound().build();
        }

        Path file = Paths.get(cvFilePath).normalize();
        if (!Files.exists(file) || Files.isDirectory(file)) {
            LOGGER.warn("CV file not found at {}", file);
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(file.toUri());
        String filename = file.getFileName().toString();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentLength(Files.size(file))
                .body(resource);
    }
}
