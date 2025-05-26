package com.franchise.api.service;

import com.franchise.api.dto.branch.BranchDto;
import com.franchise.api.dto.branch.BranchResponseDto;
import com.franchise.api.dto.branch.BranchUpdateDto;
import com.franchise.api.entities.Branch;
import com.franchise.api.entities.Franchise;
import com.franchise.api.repositories.BranchRepository;
import com.franchise.api.repositories.FranchiseRepository;
import org.springframework.stereotype.Service;

@Service
public class BranchService {
    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    public BranchService(BranchRepository branchRepository, FranchiseRepository franchiseRepository) {
        this.branchRepository = branchRepository;
        this.franchiseRepository = franchiseRepository;
    }

    public BranchResponseDto addBranch(BranchDto dto) {
        Franchise franchise = franchiseRepository.findById(dto.getFranchiseId())
                .orElseThrow(() -> new RuntimeException("Franquicia no encontrada"));

        Branch branch = Branch.builder()
                .name(dto.getName())
                .franchise(franchise)
                .build();

        Branch savedBranch = branchRepository.save(branch);

        return BranchResponseDto.builder()
                .id(savedBranch.getId())
                .name(savedBranch.getName())
                .franchiseId(savedBranch.getFranchise().getId())
                .build();
    }

    public BranchResponseDto updateBranchName(Long id, BranchUpdateDto dto) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        branch.setName(dto.getName());
        Branch updated = branchRepository.save(branch);

        return BranchResponseDto.builder()
                .id(updated.getId())
                .name(updated.getName())
                .franchiseId(updated.getFranchise() != null ? updated.getFranchise().getId() : null)
                .build();
    }
}
