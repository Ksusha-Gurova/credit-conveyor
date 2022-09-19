package ru.neoflex.deal.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.CreditDTO;
import ru.neoflex.deal.DTOForTests;
import ru.neoflex.deal.model.Credit;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CreditMapperImplTest {
    @InjectMocks
    private CreditMapperImpl mapper;
    private CreditDTO dto;
    private Credit credit;

    @BeforeEach
    void setUp() {
        dto = CreditDTO.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(6)
                .monthlyPayment(BigDecimal.valueOf(17403.38))
                .rate(BigDecimal.valueOf(15))
                .psk(BigDecimal.valueOf(21.709))
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .paymentSchedule(DTOForTests.scheduleElementList)
                .build();

        credit = Credit.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(6)
                .monthlyPayment(BigDecimal.valueOf(17403.38))
                .rate(BigDecimal.valueOf(15))
                .psk(BigDecimal.valueOf(21.709))
                .paymentSchedule(DTOForTests.scheduleElementList)
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();
    }

    @Test
    void mapDtoToEntity() {
        assertThat(mapper.mapDtoToEntity(dto))
                .isNotNull()
                .isEqualTo(credit);
    }
}