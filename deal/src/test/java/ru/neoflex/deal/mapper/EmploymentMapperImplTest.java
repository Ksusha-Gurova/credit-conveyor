package ru.neoflex.deal.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.EmploymentDTO;
import org.openapitools.model.EmploymentStatus;
import org.openapitools.model.Position;
import ru.neoflex.deal.model.Employment;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class EmploymentMapperImplTest {
    @InjectMocks
    private EmploymentMapperImpl mapper;
    private EmploymentDTO employmentDTO;
    private Employment employment;

    @BeforeEach
    void setUp() {
    employmentDTO = EmploymentDTO.builder()
            .employmentStatus(EmploymentStatus.EMPLOYED)
            .employerINN("12345678900")
            .salary(BigDecimal.valueOf(10000))
            .position(Position.MID_MANAGER)
            .workExperienceTotal(22)
            .workExperienceCurrent(7)
            .build();
    employment = Employment.builder()
            .employmentStatus("EMPLOYED")
            .salary(BigDecimal.valueOf(10000))
            .position("MID_MANAGER")
            .employerINN("12345678900")
            .workExperienceTotal(22)
            .workExperienceCurrent(7)
            .build();
    }

    @Test
    void mapDtoToEntity() {
        assertThat(mapper.mapDtoToEntity(employmentDTO))
                .isNotNull()
                .isEqualTo(employment);
    }
}