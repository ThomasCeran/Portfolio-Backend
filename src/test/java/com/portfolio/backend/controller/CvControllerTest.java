package com.portfolio.backend.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class CvControllerTest {

    @TempDir
    Path tempDir;

    private MockMvc buildMvc(String path) {
        return MockMvcBuilders.standaloneSetup(new CvController(path)).build();
    }

    @Test
    void shouldReturnNotFoundWhenFileMissing() throws Exception {
        MockMvc mockMvc = buildMvc(tempDir.resolve("missing.pdf").toString());

        mockMvc.perform(get("/api/public/cv"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldServePdfWhenFileExists() throws Exception {
        Path pdf = tempDir.resolve("cv.pdf");
        Files.writeString(pdf, "dummy-pdf");

        MockMvc mockMvc = buildMvc(pdf.toString());

        mockMvc.perform(get("/api/public/cv"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString("cv.pdf")))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }
}
