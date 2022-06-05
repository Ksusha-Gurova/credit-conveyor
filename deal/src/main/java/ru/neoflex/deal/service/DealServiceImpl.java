package ru.neoflex.deal.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.*;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.client.ConveyorClient;
import ru.neoflex.deal.mapper.ApplicationMapper;
import ru.neoflex.deal.mapper.ClientMapper;
import ru.neoflex.deal.mapper.CreditMapper;
import ru.neoflex.deal.mapper.EmploymentMapper;
import ru.neoflex.deal.model.Application;
import ru.neoflex.deal.model.Client;
import ru.neoflex.deal.model.Credit;
import ru.neoflex.deal.model.Employment;
import ru.neoflex.deal.repository.ApplicationRepository;
import ru.neoflex.deal.repository.ClientRepository;
import ru.neoflex.deal.repository.CreditRepository;
import ru.neoflex.deal.repository.EmploymentRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class DealServiceImpl implements DealService{

    private static final int BAD_REQUEST_STATUS = 400;

    private final ApplicationRepository applicationRepository;
    private final ClientRepository clientRepository;
    private final CreditRepository creditRepository;
    private final EmploymentRepository employmentRepository;
    private final ClientMapper clientMapper;
    private final ApplicationMapper applicationMapper;
    private final CreditMapper creditMapper;
    private final EmploymentMapper employmentMapper;
    private final ConveyorClient conveyorClient;

    @Override
    public void applyOffer(LoanOfferDTO loanOfferDTO) {
        log.info("applyOffer(), loanOfferDTO = {}", loanOfferDTO);

        Application application = applicationRepository.findById(loanOfferDTO.getApplicationId())
                .orElseThrow(() -> new EntityNotFoundException("В базе нет заявки с id = " + loanOfferDTO.getApplicationId()));
        log.info("applyOffer(), из базы достается заявка по id из loanOfferDTO, application = {}", application);

        application.setStatus(ApplicationStatus.APPROVED);
        if (application.getStatusHistory() == null){
            application.setStatusHistory(new ArrayList<>());
            log.info("applyOffer(), инициализируется новый список statusHistory");
        }
        application.getStatusHistory().add(new ApplicationStatusHistoryDTO(application.getStatus(), LocalDateTime.now(),ChangeType.AUTOMATIC));
        log.info("applyOffer(), в список добавляется новый объект, statusHistory = {}", application.getStatusHistory());

        application.setAppliedOffer(loanOfferDTO);
        log.info("applyOffer(), в заявку добавляется информация из loanOfferDTO, application = {}", application);

        applicationRepository.save(application);
        log.info("applyOffer(), сохраняем заявку в базу");
    }

    @Override
    public void calculateCredit(Long applicationId, FinishRegistrationRequestDTO finishRegistrationRequestDTO) {
        log.info("calculateCredit(), applicationId = {}, finishRegistrationRequestDTO = {}", applicationId, finishRegistrationRequestDTO);

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("В базе нет заявки с id = " + applicationId));
        log.info("calculateCredit(), достали из базы заявку по applicationId, application = {}", application);

        ScoringDataDTO scoringDataDTO =
                ScoringDataDTO.builder()
                        .amount(application.getAppliedOffer().getRequestedAmount())
                        .term(application.getAppliedOffer().getTerm())
                        .firstName(application.getClient().getFirstName())
                        .lastName(application.getClient().getLastName())
                        .middleName(application.getClient().getMiddleName())
                        .gender(finishRegistrationRequestDTO.getGender())
                        .birthdate(application.getClient().getBirthdate())
                        .passportSeries(application.getClient().getPassport().getSeries())
                        .passportNumber(application.getClient().getPassport().getNumber())
                        .passportIssueDate(finishRegistrationRequestDTO.getPassportIssueDate())
                        .passportIssueBranch(finishRegistrationRequestDTO.getPassportIssueBranch())
                        .maritalStatus(finishRegistrationRequestDTO.getMaritalStatus())
                        .dependentAmount(finishRegistrationRequestDTO.getDependentAmount())
                        .employment(finishRegistrationRequestDTO.getEmployment())
                        .account(finishRegistrationRequestDTO.getAccount())
                        .isInsuranceEnabled(application.getAppliedOffer().getIsInsuranceEnabled())
                        .isSalaryClient(application.getAppliedOffer().getIsSalaryClient())
                        .build();
        log.info("calculateCredit(), сформировали scoringDataDTO на основании finishRegistrationRequestDTO и данных о клиенте из application, " +
                "scoringDataDTO = {}", scoringDataDTO);

        Employment employment = employmentMapper.mapDtoToEntity(finishRegistrationRequestDTO.getEmployment());
        employment = employmentRepository.save(employment);
        log.info("calculateCredit(), создается объект employment с данными из finishRegistrationRequestDTO и сохраняется в базу, " +
                "employment = {}", employment);

        application.getClient().setGender(finishRegistrationRequestDTO.getGender().getValue());
        application.getClient().getPassport().setIssueDate(finishRegistrationRequestDTO.getPassportIssueDate());
        application.getClient().getPassport().setIssueBranch(finishRegistrationRequestDTO.getPassportIssueBranch());
        application.getClient().setMaritalStatus(finishRegistrationRequestDTO.getMaritalStatus().getValue());
        application.getClient().setDependentAmount(finishRegistrationRequestDTO.getDependentAmount());
        application.getClient().setEmployment(employment);
        Client client = clientRepository.save(application.getClient());
        log.info("calculateCredit(), добавляем информацию в таблицу client из finishRegistrationRequestDTO, client = {}", client);

        CreditDTO creditDTO;
        try {
            creditDTO = conveyorClient.getCreditDTO(scoringDataDTO);
            Credit credit = creditMapper.mapDtoToEntity(creditDTO);
            credit.setCreditStatus(CreditStatus.CALCULATED);
            credit = creditRepository.save(credit);
            log.info("calculateCredit(), отправляем запрос /conveyor/calculation и ответ сохраняем в базу credit, credit = {}", credit);

            application.setStatus(ApplicationStatus.CC_APPROVED);
            application.setCredit(credit);
            log.info("calculateCredit(), сохраняем credit в application  и обновляем статус на CC_APPROVED");
        } catch (FeignException e) {
            switch (e.status()){
                case (BAD_REQUEST_STATUS) :
                    application.setStatus(ApplicationStatus.CC_DENIED);
                    log.info("calculateCredit(), обновляем статус на CC_DENIED");
                    break;
            }
        }

        application.getStatusHistory()
                .add(new ApplicationStatusHistoryDTO(application.getStatus(), LocalDateTime.now(), ChangeType.AUTOMATIC));
        application = applicationRepository.save(application);
        log.info("calculateCredit(), обновляем statusHistory и сохраняем измененную заявку в базу, application = {}", application);
    }

    @Override
    public List<LoanOfferDTO> calculateCreditOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        Client client = clientRepository.save(clientMapper.mapDtoToEntity(loanApplicationRequestDTO));
        log.info("calculateCreditOffers(), создается объект Client и сохраняется в базу, client = {}", client);

        Application application = applicationRepository.save(applicationMapper.mapDtoToEntity(loanApplicationRequestDTO, client));
        log.info("calculateCreditOffers(), создается объект Application и сохраняется в базу, application = {}", application);

        List<LoanOfferDTO> loanOffers;

            loanOffers = conveyorClient.getLoanOffers(loanApplicationRequestDTO);
            log.info("calculateCreditOffers(), отправляется запрос /conveyor/offers, ответ присваивается List<LoanOfferDTO> loanOffers = {}", loanOffers);

            loanOffers.forEach(offer -> offer.setApplicationId(application.getId()));
            log.info("calculateCreditOffers(), всем кредитным предложениям в списке присваивается id ранее созданной заявки, return loanOffers = {}", loanOffers);

        log.info("calculateCreditOffers(), loanApplicationRequestDTO = {}", loanApplicationRequestDTO);
        return loanOffers;
    }
}
