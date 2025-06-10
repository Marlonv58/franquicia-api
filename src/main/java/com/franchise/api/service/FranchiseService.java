package com.franchise.api.service;

import com.franchise.api.dto.franchise.FranchiseCreateDto;
import com.franchise.api.dto.franchise.FranchiseUpdateDto;
import com.franchise.api.dto.franchise.FranchiseResponseDto;
import com.franchise.api.entities.Franchise;
import com.franchise.api.repositories.FranchiseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FranchiseService {
    private final FranchiseRepository franchiseRepository;

    public FranchiseService(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    public Mono<Franchise> createFranchise(FranchiseCreateDto dto) {
        Franchise franchise = Franchise.builder()
                .name(dto.getName())
                .build();
        return franchiseRepository.save(franchise);
    }

    public Mono<FranchiseResponseDto> updateFranchise(Long id, FranchiseUpdateDto dto) {
        return franchiseRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Franquicia no encontrada")))
                .flatMap(franchise -> {
                    franchise.setName(dto.getName());
                    return franchiseRepository.save(franchise);
                })
                .map(updated -> FranchiseResponseDto.builder()
                        .id(updated.getId())
                        .name(updated.getName())
                        .build());
    }
}
