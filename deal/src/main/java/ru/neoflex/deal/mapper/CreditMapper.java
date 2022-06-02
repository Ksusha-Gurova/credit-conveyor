package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.openapitools.model.CreditDTO;
import ru.neoflex.deal.model.Credit;

@Mapper(componentModel = "spring")
public interface CreditMapper {
    Credit mapDtoToEntity(CreditDTO creditDTO);
}
