package ru.neoflex.conveyor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = {"application.rate = 20"})
class ConveyorServiceImplTest {
    private final ConveyorServiceImpl conveyorService = new ConveyorServiceImpl();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(conveyorService, "baseRate", BigDecimal.valueOf(20));
        ReflectionTestUtils.setField(conveyorService, "insuranceCost", BigDecimal.valueOf(0.02));

    }

    @Test
    void calculateCreditOffersShouldReturnDtoListTest() {
        assertThat(conveyorService.calculateCreditOffers(DTOForTests.loanApplicationRequestDTO1))
                .isNotNull()
                .hasSize(4)
                .containsExactlyElementsOf(DTOForTests.loanOfferDTOList1);
    }
    @Test
    void calculateCreditOffersShouldThrowIllegalArgumentExceptionTest(){
        assertThrows(IllegalArgumentException.class, () -> conveyorService.calculateCreditOffers(DTOForTests.loanApplicationRequestDTO2));

    }

    @Test
    void calculateCreditOffersShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> conveyorService.calculateCreditOffers(null));
        DTOForTests.loanApplicationRequestDTO1.setAmount(null);
        assertThrows(NullPointerException.class, () -> conveyorService.calculateCreditOffers(DTOForTests.loanApplicationRequestDTO1));
    }

    @Test
    void calculateCreditParametersShouldReturnCreditDTOTest(){
        assertThat(conveyorService.calculateCreditParameters(DTOForTests.scoringDataDTO1))
                .isNotNull()
                .isEqualTo(DTOForTests.creditDTO1);
    }

    @Test
    void calculateCreditParametersShouldReturnCreditDTOTest2(){
        assertThat(conveyorService.calculateCreditParameters(DTOForTests.scoringDataDTO2))
                .isNotNull()
                .isEqualTo(DTOForTests.creditDTO2);
    }
}