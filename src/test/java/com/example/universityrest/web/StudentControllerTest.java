package com.example.universityrest.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    private static final String URL = "/api/v1/students";
    private static final String SAMI =
            "{\"firstName\":\"Sami\",\"lastName\":\"Trabelsi\",\"email\":\"sami@example.com\"}";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private long createSami() throws Exception {
        String body = mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(SAMI))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        JsonNode json = objectMapper.readTree(body);
        return json.get("id").asLong();
    }

    @Test
    void getAll_returns200() throws Exception {
        mockMvc.perform(get(URL)).andExpect(status().isOk());
    }

    @Test
    void create_returns201() throws Exception {
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(SAMI))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("Sami"))
                .andExpect(jsonPath("$.email").value("sami@example.com"));
    }

    @Test
    void getById_existing_returns200() throws Exception {
        long id = createSami();
        mockMvc.perform(get(URL + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Trabelsi"));
    }

    @Test
    void getById_unknown_returns404() throws Exception {
        mockMvc.perform(get(URL + "/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void update_returns200_andUpdatesFields() throws Exception {
        long id = createSami();
        String updated = "{\"firstName\":\"Sami\",\"lastName\":\"Ben Ali\",\"email\":\"sami.benali@example.com\"}";
        mockMvc.perform(put(URL + "/" + id).contentType(MediaType.APPLICATION_JSON).content(updated))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.lastName").value("Ben Ali"))
                .andExpect(jsonPath("$.email").value("sami.benali@example.com"));
    }

    @Test
    void update_unknown_returns404() throws Exception {
        mockMvc.perform(put(URL + "/999999").contentType(MediaType.APPLICATION_JSON).content(SAMI))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_returns204_thenGetReturns404() throws Exception {
        long id = createSami();
        mockMvc.perform(delete(URL + "/" + id)).andExpect(status().isNoContent());
        mockMvc.perform(get(URL + "/" + id)).andExpect(status().isNotFound());
    }

    @Test
    void delete_unknown_returns404() throws Exception {
        mockMvc.perform(delete(URL + "/999999")).andExpect(status().isNotFound());
    }

    @Test
    void create_withInvalidEmail_returns400() throws Exception {
        String bad = "{\"firstName\":\"Sami\",\"lastName\":\"Trabelsi\",\"email\":\"pas-un-email\"}";
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(bad))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Requête invalide"));
    }

    @Test
    void create_withBlankName_returns400() throws Exception {
        String bad = "{\"firstName\":\"\",\"lastName\":\"Trabelsi\",\"email\":\"sami@example.com\"}";
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(bad))
                .andExpect(status().isBadRequest());
    }
}
