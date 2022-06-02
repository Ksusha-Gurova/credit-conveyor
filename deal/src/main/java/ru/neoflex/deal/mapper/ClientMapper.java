package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.model.LoanApplicationRequestDTO;
import ru.neoflex.deal.model.Client;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    @Mapping(source = "passportSeries", target = "passport.series")
    @Mapping(source = "passportNumber", target = "passport.number")
    Client mapDtoToEntity(LoanApplicationRequestDTO loanApplicationRequestDTO);


}
