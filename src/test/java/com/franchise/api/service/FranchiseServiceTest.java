package com.franchise.api.service;

import com.franchise.api.dto.franchise.FranchiseCreateDto;
import com.franchise.api.dto.franchise.FranchiseUpdateDto;
import com.franchise.api.entities.Franchise;
import com.franchise.api.repositories.FranchiseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class FranchiseServiceTest {
    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private FranchiseService franchiseService;

    @Test
    void createFranchise_shouldReturnCreatedFranchise() {
        // Arrange
        FranchiseCreateDto dto = FranchiseCreateDto.builder().name("Franquicia Test").build();
        Franchise expected = Franchise.builder().id(1L).name("Franquicia Test").build();

        Mockito.when(franchiseRepository.save(Mockito.any(Franchise.class)))
                .thenReturn(expected);

        // Act
        Franchise result = franchiseService.createFranchise(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Franquicia Test", result.getName());
        assertEquals(1L, result.getId());
    }
}
