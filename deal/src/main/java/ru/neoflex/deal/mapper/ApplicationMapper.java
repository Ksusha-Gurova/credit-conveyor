package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.model.LoanApplicationRequestDTO;
import ru.neoflex.deal.model.Application;
import ru.neoflex.deal.model.Client;

import java.time.LocalDate;

@Mapper(componentModel = "spring", imports = {LocalDate.class})
public interface ApplicationMapper {
    @Mapping(target = "creationDate", expression = "java(LocalDate.now())")
    @Mapping(source = "client", target = "client")
    Application mapDtoToEntity(LoanApplicationRequestDTO loanApplicationRequestDTO, Client client);
}
