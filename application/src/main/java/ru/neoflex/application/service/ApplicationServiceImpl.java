package ru.neoflex.application.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.LoanApplicationRequestDTO;
import org.openapitools.model.LoanOfferDTO;
import org.springframework.stereotype.Service;
import ru.neoflex.application.client.DealClient;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService{
    private final DealClient dealClient;
    private static final Integer PRESCORING_MATURITY_AGE = 18;

    @Override
    public void applyOffer(LoanOfferDTO loanOfferDTO) {
        log.debug("applyOffer(), loanOfferDTO = {}", loanOfferDTO);

        try {
            dealClient.applyOffer(loanOfferDTO);
            log.debug("applyOffer(), отправляем запрос /deal/offer");
        } catch (FeignException e) {
            log.error("applyOffer(), ошибка при обработке запроса с выбором одного из предложений: {}", e.getMessage());
        }

    }

    @Override
    public List<LoanOfferDTO> createApplication(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.debug("createApplication(), loanApplicationRequestDTO = {}", loanApplicationRequestDTO);

        if (ChronoUnit.YEARS.between(loanApplicationRequestDTO.getBirthdate(), LocalDate.now()) < PRESCORING_MATURITY_AGE){
            log.debug("calculateCreditOffers(), из-за несовершеннолетнего возраста заявка отклоняется");
            throw new IllegalArgumentException("Ваш возраст менее 18. Заявка не может быть выполнена");
        }
        List<LoanOfferDTO> loanOffersList = dealClient.getLoanOffers(loanApplicationRequestDTO);
        log.info("createApplication(), отправляем запрос /deal/application, return loanOffersList = {}", loanOffersList);
        return loanOffersList;
    }
}
