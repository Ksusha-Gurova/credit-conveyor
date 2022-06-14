package ru.neoflex.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.neoflex.application.DTOForTests;
import ru.neoflex.application.api.ApplicationExceptionHandler;
import ru.neoflex.application.service.ApplicationService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ApplicationControllerTest {
    @InjectMocks
    private ApplicationController controller;
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();
    private ObjectWriter objectWriter;

    @Mock private ApplicationService service;

    @BeforeEach
    void setUp() {
        mapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new ApplicationExceptionHandler()).build();
        objectWriter = mapper.writer().withDefaultPrettyPrinter();

        Mockito.lenient().when(service.createApplication(DTOForTests.loanApplicationRequestDTOCorrect))
                .thenAnswer(i -> DTOForTests.loanOfferDTOListFromLoanApplicationRequestDTOCorrect);
        Mockito.lenient().when(service.createApplication(DTOForTests.loanApplicationRequestDTOWrongAge))
                .thenThrow(new IllegalArgumentException("Ваш возраст менее 18. Заявка не может быть выполнена"));
    }

    @Test
    @SneakyThrows
    void applyOffer() {
        mockMvc.perform(put("/application/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(DTOForTests.loanOfferDTOTrueTrue)))
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @SneakyThrows
    void createApplicationShouldReturnDtoListAndStatus200Test() {
        mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(DTOForTests.loanApplicationRequestDTOCorrect)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectWriter.writeValueAsString(DTOForTests.loanOfferDTOListFromLoanApplicationRequestDTOCorrect)));
    }

    @Test
    @SneakyThrows
    void createApplicationShouldReturnDtoListAndStatus400Test() {
        mockMvc.perform(post("/application")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(DTOForTests.loanApplicationRequestDTOWrongAge)))
                .andExpect(status().isBadRequest());
    }
}