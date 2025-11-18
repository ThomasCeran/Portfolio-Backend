package com.portfolio.backend.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@RestController
public class OpenApiController {

    private final String openApiJson;
    private final byte[] openApiYaml;

    public OpenApiController() throws IOException {
        Resource resource = new ClassPathResource("openapi.yaml");
        this.openApiYaml = StreamUtils.copyToByteArray(resource.getInputStream());

        ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
        JsonNode node = yamlReader.readTree(openApiYaml);
        ObjectMapper jsonWriter = new ObjectMapper();
        this.openApiJson = jsonWriter.writeValueAsString(node);
    }

    @GetMapping(value = "/v3/api-docs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> jsonDocs() {
        return ResponseEntity.ok(openApiJson);
    }

    @GetMapping(value = "/v3/api-docs.yaml", produces = "application/yaml")
    public ResponseEntity<byte[]> yamlDocs() {
        return ResponseEntity.ok().body(openApiYaml);
    }

    @GetMapping(value = "/openapi.yaml", produces = "application/yaml")
    public ResponseEntity<byte[]> legacyYaml() {
        return ResponseEntity.ok().body(openApiYaml);
    }
}
