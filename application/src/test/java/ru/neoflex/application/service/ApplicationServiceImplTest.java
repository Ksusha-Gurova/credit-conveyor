package ru.neoflex.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.application.DTOForTests;
import ru.neoflex.application.client.DealClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {

    @InjectMocks
    private ApplicationServiceImpl service;

    @Mock private DealClient dealClient;

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(dealClient.getLoanOffers(DTOForTests.loanApplicationRequestDTOCorrect))
                .thenAnswer(i -> DTOForTests.loanOfferDTOListFromLoanApplicationRequestDTOCorrect);
    }

    @Test
    void applyOffer() {
        service.applyOffer(DTOForTests.loanOfferDTOTrueTrue);
        Mockito.verify(dealClient, Mockito.times(1)).applyOffer(DTOForTests.loanOfferDTOTrueTrue);
    }

    @Test
    void createApplicationShouldReturnDtoListTest() {
        assertThat(service.createApplication(DTOForTests.loanApplicationRequestDTOCorrect))
                .isNotNull()
                .hasSize(4)
                .containsExactlyElementsOf(DTOForTests.loanOfferDTOListFromLoanApplicationRequestDTOCorrect);
    }

    @Test
    void createApplicationShouldReturnTrowIllegalArgumentExceptionTest() {
        assertThrows(IllegalArgumentException.class, () -> service.createApplication(DTOForTests.loanApplicationRequestDTOWrongAge));
    }
}