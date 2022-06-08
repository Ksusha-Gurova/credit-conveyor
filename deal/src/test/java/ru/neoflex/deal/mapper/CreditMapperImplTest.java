package ru.neoflex.deal.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.deal.DTOForTests;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CreditMapperImplTest {
    @InjectMocks
    private CreditMapperImpl mapper;

    @Test
    void mapDtoToEntity() {
        assertThat(mapper.mapDtoToEntity(DTOForTests.creditDTO))
                .isNotNull()
                .isEqualTo(DTOForTests.credit);
    }
}