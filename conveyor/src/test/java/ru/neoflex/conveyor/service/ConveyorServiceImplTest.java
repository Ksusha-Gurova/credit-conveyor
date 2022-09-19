package ru.neoflex.conveyor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.neoflex.conveyor.DTOForTests;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ConveyorServiceImplTest {

    @InjectMocks
    private ConveyorServiceImpl conveyorService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(conveyorService, "baseRate", BigDecimal.valueOf(20));
        ReflectionTestUtils.setField(conveyorService, "insuranceCost", BigDecimal.valueOf(0.02));
    }

    @Test
    void calculateCreditOffersShouldReturnDtoListTest() {
        assertThat(conveyorService.calculateCreditOffers(DTOForTests.loanApplicationRequestDTOCorrect))
                .isNotNull()
                .hasSize(4)
                .containsExactlyElementsOf(DTOForTests.loanOfferDTOListFromLoanApplicationRequestDTOCorrect);
    }
    @Test
    void calculateCreditOffersShouldThrowIllegalArgumentExceptionTest(){
        assertThrows(IllegalArgumentException.class, () -> conveyorService.calculateCreditOffers(DTOForTests.loanApplicationRequestDTOWrongAge));

    }

    @Test
    void calculateCreditOffersShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> conveyorService.calculateCreditOffers(null));
    }

    @Test
    void calculateCreditParametersShouldReturnCreditDTOTest(){
        assertThat(conveyorService.calculateCreditParameters(DTOForTests.scoringDataDTOCorrectMan30_55DivorcedSelfEmployedInsuranceSalaryClient))
                .isNotNull()
                .isEqualTo(DTOForTests.creditDTOFromScoringDataDTOCorrectMan30_55DivorcedSelfEmployedInsuranceSalaryClient);
    }

    @Test
    void calculateCreditParametersShouldReturnCreditDTOTest2(){
        assertThat(conveyorService.calculateCreditParameters(DTOForTests.scoringDataDTOCorrectWoman35_60MariedBusinessOwnerSalaryClient))
                .isNotNull()
                .isEqualTo(DTOForTests.creditDTOFromScoringDataDTOCorrectWoman35_60MariedBusinessOwnerSalaryClient);
    }
}