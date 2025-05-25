package com.franchise.api.service;

import com.franchise.api.dto.BranchDto;
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

    public Branch addBranch(BranchDto dto) {
        Franchise franchise = franchiseRepository.findById(dto.getFranchiseId())
                .orElseThrow(() -> new RuntimeException("Franquicia no encontrada"));

        Branch branch = Branch.builder()
                .name(dto.getName())
                .franchise(franchise)
                .build();

        return branchRepository.save(branch);
    }

    public Branch updateBranchName(Long branchId, String newName) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        branch.setName(newName);
        return branchRepository.save(branch);
    }
}
