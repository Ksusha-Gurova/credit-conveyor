package ru.neoflex.conveyor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConveyorServiceImpl implements ConveyorService{

    private static final Integer PRESCORING_MATURITY_AGE = 18;
    private static final Integer SCORING_MIN_AGE = 20;
    private static final Integer SCORING_MAX_AGE = 60;
    private static final BigDecimal MAX_SALARY_MULTIPLIER = BigDecimal.valueOf(20);
    private static final Integer MIN_WORK_EXPERIENCE_TOTAL = 12;
    private static final Integer MIN_WORK_EXPERIENCE_CURRENT = 3;
    private static final BigDecimal selfEmployedValue = BigDecimal.valueOf(1);
    private static final BigDecimal businessOwnerValue = BigDecimal.valueOf(3);
    private static final BigDecimal middleManagerValue = BigDecimal.valueOf(-2);
    private static final BigDecimal topManagerValue = BigDecimal.valueOf(-4);
    private static final BigDecimal marriedValue = BigDecimal.valueOf(-3);
    private static final BigDecimal divorcedValue = BigDecimal.valueOf(1);
    private static final BigDecimal dependentAmountValue = BigDecimal.valueOf(1);
    private static final BigDecimal female35_60Value = BigDecimal.valueOf(-3);
    private static final BigDecimal male30_55Value = BigDecimal.valueOf(-3);
    private static final BigDecimal nonBinaryValue = BigDecimal.valueOf(3);
    private static final BigDecimal insuranceValue = BigDecimal.valueOf(-2);
    private static final BigDecimal salaryClientValue = BigDecimal.valueOf(-1);

    @Value("${application.rate}")
    private BigDecimal baseRate;
    @Value("${application.insurance}")
    private BigDecimal insuranceCost;

    @Override
    public List<LoanOfferDTO> calculateCreditOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.debug("calculateCreditOffers(), loanApplicationRequestDTO = {}", loanApplicationRequestDTO);

        ageVerification(loanApplicationRequestDTO.getBirthdate());

        LoanOfferDTO insuranceTrueSalaryTrue = createOffer(1L,loanApplicationRequestDTO, true, true);
        log.debug("calculateCreditOffers(), создали предложение с включенной страховкой и для зарплатного клиента, insuranceTrueSalaryTrue = {}", insuranceTrueSalaryTrue);

        LoanOfferDTO insuranceTrueSalaryFalse = createOffer(2L, loanApplicationRequestDTO, true, false);
        log.debug("calculateCreditOffers(), создали предложение с включенной страховкой, insuranceTrueSalaryFalse = {}", insuranceTrueSalaryFalse);

        LoanOfferDTO insuranceFalseSalaryTrue = createOffer(3L, loanApplicationRequestDTO, false, true);
        log.debug("calculateCreditOffers(), создали предложение без страховки для зарплатного клиента, insuranceFalseSalaryTrue = {}", insuranceFalseSalaryTrue);

        LoanOfferDTO insuranceFalseSalaryFalse = createOffer(4L, loanApplicationRequestDTO, false, false);
        log.debug("calculateCreditOffers(), создали предложение без страховки, insuranceFalseSalaryFalse = {}", insuranceFalseSalaryFalse);

        List<LoanOfferDTO> loanOffers = Arrays.asList(insuranceTrueSalaryTrue, insuranceTrueSalaryFalse, insuranceFalseSalaryTrue, insuranceFalseSalaryFalse);
        log.debug("calculateCreditOffers(), создали список со всеми ранее инициализированными предложениями, loanOffers = {}", loanOffers);

        loanOffers = loanOffers.stream().sorted(Comparator.comparing(LoanOfferDTO::getRate).reversed()).toList();
        log.info("calculateCreditOffers(), отсортировали список с предложениями (от худшего к лучшему) по ставке, return loanOffers = {}", loanOffers);

        return loanOffers;
    }

    @Override
    public CreditDTO calculateCreditParameters(ScoringDataDTO scoringDataDTO) {
        log.debug("calculateCreditParameters(), scoringDataDTO = {}", scoringDataDTO);

        List<String> creditDeclineMessagesList = new ArrayList<>();

        if (scoringDataDTO.getEmployment().getEmploymentStatus().equals(EmploymentDTO.EmploymentStatusEnum.UNEMPLOYED)){
            log.debug("calculateCreditParameters(), из-за рабочего статуса - безработный в кредите отказано");
            creditDeclineMessagesList.add("Ваш рабочий статус: безработный");
        }
        if (scoringDataDTO.getEmployment().getSalary().multiply(MAX_SALARY_MULTIPLIER).compareTo(scoringDataDTO.getAmount()) < 0){
            log.debug("calculateCreditParameters(), из-за размера кредита превышающего 20 зарплат в кредите отказано");
            creditDeclineMessagesList.add("Запрашиваемая сумма больше 20-и зарплат");
        }
        if (ChronoUnit.YEARS.between(scoringDataDTO.getBirthdate(), LocalDate.now()) < SCORING_MIN_AGE
                || ChronoUnit.YEARS.between(scoringDataDTO.getBirthdate(), LocalDate.now()) > SCORING_MAX_AGE){
            log.debug("calculateCreditParameters(), из-за возраста равного менее 20 или более 60 в кредите отказано");
            creditDeclineMessagesList.add("Ваш возраст менее 20-и или более 60-и");
        }
        if (scoringDataDTO.getEmployment().getWorkExperienceTotal() < MIN_WORK_EXPERIENCE_TOTAL
                || scoringDataDTO.getEmployment().getWorkExperienceCurrent() < MIN_WORK_EXPERIENCE_CURRENT){
            log.debug("calculateCreditParameters(), из-за общего стажа работы равного менее 12 месяцам или текущего стажа работы равного менее 3 месяцам в кредите отказано");
            creditDeclineMessagesList.add("Ваш общий стаж работы менее 12-и месяцев и/или текущий стаж работы менее 3-х месяцев");
        }
        if (!creditDeclineMessagesList.isEmpty()){
            throw new IllegalArgumentException("Ваша заявка не может быть одобрена по следующим причинам: " +
                    String.join(", ", creditDeclineMessagesList));
        }

        BigDecimal rate = calculateCreditRate(scoringDataDTO);
        log.debug("calculateCreditParameters(), расчитали процентную ставку по кредиту, rate = {}", rate);

        BigDecimal monthlyPayment = calculateMonthlyPayment(scoringDataDTO.getAmount(), rate, scoringDataDTO.getTerm());
        log.debug("calculateCreditParameters(), расчитали ежемесячный платеж, monthlyPayment = {}", monthlyPayment);

        List<PaymentScheduleElement> paymentScheduleElements = calculatePaymentScheduleElement(
                scoringDataDTO.getAmount(),
                monthlyPayment,
                rate,
                scoringDataDTO.getTerm(),
                scoringDataDTO.getIsInsuranceEnabled());
        log.debug("calculateCreditParameters(), создан список ежемесячных платежей, paymentScheduleElements = {}", paymentScheduleElements);

        CreditDTO creditDTO = CreditDTO.builder()
                .amount(scoringDataDTO.getAmount())
                .term(scoringDataDTO.getTerm())
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .psk(calculatePsk(scoringDataDTO.getAmount(), paymentScheduleElements))
                .isInsuranceEnabled(scoringDataDTO.getIsInsuranceEnabled())
                .isSalaryClient(scoringDataDTO.getIsSalaryClient())
                .paymentSchedule(paymentScheduleElements)
                .build();

        log.info("calculateCreditParameters(), return creditDTO = {}", creditDTO);
        return creditDTO;
    }

    private BigDecimal calculateCreditRate(boolean isInsuranceEnabled, boolean isSalaryClient){
        log.debug("calculateCreditRate(), isInsuranceEnabled = {}, isSalaryClient = {}", isInsuranceEnabled, isSalaryClient);

        BigDecimal tempRate = baseRate;
        log.debug("calculateCreditRate(), создаем tempRate = {}", tempRate);

        if (isInsuranceEnabled) {
            tempRate = tempRate.subtract(BigDecimal.valueOf(2));
            log.debug("calculateCreditRate(), ставка уменьшается на 2, tempRate = {}", tempRate);
        }
        if (isSalaryClient) {
            tempRate = tempRate.subtract(BigDecimal.valueOf(1));
            log.debug("calculateCreditRate(), ставка уменьшается на 1б tempRate = {}", tempRate);
        }
        log.info("calculateCreditRate(), return tempRate = {}", tempRate);
        return tempRate;
    }

    private BigDecimal calculateCreditRate(ScoringDataDTO dto){
        log.debug("calculateCreditRate(), dto = {}", dto);

        BigDecimal tempRate = baseRate;
        log.debug("calculateCreditRate(), ставка равна базовой ставке tempRate = {}", tempRate);
        if (dto.getEmployment().getEmploymentStatus().equals(EmploymentDTO.EmploymentStatusEnum.SELF_EMPLOYED)){
            tempRate = tempRate.add(selfEmployedValue);
            log.debug("calculateCreditRate(), к базовой ставке прибавляется {} для самозанятого, tempRate = {}",selfEmployedValue, tempRate);
        }
        if (dto.getEmployment().getEmploymentStatus().equals(EmploymentDTO.EmploymentStatusEnum.BUSINESS_OWNER)){
            tempRate = tempRate.add(businessOwnerValue);
            log.debug("calculateCreditRate(), к базовой ставке прибавляется {} для владельца бизнеса, tempRate = {}",businessOwnerValue, tempRate);
        }
        if (Objects.equals(dto.getEmployment().getPosition(), EmploymentDTO.PositionEnum.MIDDLE_MANAGER)){
            tempRate = tempRate.add(middleManagerValue);
            log.debug("calculateCreditRate(), к базовой ставке прибавляется {} для менеджера среднего звена, tempRate = {}",middleManagerValue, tempRate);
        }
        if (Objects.equals(dto.getEmployment().getPosition(), EmploymentDTO.PositionEnum.TOP_MANAGER)){
            tempRate = tempRate.add(topManagerValue);
            log.debug("calculateCreditRate(), к базовой ставке прибавляется {} для топ-менеджера, tempRate = {}",topManagerValue, tempRate);
        }
        if (dto.getMaritalStatus().equals(ScoringDataDTO.MaritalStatusEnum.MARRIED)){
            tempRate = tempRate.add(marriedValue);
            log.debug("calculateCreditRate(), к базовой ставке прибавляется {} для зумужней/женатого, tempRate = {}",marriedValue, tempRate);
        }
        if (dto.getMaritalStatus().equals(ScoringDataDTO.MaritalStatusEnum.DIVORCED)){
            tempRate = tempRate.add(divorcedValue);
            log.debug("calculateCreditRate(), к базовой ставке прибавляется {} для разведенного, tempRate = {}",divorcedValue, tempRate);
        }
        if (dto.getDependentAmount() > 1){
            tempRate = tempRate.add(dependentAmountValue);
            log.debug("calculateCreditRate(), к базовой ставке прибавляется {} если еждевенцев более 1, tempRate = {}",dependentAmountValue, tempRate);
        }
        if (dto.getGender().equals(ScoringDataDTO.GenderEnum.FEMALE)
                && ChronoUnit.YEARS.between(dto.getBirthdate(), LocalDate.now()) >= 35
                && ChronoUnit.YEARS.between(dto.getBirthdate(), LocalDate.now()) <= 60){
            tempRate = tempRate.add(female35_60Value);
            log.debug("calculateCreditRate(), к базовой ставке прибавляется {} для женщины от 30 до 60 лет, tempRate = {}",female35_60Value, tempRate);
        }
        if (dto.getGender().equals(ScoringDataDTO.GenderEnum.MALE)
                && ChronoUnit.YEARS.between(dto.getBirthdate(), LocalDate.now()) >= 30
                && ChronoUnit.YEARS.between(dto.getBirthdate(), LocalDate.now()) <= 55){
            tempRate = tempRate.add(male30_55Value);
            log.debug("calculateCreditRate(), к базовой ставке прибавляется {} для мужчины от 30 до 55 лет, tempRate = {}",male30_55Value, tempRate);
        }
        if (dto.getGender().equals(ScoringDataDTO.GenderEnum.NON_BINARY)){
            tempRate = tempRate.add(nonBinaryValue);
            log.debug("calculateCreditRate(), к базовой ставке прибавляется {} для не бинарного, tempRate = {}",nonBinaryValue, tempRate);
        }
        if (dto.getIsInsuranceEnabled()) {
            tempRate = tempRate.add(insuranceValue);
            log.debug("calculateCreditRate(), к базовой ставке прибавляется {} если включена страховка, tempRate = {}",insuranceValue, tempRate);
        }
        if (dto.getIsSalaryClient()) {
            tempRate = tempRate.add(salaryClientValue);
            log.debug("calculateCreditRate(), к базовой ставке прибавляется {} если зарплатный клиент, tempRate = {}",salaryClientValue, tempRate);
        }
        log.info("calculateCreditRate(), return tempRate = {}", tempRate);
        return tempRate;
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term){
        log.debug("calculateMonthlyPayment(), amount = {}, rate = {}, term = {}", amount, rate, term);
        //Ссылка на источник с формулой расчета ежемесячного платежа https://journal.tinkoff.ru/guide/credit-payment/
        //monthlyPayment = amount * (rate/100/12 * (1 + rate/100/12)^term) / ((1 + rate/100/12)^term - 1)
        //                           monthlyRate      coefficientPart1                  coefficientPart3
        //                                  coefficientPart2        coefficientPart4
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);
        BigDecimal coefficientPart1 = monthlyRate.add(BigDecimal.valueOf(1)).pow(term);
        BigDecimal coefficientPart2 = monthlyRate.multiply(coefficientPart1);
        BigDecimal coefficientPart3 = coefficientPart1.subtract(BigDecimal.valueOf(1));
        BigDecimal coefficientPart4 = coefficientPart2.divide(coefficientPart3, 10, RoundingMode.HALF_UP);
        BigDecimal monthlyPayment = coefficientPart4.multiply(amount).setScale(2, RoundingMode.HALF_UP);
        log.info("calculateMonthlyPayment(), расчитали ежемесячный платеж, return monthlyPayment = {}", monthlyPayment);
        return monthlyPayment;
    }

    private BigDecimal calculateTotalAmount(BigDecimal amount, BigDecimal rate, Integer term, boolean isInsuranceEnabled){
        log.debug("calculateTotalAmount(), amount = {}, rate = {}, term = {}, isInsuranceEnabled = {}", amount, rate, term, isInsuranceEnabled);
        BigDecimal totalAmount;
        log.debug("calculateTotalAmount(), создана переменная totalAmount");
        if (isInsuranceEnabled){
            totalAmount =
                    calculateMonthlyPayment(amount, rate, term)
                            .multiply(BigDecimal.valueOf(term))
                            .add(amount.multiply(insuranceCost));
        } else totalAmount =
                calculateMonthlyPayment(amount, rate, term)
                        .multiply(BigDecimal.valueOf(term));
        log.info("calculateTotalAmount(), return totalAmount = {}", totalAmount);
        return totalAmount;
    }

    private LoanOfferDTO createOffer(Long id,LoanApplicationRequestDTO dto, Boolean isInsuranceEnabled, Boolean isSalaryClient){
        log.debug("createOffer(), id = {}, dto = {}, isInsuranceEnabled = {}, isSalaryClient = {}", id, dto, isInsuranceEnabled, isSalaryClient);
        LoanOfferDTO loanOfferDTO = LoanOfferDTO.builder()
                .applicationId(id)
                .requestedAmount(dto.getAmount())
                .totalAmount(calculateTotalAmount(
                        dto.getAmount(),
                        calculateCreditRate(isInsuranceEnabled,isSalaryClient),
                        dto.getTerm(), isInsuranceEnabled))
                .term(dto.getTerm())
                .monthlyPayment(calculateMonthlyPayment(
                        dto.getAmount(),
                        calculateCreditRate(isInsuranceEnabled,isSalaryClient),
                        dto.getTerm()))
                .rate(calculateCreditRate(isInsuranceEnabled,isSalaryClient))
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();
        log.info("createOffer(), создано кредитное предложение, return loanOfferDTO = {}", loanOfferDTO);
        return loanOfferDTO;
    }

    private List<PaymentScheduleElement> calculatePaymentScheduleElement(
            BigDecimal amount,
            BigDecimal monthlyPayment,
            BigDecimal rate,
            Integer term,
            boolean isInsuranceEnabled){
        log.debug("calculatePaymentScheduleElement(), amount = {}, monthlyPayment = {}, rate = {}, term = {}, isInsuranceEnabled = {}",
                amount, monthlyPayment, rate, term, isInsuranceEnabled);

        List<PaymentScheduleElement> listPayments = new ArrayList<>();
        log.debug("calculatePaymentScheduleElement(), создали список платежей, listPayments = {}", listPayments);

        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(1200), 15, RoundingMode.HALF_UP);
        if (isInsuranceEnabled){
            listPayments.add(PaymentScheduleElement.builder()
                    .number(0)
                    .date(LocalDate.now())
                    .totalPayment(amount.multiply(insuranceCost))
                    .interestPayment(BigDecimal.valueOf(0))
                    .debtPayment(BigDecimal.valueOf(0))
                    .remainingDebt(amount)
                    .build());
            log.debug("calculatePaymentScheduleElement(), добавили в список платеж №0 с стоимостью страховки, PaymentScheduleElement = {}", listPayments.get(0));
        } else {
            listPayments.add(PaymentScheduleElement.builder()
                    .number(0)
                    .date(LocalDate.now())
                    .totalPayment(BigDecimal.valueOf(0))
                    .interestPayment(BigDecimal.valueOf(0))
                    .debtPayment(BigDecimal.valueOf(0))
                    .remainingDebt(amount)
                    .build());
            log.debug("calculatePaymentScheduleElement(), добавили в список платеж №0, PaymentScheduleElement = {}", listPayments.get(0));
        }
        for (int i = 1; i < term; i++){
            listPayments.add(PaymentScheduleElement.builder()
                    .number(i)
                    .date(LocalDate.now().plusMonths(i))
                    .totalPayment(monthlyPayment)
                    .interestPayment(listPayments.get(i-1).getRemainingDebt().multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP))
                    .debtPayment(monthlyPayment.subtract(listPayments.get(i-1).getRemainingDebt().multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP)))
                    .remainingDebt(listPayments.get(i-1).getRemainingDebt().subtract(monthlyPayment.subtract(listPayments.get(i-1).getRemainingDebt().multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP))))
                    .build());
            log.debug("calculatePaymentScheduleElement(), добавили в список следующий платеж, PaymentScheduleElement = {}", listPayments.get(i));
        }
        listPayments.add(PaymentScheduleElement.builder()
                .number(term)
                .date(LocalDate.now().plusMonths(term))
                .totalPayment(listPayments.get(term - 1).getRemainingDebt().add(listPayments.get(term - 1).getRemainingDebt().multiply(monthlyRate)).setScale(2, RoundingMode.HALF_UP))
                .interestPayment(listPayments.get(term - 1).getRemainingDebt().multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP))
                .debtPayment(listPayments.get(term - 1).getRemainingDebt().setScale(2, RoundingMode.HALF_UP))
                .remainingDebt(BigDecimal.valueOf(0))
                .build());
        log.debug("calculatePaymentScheduleElement(), добавили в список последний платеж, PaymentScheduleElement = {}", listPayments.get(term));
        log.info("calculatePaymentScheduleElement(), return listPayments = {}", listPayments);
        return listPayments;
    }

    private BigDecimal calculatePsk(BigDecimal amount, List<PaymentScheduleElement> listPaymentScheduleElements) {
        log.debug("calculatePsk(), amount = {}, listPaymentScheduleElements = {}", amount, listPaymentScheduleElements);
        // Ссылка на иисточник с формулой рачета ПСК https://ru.wikipedia.org/wiki/%D0%9F%D0%BE%D0%BB%D0%BD%D0%B0%D1%8F_%D1%81%D1%82%D0%BE%D0%B8%D0%BC%D0%BE%D1%81%D1%82%D1%8C_%D0%BA%D1%80%D0%B5%D0%B4%D0%B8%D1%82%D0%B0
        List<BigDecimal> listTotalPayments = listPaymentScheduleElements.stream().map(PaymentScheduleElement::getTotalPayment).toList();
        BigDecimal i = BigDecimal.ZERO;
        BigDecimal d = BigDecimal.valueOf(0.1);
        Integer startAccuracy = 0;
        Integer maxAccuracy = 10;
        BigDecimal amountSubstractInsurance = amount.multiply(BigDecimal.valueOf(-1)).add(listTotalPayments.get(0));
        while (startAccuracy <= maxAccuracy) {
            BigDecimal result = amountSubstractInsurance;
            for (int j = 1; j < listTotalPayments.size(); j++) {
                result = result.add(listTotalPayments.get(j).divide(i.add(BigDecimal.ONE).pow(j), 10, RoundingMode.HALF_UP));
            }

            if (result.compareTo(BigDecimal.ZERO) > 0)
                i = i.add(d);
            else
                i = i.subtract(d);

            if (result.setScale(startAccuracy, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO) < 0) {
                d = d.divide(BigDecimal.valueOf(10), 10, RoundingMode.HALF_UP);
                startAccuracy++;
            }

            if (startAccuracy.equals(maxAccuracy)) break;
        }
        log.debug("calculatePsk(), расчитали значение i (составляющая формулы psk), i = {}", i);

        BigDecimal psk = i.multiply(BigDecimal.valueOf(365).divide(BigDecimal.valueOf(31),10, RoundingMode.HALF_UP))
                .multiply(BigDecimal.valueOf(100))
                .setScale(3, RoundingMode.HALF_UP);
        log.info("calculatePsk(), расчитали значение psk, return psk = {}", psk);
        return psk;
    }

    @Deprecated
    void ageVerification(LocalDate birthdate){
         if (ChronoUnit.YEARS.between(birthdate, LocalDate.now()) < PRESCORING_MATURITY_AGE){
            log.debug("ageVerification(), из-за несовершеннолетнего возраста заявка отклоняется");
            throw new IllegalArgumentException("Ваш возраст менее 18. Заявка не может быть выполнена");
        }
    }
}
