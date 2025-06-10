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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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
        FranchiseCreateDto dto = FranchiseCreateDto.builder()
                .name("Franquicia Test")
                .build();

        Franchise expected = Franchise.builder()
                .id(1L)
                .name("Franquicia Test")
                .build();

        Mockito.when(franchiseRepository.save(Mockito.any(Franchise.class)))
                .thenReturn(Mono.just(expected));

        StepVerifier.create(franchiseService.createFranchise(dto))
                .assertNext(result -> {
                    assertNotNull(result);
                    assertEquals("Franquicia Test", result.getName());
                    assertEquals(1L, result.getId());
                })
                .verifyComplete();
    }

    @Test
    void updateFranchise_shouldUpdateAndReturnFranchise() {
        Long franchiseId = 1L;
        FranchiseUpdateDto dto = FranchiseUpdateDto.builder()
                .name("Franquicia Modificada")
                .build();

        Franchise existing = Franchise.builder()
                .id(franchiseId)
                .name("Franquicia Original")
                .build();

        Franchise updated = Franchise.builder()
                .id(franchiseId)
                .name(dto.getName())
                .build();

        Mockito.when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(existing));
        Mockito.when(franchiseRepository.save(existing)).thenReturn(Mono.just(updated));

        StepVerifier.create(franchiseService.updateFranchise(franchiseId, dto))
                .assertNext(response -> {
                    assertEquals(franchiseId, response.getId());
                    assertEquals("Franquicia Modificada", response.getName());
                })
                .verifyComplete();
    }

    @Test
    void updateFranchise_shouldThrowIfNotFound() {
        Long franchiseId = 99L;
        FranchiseUpdateDto dto = FranchiseUpdateDto.builder()
                .name("No importa")
                .build();

        Mockito.when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.empty());

        StepVerifier.create(franchiseService.updateFranchise(franchiseId, dto))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Franquicia no encontrada"))
                .verify();
    }
}
