package ru.neoflex.deal.controller;

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
import ru.neoflex.deal.DTOForTests;
import ru.neoflex.deal.service.DealService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class DealControllerImplTest {
    @Mock private DealService dealService;

    @InjectMocks
    private DealControllerImpl dealController;
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();
    private ObjectWriter objectWriter;

    @BeforeEach
    void setUp() {
        mapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders.standaloneSetup(dealController).build();
        objectWriter = mapper.writer().withDefaultPrettyPrinter();
        Mockito.lenient().when(dealService.calculateCreditOffers(DTOForTests.loanApplicationRequestDTOCorrect))
                .thenAnswer(i -> DTOForTests.loanOfferDTOListFromLoanApplicationRequestDTOCorrect);
    }

    @Test
    @SneakyThrows
    void applyOffer() {
        mockMvc.perform(put("/deal/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(DTOForTests.loanOfferDTOTrueTrue)))
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @SneakyThrows
    void calculateCredit() {
        mockMvc.perform(put("/deal/calculate/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(DTOForTests.finishRegistrationRequestDTO)))
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @SneakyThrows
    void calculateCreditOffersShouldReturnDtoListAndStatus200Test() {

        mockMvc.perform(post("/deal/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(DTOForTests.loanApplicationRequestDTOCorrect)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectWriter.writeValueAsString(DTOForTests.loanOfferDTOListFromLoanApplicationRequestDTOCorrect)));
    }


}