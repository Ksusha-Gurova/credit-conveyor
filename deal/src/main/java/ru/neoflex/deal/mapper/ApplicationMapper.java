package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.model.ApplicationDTO;
import org.openapitools.model.LoanApplicationRequestDTO;
import ru.neoflex.deal.model.Application;
import ru.neoflex.deal.model.Client;

import java.time.LocalDate;

@Mapper(componentModel = "spring", imports = {LocalDate.class})
public interface ApplicationMapper {
    @Mapping(target = "creationDate", expression = "java(LocalDate.now())")
    @Mapping(source = "client", target = "client")
    Application mapDtoToEntity(LoanApplicationRequestDTO loanApplicationRequestDTO, Client client);

    @Mapping(source = "client.firstName", target = "firstName")
    @Mapping(source = "client.lastName", target = "lastName")
    @Mapping(source = "client.middleName", target = "middleName")
    @Mapping(source = "client.email", target = "email")
    @Mapping(source = "client.birthdate", target = "birthdate")
    @Mapping(source = "client.passport.series", target = "passportSeries")
    @Mapping(source = "client.passport.number", target = "passportNumber")
    @Mapping(source = "client.passport.issueDate", target = "passportIssueDate")
    @Mapping(source = "client.passport.issueBranch", target = "passportIssueBranch")
    @Mapping(source = "client.gender", target = "gender")
    @Mapping(source = "client.maritalStatus", target = "maritalStatus")
    @Mapping(source = "client.dependentAmount", target = "dependentAmount")
    @Mapping(source = "client.employment.employmentStatus", target = "employmentStatus")
    @Mapping(source = "client.employment.employerINN", target = "employerINN")
    @Mapping(source = "client.employment.salary", target = "salary")
    @Mapping(source = "client.employment.position", target = "position")
    @Mapping(source = "client.employment.workExperienceTotal", target = "workExperienceTotal")
    @Mapping(source = "client.employment.workExperienceCurrent", target = "workExperienceCurrent")
    @Mapping(source = "client.account", target = "account")
    @Mapping(source = "credit.amount", target = "amount")
    @Mapping(source = "credit.term", target = "term")
    @Mapping(source = "credit.monthlyPayment", target = "monthlyPayment")
    @Mapping(source = "credit.rate", target = "rate")
    @Mapping(source = "credit.psk", target = "psk")
    @Mapping(source = "credit.isInsuranceEnabled", target = "isInsuranceEnabled")
    @Mapping(source = "credit.isSalaryClient", target = "isSalaryClient")
    @Mapping(source = "credit.paymentSchedule", target = "paymentSchedule")
    ApplicationDTO mapEntityToDto(Application application);
}
