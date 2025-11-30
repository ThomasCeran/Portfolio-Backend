package com.portfolio.backend.config;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "cors.allowed-origin=http://allowed.com,http://other.com"
})
class CorsConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAllowConfiguredOriginOnPreflight() throws Exception {
        mockMvc.perform(options("/api/projects")
                .header("Origin", "http://allowed.com")
                .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", equalTo("http://allowed.com")))
                .andExpect(header().string("Access-Control-Allow-Credentials", equalTo("true")));
    }

    @Test
    void shouldRejectUnknownOrigin() throws Exception {
        mockMvc.perform(get("/api/projects").header("Origin", "http://evil.com"))
                .andExpect(status().isForbidden())
                .andExpect(header().string("Access-Control-Allow-Origin", nullValue()));
    }
}
