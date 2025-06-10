package com.franchise.api.service;

import com.franchise.api.dto.branch.BranchDto;
import com.franchise.api.dto.branch.BranchResponseDto;
import com.franchise.api.dto.branch.BranchUpdateDto;
import com.franchise.api.entities.Branch;
import com.franchise.api.entities.Franchise;
import com.franchise.api.repositories.BranchRepository;
import com.franchise.api.repositories.FranchiseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BranchServiceTest {
    @Mock
    private BranchRepository branchRepository;

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private BranchService branchService;

    @Test
    void addBranch_shouldCreateBranchForFranchise() {
        Long franchiseId = 1L;
        Franchise franchise = Franchise.builder().id(franchiseId).name("Franquicia A").build();

        BranchDto dto = BranchDto.builder()
                .name("Sucursal 1")
                .franchiseId(franchiseId)
                .build();

        Branch savedBranch = Branch.builder()
                .id(1L)
                .name("Sucursal 1")
                .franchiseId(franchise.getId())
                .build();

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(branchRepository.save(any(Branch.class))).thenReturn(Mono.just(savedBranch));

        StepVerifier.create(branchService.addBranch(dto))
                .expectNextMatches(result ->
                        result.getName().equals("Sucursal 1") &&
                                result.getFranchiseId().equals(franchiseId))
                .verifyComplete();

        verify(franchiseRepository).findById(franchiseId);
        verify(branchRepository).save(any(Branch.class));
    }

    @Test
    void addBranch_shouldThrowIfFranchiseNotFound() {
        Long franchiseId = 99L;
        BranchDto dto = BranchDto.builder().name("Sucursal X").franchiseId(franchiseId).build();

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.empty());

        StepVerifier.create(branchService.addBranch(dto))
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Franquicia no encontrada"))
                .verify();

        verify(branchRepository, never()).save(any());
    }

    @Test
    void updateBranchName_shouldUpdateName() {
        Long branchId = 1L;
        String newName = "Sucursal Modificada";

        Franchise franchise = Franchise.builder().id(1L).name("Franquicia X").build();
        Branch existingBranch = Branch.builder()
                .id(branchId)
                .name("Sucursal Original")
                .franchiseId(franchise.getId())
                .build();

        Branch updatedBranch = Branch.builder()
                .id(branchId)
                .name(newName)
                .franchiseId(franchise.getId())
                .build();

        when(branchRepository.findById(branchId)).thenReturn(Mono.just(existingBranch));
        when(branchRepository.save(any(Branch.class))).thenReturn(Mono.just(updatedBranch));

        BranchUpdateDto dto = BranchUpdateDto.builder().name(newName).build();

        StepVerifier.create(branchService.updateBranchName(branchId, dto))
                .expectNextMatches(result ->
                        result.getName().equals(newName) &&
                                result.getFranchiseId().equals(franchise.getId()))
                .verifyComplete();

        verify(branchRepository).findById(branchId);
        verify(branchRepository).save(existingBranch);
    }

    @Test
    void updateBranchName_shouldThrowIfBranchNotFound() {
        Long branchId = 999L;
        when(branchRepository.findById(branchId)).thenReturn(Mono.empty());

        BranchUpdateDto dto = BranchUpdateDto.builder().name("Nuevo Nombre").build();

        StepVerifier.create(branchService.updateBranchName(branchId, dto))
                .expectErrorMatches(ex ->
                        ex instanceof RuntimeException &&
                                ex.getMessage().equals("Sucursal no encontrada"))
                .verify();

        verify(branchRepository, never()).save(any());
    }
}
