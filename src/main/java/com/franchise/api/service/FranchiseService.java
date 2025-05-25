package com.franchise.api.service;

import com.franchise.api.dto.franchise.FranchiseDto;
import com.franchise.api.dto.product.MaxStockProductDto;
import com.franchise.api.entities.Branch;
import com.franchise.api.entities.Franchise;
import com.franchise.api.entities.Product;
import com.franchise.api.repositories.FranchiseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class FranchiseService {
    @Autowired
    private FranchiseRepository franchiseRepository;

    public Franchise createFranchise(FranchiseDto franchiseDto) {
        Franchise franchise = Franchise.builder()
                .name(franchiseDto.getName())
                .build();
        return franchiseRepository.save(franchise);
    }

    public Franchise getFranchiseById(Long id) {
        return franchiseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Franchise not found with id: " + id));
    }
    public Franchise updateFranchise(Long id, FranchiseDto franchiseDto) {
        Franchise franchise = getFranchiseById(id);
        franchise.setName(franchiseDto.getName());
        return franchiseRepository.save(franchise);
    }

    public List<MaxStockProductDto> getMaxStockProductsByFranchise(Long franchiseId) {
        Franchise franchise = franchiseRepository.findById(franchiseId)
                .orElseThrow(() -> new RuntimeException("Franquicia no encontrada"));

        List<MaxStockProductDto> result = new ArrayList<>();

        for (Branch branch : franchise.getBranches()) {
            Optional<Product> maxStockProduct = branch.getProducts().stream()
                    .max(Comparator.comparingInt(Product::getStock));

            maxStockProduct.ifPresent(product -> result.add(
                    MaxStockProductDto.builder()
                            .productName(product.getName())
                            .stock(product.getStock())
                            .branchName(branch.getName())
                            .build()
            ));
        }

        return result;
    }
}
