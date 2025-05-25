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
        // Arrange
        Long franchiseId = 1L;
        Franchise franchise = Franchise.builder().id(franchiseId).name("Franquicia A").build();

        BranchDto dto = BranchDto.builder()
                .name("Sucursal 1")
                .franchiseId(franchiseId)
                .build();

        Branch savedBranch = Branch.builder()
                .id(1L)
                .name("Sucursal 1")
                .franchise(franchise)
                .build();

        when(franchiseRepository.findById(franchiseId)).thenReturn(Optional.of(franchise));
        when(branchRepository.save(any(Branch.class))).thenReturn(savedBranch);

        // Act
        BranchResponseDto result = branchService.addBranch(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Sucursal 1", result.getName());
        assertEquals(franchise, "");

        verify(franchiseRepository).findById(franchiseId);
        verify(branchRepository).save(any(Branch.class));
    }

    @Test
    void addBranch_shouldThrowIfFranchiseNotFound() {
        Long franchiseId = 99L;
        BranchDto dto = BranchDto.builder().name("Sucursal X").franchiseId(franchiseId).build();

        when(franchiseRepository.findById(franchiseId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            branchService.addBranch(dto);
        });

        assertEquals("Franquicia no encontrada", ex.getMessage());
        verify(branchRepository, never()).save(any());
    }

    @Test
    void updateBranchName_shouldUpdateName() {
        Long branchId = 1L;
        String newName = "Sucursal Modificada";

        Branch existingBranch = Branch.builder()
                .id(branchId)
                .name("Sucursal Original")
                .build();

        Branch updatedBranch = Branch.builder()
                .id(branchId)
                .name(newName)
                .build();

        when(branchRepository.findById(branchId)).thenReturn(Optional.of(existingBranch));
        when(branchRepository.save(any(Branch.class))).thenReturn(updatedBranch);

        BranchUpdateDto dto = BranchUpdateDto.builder()
                .name("Sucursal Modificada")
                .build();
        BranchResponseDto result = branchService.updateBranchName(branchId, dto);


        assertNotNull(result);
        assertEquals(newName, result.getName());

        verify(branchRepository).findById(branchId);
        verify(branchRepository).save(existingBranch);
    }

    @Test
    void updateBranchName_shouldThrowIfBranchNotFound() {
        Long branchId = 999L;
        when(branchRepository.findById(branchId)).thenReturn(Optional.empty());

        BranchUpdateDto dto = BranchUpdateDto.builder()
                .name("Nuevo Nombre")
                .build();

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            branchService.updateBranchName(branchId, dto);
        });

        assertEquals("Sucursal no encontrada", ex.getMessage());
        verify(branchRepository, never()).save(any());
    }
}
