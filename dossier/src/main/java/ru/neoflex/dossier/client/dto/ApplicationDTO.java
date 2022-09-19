package ru.neoflex.dossier.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@ToString
@Builder
@AllArgsConstructor
@Getter
public class ApplicationDTO {
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private LocalDate birthdate;
    private String passportSeries;
    private String passportNumber;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private String gender;
    private String maritalStatus;
    private Integer dependentAmount;
    private String employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private String position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
    private String account;
    private BigDecimal amount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private BigDecimal psk;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
    private List<PaymentScheduleElement> paymentSchedule;
    private String status;
    private LocalDate creationDate;
    private LoanOfferDTO appliedOffer;
    private LocalDate signDate;
    private String sesCode;
    private List<ApplicationStatusHistoryDTO> statusHistory;
}
