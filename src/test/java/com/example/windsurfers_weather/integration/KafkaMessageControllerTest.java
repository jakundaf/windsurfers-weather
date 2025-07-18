package com.example.windsurfers_weather.integration;

import com.example.windsurfers_weather.dto.KafkaMessageDto;
import com.example.windsurfers_weather.service.KafkaProducerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class KafkaMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KafkaProducerService kafkaProducerService;

    @Test
    void shouldSendMessageToKafka() throws Exception {

        KafkaMessageDto dto = new KafkaMessageDto("jakundaf", "kafka test message");

        mockMvc.perform(post("/api/kafka/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(kafkaProducerService).sendMessage("Login: jakundaf, Message: kafka test message");
    }

    @Test
    void shouldReturnBadRequest_WhenPayloadInvalid() throws Exception {
        mockMvc.perform(post("/api/kafka/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}



