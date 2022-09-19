package ru.neoflex.deal;

import org.openapitools.model.*;
import ru.neoflex.deal.model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DTOForTests {
    public static final LoanApplicationRequestDTO loanApplicationRequestDTOCorrect = LoanApplicationRequestDTO.builder()
            .amount(BigDecimal.valueOf(100000.00))
            .term(6)
            .firstName("Kseniya")
            .lastName("Gurova")
            .email("blablabla@mail.ru")
            .birthdate(LocalDate.of(1985, 3, 20))
            .passportSeries("1234")
            .passportNumber("567890")
            .build();

    public static final Client clientForLoanApplicationRequestDTOCorrect = Client.builder()
            .firstName("Kseniya")
            .lastName("Gurova")
            .email("blablabla@mail.ru")
            .birthdate(LocalDate.of(1985, 3, 20))
            .passport(Passport.builder()
                    .series("1234")
                    .number("567890")
                    .build())
            .build();
    public static final Application applicationForLoanApplicationRequestDTOCorrect = Application.builder()
            .id(1L)
            .client(clientForLoanApplicationRequestDTOCorrect)
            .creationDate(LocalDate.now())
            .build();

    public static final LoanOfferDTO loanOfferDTOFalseFalse = LoanOfferDTO.builder()
            .applicationId(1L)
            .requestedAmount(BigDecimal.valueOf(100000))
            .totalAmount(BigDecimal.valueOf(105913.68))
            .term(6)
            .monthlyPayment(BigDecimal.valueOf(17652.28))
            .rate(BigDecimal.valueOf(20))
            .isInsuranceEnabled(false)
            .isSalaryClient(false)
            .build();

    public static final LoanOfferDTO loanOfferDTOFalseTrue = LoanOfferDTO.builder()
            .applicationId(1L)
            .requestedAmount(BigDecimal.valueOf(100000))
            .totalAmount(BigDecimal.valueOf(105614.22))
            .term(6)
            .monthlyPayment(BigDecimal.valueOf(17602.37))
            .rate(BigDecimal.valueOf(19))
            .isInsuranceEnabled(false)
            .isSalaryClient(true)
            .build();

    public static final LoanOfferDTO loanOfferDTOTrueFalse = LoanOfferDTO.builder()
            .applicationId(1L)
            .requestedAmount(BigDecimal.valueOf(100000))
            .totalAmount(BigDecimal.valueOf(107315.12))
            .term(6)
            .monthlyPayment(BigDecimal.valueOf(17552.52))
            .rate(BigDecimal.valueOf(18))
            .isInsuranceEnabled(true)
            .isSalaryClient(false)
            .build();

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
    public static final List<LoanOfferDTO> loanOfferDTOListFromLoanApplicationRequestDTOCorrect =
            List.of(loanOfferDTOFalseFalse, loanOfferDTOFalseTrue, loanOfferDTOTrueFalse, loanOfferDTOTrueTrue);

    public static final FinishRegistrationRequestDTO finishRegistrationRequestDTO = FinishRegistrationRequestDTO.builder()
            .gender(Gender.FEMALE)
            .maritalStatus(MaritalStatus.MARRIED)
            .dependentAmount(2)
            .passportIssueDate(LocalDate.of(2000, 12, 13))
            .passportIssueBranch("blablabla")
            .employment(EmploymentDTO.builder()
                    .employmentStatus(EmploymentStatus.BUSINESS_OWNER)
                    .employerINN("12345678900")
                    .salary(BigDecimal.valueOf(65000))
                    .position(Position.OWNER)
                    .workExperienceTotal(36)
                    .workExperienceCurrent(12)
                    .build())
            .account("123456789")
            .build();

    public static final Application application = Application.builder()
            .id(1L)
            .client(clientForLoanApplicationRequestDTOCorrect)
            .status(ApplicationStatus.APPROVED)
            .creationDate(LocalDate.now())
            .appliedOffer(loanOfferDTOTrueTrue)
            .statusHistory(new ArrayList<>(List.of(ApplicationStatusHistoryDTO.builder()
                    .time(LocalDateTime.now())
                    .status(ApplicationStatus.APPROVED)
                    .changeType(ChangeType.AUTOMATIC)
                    .build())))
            .build();

    public static final Employment employment = Employment.builder()
            .employmentStatus("BUSINESS_OWNER")
            .salary(BigDecimal.valueOf(65000))
            .position("OWNER")
            .workExperienceTotal(36)
            .workExperienceCurrent(12)
            .build();

    public static final List<PaymentScheduleElement> scheduleElementList = List.of(
            PaymentScheduleElement.builder()
                    .number(0)
                    .date(LocalDate.now())
                    .totalPayment(BigDecimal.valueOf(2000).setScale(2, RoundingMode.HALF_UP))
                    .interestPayment(BigDecimal.valueOf(0))
                    .debtPayment(BigDecimal.valueOf(0))
                    .remainingDebt(BigDecimal.valueOf(100000))
                    .build(),
            PaymentScheduleElement.builder()
                    .number(1)
                    .date(LocalDate.now().plusMonths(1))
                    .totalPayment(BigDecimal.valueOf(17403.38))
                    .interestPayment(BigDecimal.valueOf(1250.00).setScale(2, RoundingMode.HALF_UP))
                    .debtPayment(BigDecimal.valueOf(16153.38))
                    .remainingDebt(BigDecimal.valueOf(83846.62))
                    .build(),
            PaymentScheduleElement.builder()
                    .number(2)
                    .date(LocalDate.now().plusMonths(2))
                    .totalPayment(BigDecimal.valueOf(17403.38))
                    .interestPayment(BigDecimal.valueOf(1048.08))
                    .debtPayment(BigDecimal.valueOf(16355.3).setScale(2, RoundingMode.HALF_UP))
                    .remainingDebt(BigDecimal.valueOf(67491.32))
                    .build(),
            PaymentScheduleElement.builder()
                    .number(3)
                    .date(LocalDate.now().plusMonths(3))
                    .totalPayment(BigDecimal.valueOf(17403.38))
                    .interestPayment(BigDecimal.valueOf(843.64))
                    .debtPayment(BigDecimal.valueOf(16559.74))
                    .remainingDebt(BigDecimal.valueOf(50931.58))
                    .build(),
            PaymentScheduleElement.builder()
                    .number(4)
                    .date(LocalDate.now().plusMonths(4))
                    .totalPayment(BigDecimal.valueOf(17403.38))
                    .interestPayment(BigDecimal.valueOf(636.64))
                    .debtPayment(BigDecimal.valueOf(16766.74))
                    .remainingDebt(BigDecimal.valueOf(34164.84))
                    .build(),
            PaymentScheduleElement.builder()
                    .number(5)
                    .date(LocalDate.now().plusMonths(5))
                    .totalPayment(BigDecimal.valueOf(17403.38))
                    .interestPayment(BigDecimal.valueOf(427.06))
                    .debtPayment(BigDecimal.valueOf(16976.32))
                    .remainingDebt(BigDecimal.valueOf(17188.52))
                    .build(),
            PaymentScheduleElement.builder()
                    .number(6)
                    .date(LocalDate.now().plusMonths(6))
                    .totalPayment(BigDecimal.valueOf(17403.38))
                    .interestPayment(BigDecimal.valueOf(214.86))
                    .debtPayment(BigDecimal.valueOf(17188.52))
                    .remainingDebt(BigDecimal.valueOf(0))
                    .build());

    public static final CreditDTO creditDTO = CreditDTO.builder()
            .amount(BigDecimal.valueOf(100000))
            .term(6)
            .monthlyPayment(BigDecimal.valueOf(17403.38))
            .rate(BigDecimal.valueOf(15))
            .psk(BigDecimal.valueOf(21.709))
            .isInsuranceEnabled(true)
            .isSalaryClient(true)
            .paymentSchedule(scheduleElementList)
            .build();

    public static final Credit credit = Credit.builder()
            .amount(BigDecimal.valueOf(100000))
            .term(6)
            .monthlyPayment(BigDecimal.valueOf(17403.38))
            .rate(BigDecimal.valueOf(15))
            .psk(BigDecimal.valueOf(21.709))
            .isInsuranceEnabled(true)
            .isSalaryClient(true)
            .paymentSchedule(scheduleElementList)
            .build();

    public static final ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
            .amount(BigDecimal.valueOf(100000))
            .term(6)
            .firstName("Kseniya")
            .lastName("Gurova")
            .gender(Gender.FEMALE)
            .birthdate(LocalDate.of(1985, 3, 20))
            .passportSeries("1234")
            .passportNumber("567890")
            .passportIssueDate(LocalDate.of(2000, 12, 13))
            .passportIssueBranch("blablabla")
            .maritalStatus(MaritalStatus.MARRIED)
            .dependentAmount(2)
            .employment(finishRegistrationRequestDTO.getEmployment())
            .account("123456789")
            .isInsuranceEnabled(true)
            .isSalaryClient(true)
            .build();

    private static final Client fullClient = Client.builder()
            .lastName("Gurova")
            .firstName("Kseniya")
            .birthdate(LocalDate.of(1985, 3, 20))
            .email("blablabla@mail.ru")
            .gender("female")
            .maritalStatus("married")
            .dependentAmount(2)
            .passport(Passport.builder()
                    .series("1234")
                    .number("567890")
                    .issueDate(LocalDate.of(2000, 12, 13))
                    .issueBranch("blablabla")
                    .build())
            .employment(employment)
            .account("123456789")
            .build();

    private static final List<ApplicationStatusHistoryDTO> applicationStatusHistoryDTOList = new ArrayList<>(
            List.of(ApplicationStatusHistoryDTO.builder()
                            .time(LocalDateTime.now())
                            .status(ApplicationStatus.APPROVED)
                            .changeType(ChangeType.AUTOMATIC)
                            .build(),
                    ApplicationStatusHistoryDTO.builder()
                            .time(LocalDateTime.now().plusMinutes(5))
                            .status(ApplicationStatus.CC_APPROVED)
                            .changeType(ChangeType.AUTOMATIC)
                            .build())

    );

    public static final Application applicationForSavedInCalculateCredit = Application.builder()
            .client(fullClient)
            .credit(credit)
            .status(ApplicationStatus.CC_APPROVED)
            .creationDate(LocalDate.now())
            .appliedOffer(loanOfferDTOTrueTrue)
            .statusHistory(applicationStatusHistoryDTOList)
            .build();

    public static final Long applicationId = 1L;

    public static Application getApplicationForLoanApplicationRequestDTOCorrect(){
        return applicationForLoanApplicationRequestDTOCorrect;
    }


}
