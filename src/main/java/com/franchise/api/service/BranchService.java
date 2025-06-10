package com.franchise.api.service;

import com.franchise.api.dto.branch.BranchDto;
import com.franchise.api.dto.branch.BranchResponseDto;
import com.franchise.api.dto.branch.BranchUpdateDto;
import com.franchise.api.entities.Branch;
import com.franchise.api.entities.Franchise;
import com.franchise.api.repositories.BranchRepository;
import com.franchise.api.repositories.FranchiseRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BranchService {
    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    public BranchService(BranchRepository branchRepository, FranchiseRepository franchiseRepository) {
        this.branchRepository = branchRepository;
        this.franchiseRepository = franchiseRepository;
    }

    public Mono<BranchResponseDto> addBranch(BranchDto dto) {
        return franchiseRepository.findById(dto.getFranchiseId())
                .switchIfEmpty(Mono.error(new RuntimeException("Franquicia no encontrada")))
                .flatMap(franchise -> {
                    Branch branch = Branch.builder()
                            .name(dto.getName())
                            .franchiseId(franchise.getId())
                            .build();

                    return branchRepository.save(branch);
                })
                .map(savedBranch -> BranchResponseDto.builder()
                        .id(savedBranch.getId())
                        .name(savedBranch.getName())
                        .franchiseId(savedBranch.getFranchiseId())
                        .build()
                );
    }

    public Mono<BranchResponseDto> updateBranchName(Long id, BranchUpdateDto dto) {
        return branchRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Sucursal no encontrada")))
                .flatMap(branch -> {
                    branch.setName(dto.getName());
                    return branchRepository.save(branch);
                })
                .map(updated -> BranchResponseDto.builder()
                        .id(updated.getId())
                        .name(updated.getName())
                        .franchiseId(updated.getFranchiseId())
                        .build()
                );
    }
}
