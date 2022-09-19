package ru.neoflex.deal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.deal.DTOForTests;
import ru.neoflex.deal.client.ConveyorClient;
import ru.neoflex.deal.client.KafkaClient;
import ru.neoflex.deal.mapper.ApplicationMapper;
import ru.neoflex.deal.mapper.ClientMapper;
import ru.neoflex.deal.mapper.CreditMapper;
import ru.neoflex.deal.mapper.EmploymentMapper;
import ru.neoflex.deal.model.Application;
import ru.neoflex.deal.repository.ApplicationRepository;
import ru.neoflex.deal.repository.ClientRepository;
import ru.neoflex.deal.repository.CreditRepository;
import ru.neoflex.deal.repository.EmploymentRepository;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class DealServiceImplTest {
    @Mock private ApplicationRepository applicationRepository;
    @Mock private ClientRepository clientRepository;
    @Mock private CreditRepository creditRepository;
    @Mock private EmploymentRepository employmentRepository;
    @Mock private ClientMapper clientMapper;
    @Mock private ApplicationMapper applicationMapper;
    @Mock private CreditMapper creditMapper;
    @Mock private EmploymentMapper employmentMapper;
    @Mock private ConveyorClient conveyorClient;
    @Mock private KafkaClient kafkaClient;

    @InjectMocks
    private DealServiceImpl dealService;

    private Application applicationForLoanApplicationRequestDTOCorrect;

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(clientMapper.mapDtoToEntity(DTOForTests.loanApplicationRequestDTOCorrect))
                .thenAnswer(i -> DTOForTests.clientForLoanApplicationRequestDTOCorrect);
        Mockito.lenient().when(clientRepository.save(DTOForTests.clientForLoanApplicationRequestDTOCorrect))
                .thenAnswer(i -> DTOForTests.clientForLoanApplicationRequestDTOCorrect);
        Mockito.lenient().when(applicationMapper.mapDtoToEntity(DTOForTests.loanApplicationRequestDTOCorrect, DTOForTests.clientForLoanApplicationRequestDTOCorrect))
                .thenAnswer(i -> DTOForTests.applicationForLoanApplicationRequestDTOCorrect);
        Mockito.lenient().when(applicationRepository.save(DTOForTests.applicationForLoanApplicationRequestDTOCorrect))
                .thenAnswer(i -> DTOForTests.applicationForLoanApplicationRequestDTOCorrect);
        Mockito.lenient().when(conveyorClient.getLoanOffers(DTOForTests.loanApplicationRequestDTOCorrect))
                .thenReturn(DTOForTests.loanOfferDTOListFromLoanApplicationRequestDTOCorrect);

        Mockito.lenient().when(applicationRepository.findById(1L))
                .thenAnswer(i -> Optional.of(DTOForTests.applicationForLoanApplicationRequestDTOCorrect));
        applicationForLoanApplicationRequestDTOCorrect = DTOForTests.getApplicationForLoanApplicationRequestDTOCorrect();

        Mockito.lenient().when(employmentMapper.mapDtoToEntity(DTOForTests.finishRegistrationRequestDTO.getEmployment()))
                .thenAnswer(i -> DTOForTests.employment);
        Mockito.lenient().when(employmentRepository.save(DTOForTests.employment))
                .thenAnswer(i -> DTOForTests.employment);
        Mockito.lenient().when(conveyorClient.getCreditDTO(DTOForTests.scoringDataDTO))
                .thenAnswer(i -> DTOForTests.creditDTO);
        Mockito.lenient().when(creditMapper.mapDtoToEntity(DTOForTests.creditDTO))
                .thenAnswer(i -> DTOForTests.credit);
        Mockito.lenient().when(creditRepository.save(DTOForTests.credit))
                .thenAnswer(i -> DTOForTests.credit);
    }

    @Test
    void applyOfferTest() {
        Mockito.lenient().when(applicationRepository.save(DTOForTests.applicationForLoanApplicationRequestDTOCorrect))
                .thenAnswer(i -> DTOForTests.applicationForLoanApplicationRequestDTOCorrect);
        dealService.applyOffer(DTOForTests.loanOfferDTOTrueTrue);
        Mockito.verify(applicationRepository, Mockito.times(1)).findById(DTOForTests.loanOfferDTOTrueTrue.getApplicationId());
        Mockito.verify(applicationRepository, Mockito.times(1)).save(applicationForLoanApplicationRequestDTOCorrect);
    }

    @Test
    void calculateCreditTest() {
        Mockito.lenient().when(applicationRepository.findById(1L)).thenAnswer(i -> Optional.of(DTOForTests.application));
        Mockito.lenient().when(applicationRepository.save(any(Application.class)))
                        .thenAnswer(i -> DTOForTests.applicationForSavedInCalculateCredit);
        dealService.calculateCredit(1L, DTOForTests.finishRegistrationRequestDTO);
        Mockito.verify(applicationRepository, Mockito.times(1)).findById(1L);

    }

    @Test
    void calculateCreditOffersShouldReturnDtoListTest() {
        assertThat(dealService.calculateCreditOffers(DTOForTests.loanApplicationRequestDTOCorrect))
                .isNotNull()
                .hasSize(4)
                .containsExactlyElementsOf(DTOForTests.loanOfferDTOListFromLoanApplicationRequestDTOCorrect);
        Mockito.verify(clientRepository, Mockito.times(1)).save(DTOForTests.clientForLoanApplicationRequestDTOCorrect);
        Mockito.verify(applicationRepository, Mockito.times(1)).save(DTOForTests.applicationForLoanApplicationRequestDTOCorrect);
    }

}