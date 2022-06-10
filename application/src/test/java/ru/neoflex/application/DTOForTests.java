package ru.neoflex.application;

import org.openapitools.model.LoanApplicationRequestDTO;
import org.openapitools.model.LoanOfferDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class DTOForTests {
    public static final LoanApplicationRequestDTO loanApplicationRequestDTOCorrect =
            LoanApplicationRequestDTO.builder()
                    .amount(BigDecimal.valueOf(100000))
                    .term(6)
                    .firstName("Kseniya")
                    .lastName("Gurova")
                    .middleName("Igorevna")
                    .email("blablabla@mail.ru")
                    .birthdate(LocalDate.of(1998, 12, 1))
                    .passportSeries("1111")
                    .passportNumber("666666")
                    .build();

    public static final LoanApplicationRequestDTO loanApplicationRequestDTOWrongAge =
            LoanApplicationRequestDTO.builder()
                    .amount(BigDecimal.valueOf(100000))
                    .term(6)
                    .firstName("Kseniya")
                    .lastName("Gurova")
                    .middleName("Igorevna")
                    .email("blablabla@mail.ru")
                    .birthdate(LocalDate.of(2007, 12, 1))
                    .passportSeries("1111")
                    .passportNumber("666666")
                    .build();

    public static final List<LoanOfferDTO> loanOfferDTOListFromLoanApplicationRequestDTOCorrect = List.of(
            LoanOfferDTO.builder()
                    .applicationId(4L)
                    .requestedAmount(BigDecimal.valueOf(100000))
                    .totalAmount(BigDecimal.valueOf(105913.68))
                    .term(6)
                    .monthlyPayment(BigDecimal.valueOf(17652.28))
                    .rate(BigDecimal.valueOf(20))
                    .isInsuranceEnabled(false)
                    .isSalaryClient(false)
                    .build(),
            LoanOfferDTO.builder()
                    .applicationId(3L)
                    .requestedAmount(BigDecimal.valueOf(100000))
                    .totalAmount(BigDecimal.valueOf(105614.22))
                    .term(6)
                    .monthlyPayment(BigDecimal.valueOf(17602.37))
                    .rate(BigDecimal.valueOf(19))
                    .isInsuranceEnabled(false)
                    .isSalaryClient(true)
                    .build(),
            LoanOfferDTO.builder()
                    .applicationId(2L)
                    .requestedAmount(BigDecimal.valueOf(100000))
                    .totalAmount(BigDecimal.valueOf(107315.12))
                    .term(6)
                    .monthlyPayment(BigDecimal.valueOf(17552.52))
                    .rate(BigDecimal.valueOf(18))
                    .isInsuranceEnabled(true)
                    .isSalaryClient(false)
                    .build(),
            LoanOfferDTO.builder()
                    .applicationId(1L)
                    .requestedAmount(BigDecimal.valueOf(100000))
                    .totalAmount(BigDecimal.valueOf(107016.44))
                    .term(6)
                    .monthlyPayment(BigDecimal.valueOf(17502.74))
                    .rate(BigDecimal.valueOf(17))
                    .isInsuranceEnabled(true)
                    .isSalaryClient(true)
                    .build());

    public static final LoanOfferDTO loanOfferDTOTrueTrue = LoanOfferDTO.builder()
            .applicationId(1L)
            .requestedAmount(BigDecimal.valueOf(100000))
            .totalAmount(BigDecimal.valueOf(107016.44))
            .term(6)
            .monthlyPayment(BigDecimal.valueOf(17502.74))
            .rate(BigDecimal.valueOf(17))
            .isInsuranceEnabled(true)
            .isSalaryClient(true)
            .build();

}
