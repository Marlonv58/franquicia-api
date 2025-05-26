package com.franchise.api.service;

import com.franchise.api.dto.product.*;
import com.franchise.api.entities.Branch;
import com.franchise.api.entities.Product;
import com.franchise.api.repositories.BranchRepository;
import com.franchise.api.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;

    public ProductService(ProductRepository productRepository, BranchRepository branchRepository) {
        this.productRepository = productRepository;
        this.branchRepository = branchRepository;
    }

    public ProductResponseDto addProductToBranch(ProductDto dto) {
        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        Product product = Product.builder()
                .name(dto.getName())
                .stock(dto.getStock())
                .branch(branch)
                .build();

        Product saved = productRepository.save(product);

        return ProductResponseDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .stock(saved.getStock())
                .branchId(saved.getBranch().getId())
                .build();
    }

    public ProductResponseDto updateStock(StockUpdateDto dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        product.setStock(dto.getNewStock());
        Product saved = productRepository.save(product);

        return ProductResponseDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .stock(saved.getStock())
                .branchId(saved.getBranch() != null ? saved.getBranch().getId() :  null)
                .build();
    }

    public boolean deleteProduct(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            productRepository.deleteById(productId);
            return true;
        } else {
            return false;
        }
    }

    public List<MaxStockProductDto> getMaxStockProductsByFranchise(Long franchiseId) {
        List<Branch> branches = branchRepository.findByFranchiseId(franchiseId);

        return branches.stream()
                .map(branch -> {
                    Optional<Product> topProduct = productRepository.findTopByBranchIdOrderByStockDesc(branch.getId());
                    return topProduct.map(product -> new MaxStockProductDto(
                            product.getId(),
                            product.getName(),
                            product.getStock(),
                            branch.getId(),
                            branch.getName()
                    ));
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public ProductResponseDto updateProductName(ProductUpdateNameDto dto) {
        Product product = productRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        product.setName(dto.getName());
        Product saved = productRepository.save(product);

        return ProductResponseDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .stock(saved.getStock())
                .branchId(saved.getBranch() != null ? saved.getBranch().getId() :  null)
                .build();
    }
}
