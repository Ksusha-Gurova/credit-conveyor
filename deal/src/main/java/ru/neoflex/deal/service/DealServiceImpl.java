package ru.neoflex.deal.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.client.ConveyorClient;
import ru.neoflex.deal.client.KafkaClient;
import ru.neoflex.deal.client.dto.EmailMessage;
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
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DealServiceImpl implements DealService{

    private static final int BAD_REQUEST_STATUS = 400;
    private static final String CLIENT_DENIED = "client_denied";
    private static final String DOCUMENTS_CREATED = "documents_created";

    private final ApplicationRepository applicationRepository;
    private final ClientRepository clientRepository;
    private final CreditRepository creditRepository;
    private final EmploymentRepository employmentRepository;
    private final ClientMapper clientMapper;
    private final ApplicationMapper applicationMapper;
    private final CreditMapper creditMapper;
    private final EmploymentMapper employmentMapper;
    private final ConveyorClient conveyorClient;
    private final KafkaClient kafkaClient;
    private final Random random = new Random();

    @Value("${kafka.topics.finish-registration}") private String topicFinishRegistration;
    @Value("${kafka.topics.create-documents}") private String topicCreateDocuments;
    @Value("${kafka.topics.send-documents}") private String topicSendDocuments;
    @Value("${kafka.topics.send-ses}") private String topicSendSes;
    @Value("${kafka.topics.credit-issued}") private String topicCreditIssued;
    @Value("${kafka.topics.application-denied}") private String topicApplicationDenied;

    @Override
    public void applyOffer(LoanOfferDTO loanOfferDTO) {
        log.debug("applyOffer(), loanOfferDTO = {}", loanOfferDTO);

        Application application = applicationRepository.findById(loanOfferDTO.getApplicationId())
                .orElseThrow(() -> new EntityNotFoundException("В базе нет заявки с id = " + loanOfferDTO.getApplicationId()));
        log.debug("applyOffer(), из базы достается заявка по id из loanOfferDTO, application = {}", application);

        application.setStatus(ApplicationStatus.APPROVED);
        if (application.getStatusHistory() == null){
            application.setStatusHistory(new ArrayList<>());
            log.debug("applyOffer(), инициализируется новый список statusHistory");
        }
        application.getStatusHistory().add(new ApplicationStatusHistoryDTO(application.getStatus(), LocalDateTime.now(),ChangeType.AUTOMATIC));
        log.debug("applyOffer(), в список добавляется новый объект, statusHistory = {}", application.getStatusHistory());

        application.setAppliedOffer(loanOfferDTO);
        log.debug("applyOffer(), в заявку добавляется информация из loanOfferDTO, application = {}", application);

        applicationRepository.save(application);
        log.info("applyOffer(), сохраняем заявку в базу");

        EmailMessage finishRegistrationEmailDTO = EmailMessage.builder()
                .address(application.getClient().getEmail())
                .theme(EmailMessage.ThemeEnum.FINISH_REGISTRATION)
                .applicationId(application.getId())
                .build();
        kafkaClient.send(topicFinishRegistration, finishRegistrationEmailDTO);
        log.info("applyOffer(), отправляем по kafka finishRegistrationEmailDTO = {}", finishRegistrationEmailDTO);
    }

    @Override
    public void calculateCredit(Long applicationId, FinishRegistrationRequestDTO finishRegistrationRequestDTO) {
        log.debug("calculateCredit(), applicationId = {}, finishRegistrationRequestDTO = {}", applicationId, finishRegistrationRequestDTO);

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("В базе нет заявки с id = " + applicationId));
        log.debug("calculateCredit(), достали из базы заявку по applicationId, application = {}", application);

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
        log.debug("calculateCredit(), сформировали scoringDataDTO на основании finishRegistrationRequestDTO и данных о клиенте из application, " +
                "scoringDataDTO = {}", scoringDataDTO);

        Employment employment = employmentMapper.mapDtoToEntity(finishRegistrationRequestDTO.getEmployment());
        employment = employmentRepository.save(employment);
        log.debug("calculateCredit(), создается объект employment с данными из finishRegistrationRequestDTO и сохраняется в базу, " +
                "employment = {}", employment);

        application.getClient().setGender(finishRegistrationRequestDTO.getGender().getValue());
        application.getClient().getPassport().setIssueDate(finishRegistrationRequestDTO.getPassportIssueDate());
        application.getClient().getPassport().setIssueBranch(finishRegistrationRequestDTO.getPassportIssueBranch());
        application.getClient().setMaritalStatus(finishRegistrationRequestDTO.getMaritalStatus().getValue());
        application.getClient().setDependentAmount(finishRegistrationRequestDTO.getDependentAmount());
        application.getClient().setEmployment(employment);
        application.getClient().setAccount(finishRegistrationRequestDTO.getAccount());
        Client client = clientRepository.save(application.getClient());
        log.debug("calculateCredit(), добавляем информацию в таблицу client из finishRegistrationRequestDTO, client = {}", client);

        CreditDTO creditDTO;
        try {
            creditDTO = conveyorClient.getCreditDTO(scoringDataDTO);
            Credit credit = creditMapper.mapDtoToEntity(creditDTO);
            credit.setCreditStatus(CreditStatus.CALCULATED);
            credit = creditRepository.save(credit);
            log.info("calculateCredit(), отправляем запрос /conveyor/calculation и ответ сохраняем в базу credit, credit = {}", credit);

            application.setStatus(ApplicationStatus.CC_APPROVED);
            application.setCredit(credit);
            log.debug("calculateCredit(), сохраняем credit в application  и обновляем статус на CC_APPROVED");

            EmailMessage createDocumentsEmailDTO = EmailMessage.builder()
                    .address(application.getClient().getEmail())
                    .theme(EmailMessage.ThemeEnum.CREATE_DOCUMENTS)
                    .applicationId(application.getId())
                    .build();
            kafkaClient.send(topicCreateDocuments, createDocumentsEmailDTO);
            log.info("calculateCredit(), отправили по kafka createDocumentsEmailDTO = {}", createDocumentsEmailDTO);

        } catch (FeignException e) {
            if (e.status() == BAD_REQUEST_STATUS) {
                application.setStatus(ApplicationStatus.CC_DENIED);
                log.debug("calculateCredit(), обновляем статус на CC_DENIED");
            }
        }

        application.getStatusHistory()
                .add(new ApplicationStatusHistoryDTO(application.getStatus(), LocalDateTime.now(), ChangeType.AUTOMATIC));
        application = applicationRepository.save(application);
        log.debug("calculateCredit(), обновляем statusHistory и сохраняем измененную заявку в базу, application = {}", application);

    }

    @Override
    public List<LoanOfferDTO> calculateCreditOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        Client client = clientRepository.save(clientMapper.mapDtoToEntity(loanApplicationRequestDTO));
        log.debug("calculateCreditOffers(), создается объект Client и сохраняется в базу, client = {}", client);

        Application application = applicationRepository.save(applicationMapper.mapDtoToEntity(loanApplicationRequestDTO, client));
        log.debug("calculateCreditOffers(), создается объект Application и сохраняется в базу, application = {}", application);

        List<LoanOfferDTO> loanOffers;

            loanOffers = conveyorClient.getLoanOffers(loanApplicationRequestDTO);
            log.debug("calculateCreditOffers(), отправляется запрос /conveyor/offers, ответ присваивается List<LoanOfferDTO> loanOffers = {}", loanOffers);

            loanOffers.forEach(offer -> offer.setApplicationId(application.getId()));
            log.debug("calculateCreditOffers(), всем кредитным предложениям в списке присваивается id ранее созданной заявки, return loanOffers = {}", loanOffers);

        log.info("calculateCreditOffers(), return loanApplicationRequestDTO = {}", loanApplicationRequestDTO);
        return loanOffers;
    }

    @Override
    public void sendDocuments(Long applicationId) {
        log.debug("sendDocuments(), applicationId = {}", applicationId);

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("В базе нет заявки с id = " + applicationId));
        log.debug("sendDocuments(), подгружаеем из базы application = {}", application);

        application.setStatus(ApplicationStatus.PREPARE_DOCUMENTS);
        application.getStatusHistory().add(new ApplicationStatusHistoryDTO(application.getStatus(), LocalDateTime.now(),ChangeType.AUTOMATIC));
        applicationRepository.save(application);
        log.debug("sendDocuments(), обновляем в заявке статус и историю статусов, сохраняемв базу обновленную application = {}", application);

        EmailMessage sendDocumentsEmailDTO = EmailMessage.builder()
                .address(application.getClient().getEmail())
                .theme(EmailMessage.ThemeEnum.SEND_DOCUMENTS)
                .applicationId(application.getId())
                .build();
        kafkaClient.send(topicSendDocuments, sendDocumentsEmailDTO);
        log.info("sendDocuments(), отправляем по kafka sendDocumentsEmailDTO = {}", sendDocumentsEmailDTO);
    }

    @Override
    public void signDocuments(Long applicationId) {
        log.debug("signDocuments(), applicationId = {}", applicationId);

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("В базе нет заявки с id = " + applicationId));
        log.debug("signDocuments(), подгружаеем из базы application = {}", application);

        String sesCode = String.format("%04d", random.nextInt(10000));
        application.setSesCode(sesCode);
        applicationRepository.save(application);
        log.debug("signDocuments(), генерируем ПЭП и добавляем в заявку, сохраняем в базу обновленную application = {}", application);

        EmailMessage sendSesEmailDTO = EmailMessage.builder()
                .address(application.getClient().getEmail())
                .theme(EmailMessage.ThemeEnum.SEND_SES)
                .applicationId(application.getId())
                .build();
        kafkaClient.send(topicSendSes, sendSesEmailDTO);
        log.info("signDocuments(), отправляем по kafka sendSesEmailDTO = {}", sendSesEmailDTO);
    }

    @Override
    public void signDocumentsSesCode(Long applicationId, String sesCode) {
        log.debug("signDocumentsSesCode(), applicationId = {}, sesCode = {}", applicationId, sesCode);

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("В базе нет заявки с id = " + applicationId));
        log.debug("signDocumentsSesCode(), подгружаеем из базы application = {}", application);

        if (application.getSesCode().equals(sesCode)){
            application.setStatus(ApplicationStatus.DOCUMENT_SIGNED);
            application.getStatusHistory().add(new ApplicationStatusHistoryDTO(application.getStatus(), LocalDateTime.now(),ChangeType.AUTOMATIC));
            applicationRepository.save(application);
            log.debug("signDocumentsSesCode(), обновляем статус заявки на DOCUMENT_SIGNED, " +
                    "обновляем историю статусов, " +
                    "сохраняем обновления в базу,application = {}", application);

            EmailMessage creditIssuedEmailDTO = EmailMessage.builder()
                    .address(application.getClient().getEmail())
                    .theme(EmailMessage.ThemeEnum.CREDIT_ISSUED)
                    .applicationId(application.getId())
                    .build();
            kafkaClient.send(topicCreditIssued, creditIssuedEmailDTO);
            log.info("signDocumentsSesCode(), отправляем по kafka creditIssuedEmailDTO = {}", creditIssuedEmailDTO);

        } else throw new IllegalArgumentException("Введенный код не верен");
    }

    @Override
    public ApplicationDTO getApplication(Long applicationId) {
        log.debug("getApplication(), applicationId = {}", applicationId);

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("В базе нет заявки с id = " + applicationId));
        log.debug("getApplication(), подгружаеем из базы, application = {}", application);

        ApplicationDTO applicationDTO = applicationMapper.mapEntityToDto(application);
        log.debug("getApplication(), преобразуем application в dto, return applicationDTO = {}", applicationDTO);

        return applicationDTO;
    }

    @Override
    public void updateStatus(Long applicationId, String status) {
        log.debug("updateStatus(), applicationId = {}, status = {}", applicationId, status);

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("В базе нет заявки с id = " + applicationId));
        log.debug("updateStatus(), подгружаеем из базы, application = {}", application);

        if (status.equalsIgnoreCase(DOCUMENTS_CREATED)){
            application.setStatus(ApplicationStatus.DOCUMENT_CREATED);
            log.debug("updateStatus(), обновляем статус заявки");

        } else if (status.equalsIgnoreCase(CLIENT_DENIED)){
            application.setStatus(ApplicationStatus.CLIENT_DENIED);
            log.debug("updateStatus(), обновляем статус заявки");

            EmailMessage applicationDeniedEmailDTO = EmailMessage.builder()
                    .address(application.getClient().getEmail())
                    .theme(EmailMessage.ThemeEnum.APPLICATION_DENIED)
                    .applicationId(application.getId())
                    .build();
            kafkaClient.send(topicApplicationDenied, applicationDeniedEmailDTO);
            log.info("updateStatus(), отправляем по kafka applicationDeniedEmailDTO = {}", applicationDeniedEmailDTO);
        }
        application.getStatusHistory().add(new ApplicationStatusHistoryDTO(application.getStatus(), LocalDateTime.now(),ChangeType.AUTOMATIC));
        applicationRepository.save(application);
        log.debug("updateStatus(), обновляем историю статуса, сохраняем все изменения в базу, application = {}", application);
    }

    @Override
    public List<ApplicationDTO> getAllApplication() {
        List<ApplicationDTO> allApplication =
                applicationRepository.findAll().stream().map(applicationMapper::mapEntityToDto).collect(Collectors.toList());
        log.debug("getAllApplication(), подгрузили из базы все заявки, return allApplication = {}", allApplication);
        return allApplication;
    }
}
