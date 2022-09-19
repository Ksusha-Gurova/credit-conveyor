package ru.neoflex.deal.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.LoanApplicationRequestDTO;
import ru.neoflex.deal.model.Application;
import ru.neoflex.deal.model.Client;
import ru.neoflex.deal.model.Passport;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ApplicationMapperImplTest {
    @InjectMocks
    private ApplicationMapperImpl mapper;
    private LoanApplicationRequestDTO loanApplicationRequestDTO;
    private Application application;
    private Client client;

    @BeforeEach
    void setUp() {
        loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(6)
                .firstName("Kseniya")
                .lastName("Gurova")
                .middleName("Igorevna")
                .email("blablabla@mail.ru")
                .birthdate(LocalDate.of(1996, 3, 20))
                .passportSeries("1234")
                .passportNumber("567890")
                .build();
        client = Client.builder()
                .firstName("Kseniya")
                .lastName("Gurova")
                .middleName("Igorevna")
                .birthdate(LocalDate.of(1996, 3, 20))
                .email("blablabla@mail.ru")
                .passport(Passport.builder()
                        .series("1234")
                        .number("567890")
                        .build())
                .build();

        application = Application.builder()
                .client(client)
                .creationDate(LocalDate.now())
                .build();
    }

    @Test
    void mapDtoToEntityShouldReturnApplicationTest() {
        assertThat(mapper.mapDtoToEntity(loanApplicationRequestDTO, client))
                .isNotNull()
                .isEqualTo(application);
    }
}