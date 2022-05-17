package ru.neoflex.conveyor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.CreditDTO;
import org.openapitools.model.LoanApplicationRequestDTO;
import org.openapitools.model.LoanOfferDTO;
import org.openapitools.model.ScoringDataDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConveyorServiceImpl implements ConveyorService{
    @Value("${application.rate}")
    private BigDecimal baseRate;

    @Override
    public List<LoanOfferDTO> calculateCreditOffers(LoanApplicationRequestDTO dto) {
        if (ChronoUnit.YEARS.between(dto.getBirthdate(), LocalDate.now()) < 18){
            throw new IllegalArgumentException("Ваш возраст менее 18. Заявка не может быть выполнена");
        }
        LoanOfferDTO insuranceTrueSalaryTrue = LoanOfferDTO.builder()
                .applicationId(1L)
                .requestedAmount(dto.getAmount())
                .totalAmount(calculateMonthlyPayment(dto.getAmount().multiply(BigDecimal.valueOf(1.02)),
                        calculateCreditRate(true,true),
                        dto.getTerm())
                        .multiply(BigDecimal.valueOf(dto.getTerm())))
                .term(dto.getTerm())
                .monthlyPayment(calculateMonthlyPayment(
                        dto.getAmount().multiply(BigDecimal.valueOf(1.02)),
                        calculateCreditRate(true,true),
                        dto.getTerm()))
                .rate(calculateCreditRate(true,true))
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();

        LoanOfferDTO insuranceTrueSalaryFalse = LoanOfferDTO.builder()
                .applicationId(2L)
                .requestedAmount(dto.getAmount())
                .totalAmount(calculateMonthlyPayment(dto.getAmount().multiply(BigDecimal.valueOf(1.02)),
                        calculateCreditRate(true,false),
                        dto.getTerm())
                        .multiply(BigDecimal.valueOf(dto.getTerm())))
                .term(dto.getTerm())
                .monthlyPayment(calculateMonthlyPayment(
                        dto.getAmount().multiply(BigDecimal.valueOf(1.02)),
                        calculateCreditRate(true,false),
                        dto.getTerm()))
                .rate(calculateCreditRate(true,false))
                .isInsuranceEnabled(true)
                .isSalaryClient(false)
                .build();

        LoanOfferDTO insuranceFalseSalaryTrue = LoanOfferDTO.builder()
                .applicationId(3L)
                .requestedAmount(dto.getAmount())
                .totalAmount(calculateMonthlyPayment(dto.getAmount(),
                        calculateCreditRate(false,true),
                        dto.getTerm())
                        .multiply(BigDecimal.valueOf(dto.getTerm())))
                .term(dto.getTerm())
                .monthlyPayment(calculateMonthlyPayment(
                        dto.getAmount().multiply(BigDecimal.valueOf(1.02)),
                        calculateCreditRate(false,true),
                        dto.getTerm()))
                .rate(calculateCreditRate(false,true))
                .isInsuranceEnabled(false)
                .isSalaryClient(true)
                .build();

        LoanOfferDTO insuranceFalseSalaryFalse = LoanOfferDTO.builder()
                .applicationId(4L)
                .requestedAmount(dto.getAmount())
                .totalAmount(calculateMonthlyPayment(dto.getAmount(),
                        calculateCreditRate(false,false),
                        dto.getTerm())
                        .multiply(BigDecimal.valueOf(dto.getTerm())))
                .term(dto.getTerm())
                .monthlyPayment(calculateMonthlyPayment(
                        dto.getAmount().multiply(BigDecimal.valueOf(1.02)),
                        calculateCreditRate(false,false),
                        dto.getTerm()))
                .rate(calculateCreditRate(false,false))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();
        List<LoanOfferDTO> loanOffers = Arrays.asList(insuranceTrueSalaryTrue, insuranceTrueSalaryFalse, insuranceFalseSalaryTrue, insuranceFalseSalaryFalse);
        loanOffers = loanOffers.stream().sorted(Comparator.comparing(LoanOfferDTO::getRate).reversed()).collect(Collectors.toList());
        return loanOffers;
    }

    @Override
    public CreditDTO calculateCreditParameters(ScoringDataDTO scoringDataDTO) {
        return null;
    }

    public BigDecimal calculateCreditRate(Boolean isInsuranceEnabled, Boolean isSalaryClient){
        BigDecimal tempRate = baseRate;
        if (isInsuranceEnabled) tempRate = tempRate.subtract(BigDecimal.valueOf(2));
        if (isSalaryClient) tempRate = tempRate.subtract(BigDecimal.valueOf(1));
        return tempRate;
    }
    public BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term){
        //monthlyPayment = amount * (rate/100/12 * (1 + rate/100/12)^term) / ((1 + rate/100/12)^term - 1)
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);
        BigDecimal coefficientPart1 = monthlyRate.add(BigDecimal.valueOf(1)).pow(term);
        BigDecimal coefficientPart2 = monthlyRate.multiply(coefficientPart1);
        BigDecimal coefficientPart3 = coefficientPart1.subtract(BigDecimal.valueOf(1));
        BigDecimal coefficientPart4 = coefficientPart2.divide(coefficientPart3, 10, RoundingMode.HALF_UP);
        BigDecimal monthlyPayment = coefficientPart4.multiply(amount).setScale(2, RoundingMode.HALF_UP);

        return monthlyPayment;
    }
}
