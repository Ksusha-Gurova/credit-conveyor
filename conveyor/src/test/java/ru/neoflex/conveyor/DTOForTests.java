package ru.neoflex.conveyor;

import org.openapitools.model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
                    .birthdate(LocalDate.of(2006, 12, 1))
                    .passportSeries("1111")
                    .passportNumber("666666")
                    .build();
    public static final ScoringDataDTO scoringDataDTOCorrectMan30_55DivorcedSelfEmployedInsuranceSalaryClient =
            ScoringDataDTO.builder()
                    .amount(BigDecimal.valueOf(900000))
                    .term(6)
                    .firstName("Vasili")
                    .lastName("Pupkin")
                    .gender(ScoringDataDTO.GenderEnum.MALE)
                    .birthdate(LocalDate.of(1988, 5, 12))
                    .passportSeries("1234")
                    .passportNumber("567890")
                    .passportIssueDate(LocalDate.of(2002, 6, 12))
                    .passportIssueBranch("abc")
                    .maritalStatus(ScoringDataDTO.MaritalStatusEnum.DIVORCED)
                    .dependentAmount(3)
                    .employment(EmploymentDTO.builder()
                            .employmentStatus(EmploymentDTO.EmploymentStatusEnum.SELF_EMPLOYED)
                            .employerINN("12345678900")
                            .salary(BigDecimal.valueOf(65000))
                            .workExperienceTotal(36)
                            .workExperienceCurrent(9)
                            .build())
                    .account("pupkin")
                    .isInsuranceEnabled(true)
                    .isSalaryClient(true)
                    .build();
    public static final ScoringDataDTO scoringDataDTOCorrectWoman35_60MariedBusinessOwnerSalaryClient =
            ScoringDataDTO.builder()
                    .amount(BigDecimal.valueOf(900000))
                    .term(6)
                    .firstName("Irina")
                    .lastName("Petrova")
                    .gender(ScoringDataDTO.GenderEnum.FEMALE)
                    .birthdate(LocalDate.of(1985, 5, 12))
                    .passportSeries("1234")
                    .passportNumber("567890")
                    .passportIssueDate(LocalDate.of(2002, 6, 12))
                    .passportIssueBranch("abc")
                    .maritalStatus(ScoringDataDTO.MaritalStatusEnum.MARIED)
                    .dependentAmount(1)
                    .employment(EmploymentDTO.builder()
                            .employmentStatus(EmploymentDTO.EmploymentStatusEnum.BUSINESS_OWNER)
                            .employerINN("12345678900")
                            .salary(BigDecimal.valueOf(65000))
                            .workExperienceTotal(36)
                            .workExperienceCurrent(9)
                            .build())
                    .account("irina")
                    .isInsuranceEnabled(false)
                    .isSalaryClient(true)
                    .build();
    public static final CreditDTO creditDTOFromScoringDataDTOCorrectMan30_55DivorcedSelfEmployedInsuranceSalaryClient =
            CreditDTO.builder()
                    .amount(BigDecimal.valueOf(900000))
                    .term(6)
                    .monthlyPayment(BigDecimal.valueOf(157524.68))
                    .rate(BigDecimal.valueOf(17))
                    .psk(BigDecimal.valueOf(23.692))
                    .isInsuranceEnabled(true)
                    .isSalaryClient(true)
                    .paymentSchedule(List.of(
                            PaymentScheduleElement.builder()
                                    .number(0)
                                    .date(LocalDate.now())
                                    .totalPayment(BigDecimal.valueOf(18000).setScale(2, RoundingMode.HALF_UP))
                                    .interestPayment(BigDecimal.ZERO)
                                    .debtPayment(BigDecimal.ZERO)
                                    .remainingDebt(BigDecimal.valueOf(900000))
                                    .build(),
                            PaymentScheduleElement.builder()
                                    .number(1)
                                    .date(LocalDate.now().plusMonths(1))
                                    .totalPayment(BigDecimal.valueOf(157524.68))
                                    .interestPayment(BigDecimal.valueOf(12750.00).setScale(2, RoundingMode.HALF_UP))
                                    .debtPayment(BigDecimal.valueOf(144774.68))
                                    .remainingDebt(BigDecimal.valueOf(755225.32))
                                    .build(),
                            PaymentScheduleElement.builder()
                                    .number(2)
                                    .date(LocalDate.now().plusMonths(2))
                                    .totalPayment(BigDecimal.valueOf(157524.68))
                                    .interestPayment(BigDecimal.valueOf(10699.03))
                                    .debtPayment(BigDecimal.valueOf(146825.65))
                                    .remainingDebt(BigDecimal.valueOf(608399.67))
                                    .build(),
                            PaymentScheduleElement.builder()
                                    .number(3)
                                    .date(LocalDate.now().plusMonths(3))
                                    .totalPayment(BigDecimal.valueOf(157524.68))
                                    .interestPayment(BigDecimal.valueOf(8619.00).setScale(2,RoundingMode.HALF_UP))
                                    .debtPayment(BigDecimal.valueOf(148905.68))
                                    .remainingDebt(BigDecimal.valueOf(459493.99))
                                    .build(),
                            PaymentScheduleElement.builder()
                                    .number(4)
                                    .date(LocalDate.now().plusMonths(4))
                                    .totalPayment(BigDecimal.valueOf(157524.68))
                                    .interestPayment(BigDecimal.valueOf(6509.50).setScale(2, RoundingMode.HALF_UP))
                                    .debtPayment(BigDecimal.valueOf(151015.18))
                                    .remainingDebt(BigDecimal.valueOf(308478.81))
                                    .build(),
                            PaymentScheduleElement.builder()
                                    .number(5)
                                    .date(LocalDate.now().plusMonths(5))
                                    .totalPayment(BigDecimal.valueOf(157524.68))
                                    .interestPayment(BigDecimal.valueOf(4370.12))
                                    .debtPayment(BigDecimal.valueOf(153154.56))
                                    .remainingDebt(BigDecimal.valueOf(155324.25))
                                    .build(),
                            PaymentScheduleElement.builder()
                                    .number(6)
                                    .date(LocalDate.now().plusMonths(6))
                                    .totalPayment(BigDecimal.valueOf(157524.68))
                                    .interestPayment(BigDecimal.valueOf(2200.43))
                                    .debtPayment(BigDecimal.valueOf(155324.25))
                                    .remainingDebt(BigDecimal.ZERO)
                                    .build()
                    ))
                    .build();
    public static final CreditDTO creditDTOFromScoringDataDTOCorrectWoman35_60MariedBusinessOwnerSalaryClient =
            CreditDTO.builder()
                    .amount(BigDecimal.valueOf(900000))
                    .term(6)
                    .monthlyPayment(BigDecimal.valueOf(157077.26))
                    .rate(BigDecimal.valueOf(16))
                    .psk(BigDecimal.valueOf(15.699))
                    .isInsuranceEnabled(false)
                    .isSalaryClient(true)
                    .paymentSchedule(List.of(
                            PaymentScheduleElement.builder()
                                    .number(0)
                                    .date(LocalDate.now())
                                    .totalPayment(BigDecimal.ZERO)
                                    .interestPayment(BigDecimal.ZERO)
                                    .debtPayment(BigDecimal.ZERO)
                                    .remainingDebt(BigDecimal.valueOf(900000))
                                    .build(),
                            PaymentScheduleElement.builder()
                                    .number(1)
                                    .date(LocalDate.now().plusMonths(1))
                                    .totalPayment(BigDecimal.valueOf(157077.26))
                                    .interestPayment(BigDecimal.valueOf(12000.00).setScale(2, RoundingMode.HALF_UP))
                                    .debtPayment(BigDecimal.valueOf(145077.26))
                                    .remainingDebt(BigDecimal.valueOf(754922.74))
                                    .build(),
                            PaymentScheduleElement.builder()
                                    .number(2)
                                    .date(LocalDate.now().plusMonths(2))
                                    .totalPayment(BigDecimal.valueOf(157077.26))
                                    .interestPayment(BigDecimal.valueOf(10065.64))
                                    .debtPayment(BigDecimal.valueOf(147011.62))
                                    .remainingDebt(BigDecimal.valueOf(607911.12))
                                    .build(),
                            PaymentScheduleElement.builder()
                                    .number(3)
                                    .date(LocalDate.now().plusMonths(3))
                                    .totalPayment(BigDecimal.valueOf(157077.26))
                                    .interestPayment(BigDecimal.valueOf(8105.48))
                                    .debtPayment(BigDecimal.valueOf(148971.78))
                                    .remainingDebt(BigDecimal.valueOf(458939.34))
                                    .build(),
                            PaymentScheduleElement.builder()
                                    .number(4)
                                    .date(LocalDate.now().plusMonths(4))
                                    .totalPayment(BigDecimal.valueOf(157077.26))
                                    .interestPayment(BigDecimal.valueOf(6119.19))
                                    .debtPayment(BigDecimal.valueOf(150958.07))
                                    .remainingDebt(BigDecimal.valueOf(307981.27))
                                    .build(),
                            PaymentScheduleElement.builder()
                                    .number(5)
                                    .date(LocalDate.now().plusMonths(5))
                                    .totalPayment(BigDecimal.valueOf(157077.26))
                                    .interestPayment(BigDecimal.valueOf(4106.42))
                                    .debtPayment(BigDecimal.valueOf(152970.84))
                                    .remainingDebt(BigDecimal.valueOf(155010.43))
                                    .build(),
                            PaymentScheduleElement.builder()
                                    .number(6)
                                    .date(LocalDate.now().plusMonths(6))
                                    .totalPayment(BigDecimal.valueOf(157077.24))
                                    .interestPayment(BigDecimal.valueOf(2066.81))
                                    .debtPayment(BigDecimal.valueOf(155010.43))
                                    .remainingDebt(BigDecimal.ZERO)
                                    .build()
                    ))
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

}
