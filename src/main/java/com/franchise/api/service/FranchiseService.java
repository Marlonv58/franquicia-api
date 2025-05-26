package com.franchise.api.service;

import com.franchise.api.dto.franchise.FranchiseCreateDto;
import com.franchise.api.dto.franchise.FranchiseUpdateDto;
import com.franchise.api.dto.franchise.FranchiseResponseDto;
import com.franchise.api.entities.Franchise;
import com.franchise.api.repositories.FranchiseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FranchiseService {
    @Autowired
    private FranchiseRepository franchiseRepository;

    public Franchise createFranchise(FranchiseCreateDto franchiseDto) {
        Franchise franchise = Franchise.builder()
                .name(franchiseDto.getName())
                .build();
        return franchiseRepository.save(franchise);
    }

    public FranchiseResponseDto updateFranchise(Long id, FranchiseUpdateDto dto) {
        Franchise franchise = franchiseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Franquicia no encontrada"));

        franchise.setName(dto.getName());
        Franchise updated = franchiseRepository.save(franchise);

        return FranchiseResponseDto.builder()
                .id(updated.getId())
                .name(updated.getName())
                .build();
    }
}
