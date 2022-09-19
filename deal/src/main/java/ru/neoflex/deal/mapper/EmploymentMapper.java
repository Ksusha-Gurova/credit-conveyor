package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.openapitools.model.EmploymentDTO;
import ru.neoflex.deal.model.Employment;

@Mapper(componentModel = "spring")
public interface EmploymentMapper {
    Employment mapDtoToEntity(EmploymentDTO employmentDTO);
}
