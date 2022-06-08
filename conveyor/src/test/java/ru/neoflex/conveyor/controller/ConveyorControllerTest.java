package ru.neoflex.conveyor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.neoflex.conveyor.DTOForTests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ConveyorControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper mapper;
    private ObjectWriter objectWriter;

    @BeforeEach
    void setUp() {
        objectWriter = mapper.writer().withDefaultPrettyPrinter();
    }

    @Test
    @SneakyThrows
    void calculateCreditOffersShouldReturnDtoListAndStatus200Test() {
        mockMvc.perform(post("/conveyor/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(DTOForTests.loanApplicationRequestDTOCorrect)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectWriter.writeValueAsString(DTOForTests.loanOfferDTOListFromLoanApplicationRequestDTOCorrect)));
    }

    @Test
    @SneakyThrows
    void calculateCreditOffersShouldReturnDtoListAndStatus200Test2() {
        mockMvc.perform(post("/conveyor/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(DTOForTests.loanApplicationRequestDTOWrongAge)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void calculateCreditParametersShouldReturnCreditDtoAndStatus200Test() {
        mockMvc.perform(post("/conveyor/calculation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(DTOForTests.scoringDataDTOCorrectMan30_55DivorcedSelfEmployedInsuranceSalaryClient)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectWriter.writeValueAsString(DTOForTests.creditDTOFromScoringDataDTOCorrectMan30_55DivorcedSelfEmployedInsuranceSalaryClient)));

        mockMvc.perform(post("/conveyor/calculation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(DTOForTests.scoringDataDTOCorrectWoman35_60MariedBusinessOwnerSalaryClient)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectWriter.writeValueAsString(DTOForTests.creditDTOFromScoringDataDTOCorrectWoman35_60MariedBusinessOwnerSalaryClient)));
    }
}