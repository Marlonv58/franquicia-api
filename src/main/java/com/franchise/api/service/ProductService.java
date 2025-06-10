package com.franchise.api.service;

import com.franchise.api.dto.product.*;
import com.franchise.api.entities.Branch;
import com.franchise.api.entities.Product;
import com.franchise.api.repositories.BranchRepository;
import com.franchise.api.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Mono<ProductResponseDto> addProductToBranch(ProductDto dto) {
        return branchRepository.findById(dto.getBranchId())
                .switchIfEmpty(Mono.error(new RuntimeException("Sucursal no encontrada")))
                .flatMap(branch -> {
                    Product product = new Product(null, dto.getName(), dto.getStock(), dto.getBranchId());
                    return productRepository.save(product);
                })
                .map(saved -> ProductResponseDto.builder()
                        .id(saved.getId())
                        .name(saved.getName())
                        .stock(saved.getStock())
                        .branchId(saved.getBranchId())
                        .build());
    }

    public Mono<ProductResponseDto> updateStock(StockUpdateDto dto) {
        return productRepository.findById(dto.getProductId())
                .switchIfEmpty(Mono.error(new RuntimeException("Producto no encontrado")))
                .flatMap(product -> {
                    product.setStock(dto.getNewStock());
                    return productRepository.save(product);
                })
                .map(saved -> ProductResponseDto.builder()
                        .id(saved.getId())
                        .name(saved.getName())
                        .stock(saved.getStock())
                        .branchId(saved.getBranchId())
                        .build());
    }

    public Mono<Boolean> deleteProduct(Long productId) {
        return productRepository.findById(productId)
                .flatMap(p -> productRepository.deleteById(productId).thenReturn(true))
                .defaultIfEmpty(false);
    }

    public Flux<MaxStockProductDto> getMaxStockProductsByFranchise(Long franchiseId) {
        return branchRepository.findByFranchiseId(franchiseId)
                .flatMap(branch ->
                        productRepository.findTopByBranchIdOrderByStockDesc(branch.getId())
                                .map(product -> new MaxStockProductDto(
                                        product.getId(),
                                        product.getName(),
                                        product.getStock(),
                                        branch.getId(),
                                        branch.getName()
                                )))
                .switchIfEmpty(Flux.empty());
    }

    public Mono<ProductResponseDto> updateProductName(ProductUpdateNameDto dto) {
        return productRepository.findById(dto.getId())
                .switchIfEmpty(Mono.error(new RuntimeException("Producto no encontrado")))
                .flatMap(product -> {
                    product.setName(dto.getName());
                    return productRepository.save(product);
                })
                .map(saved -> ProductResponseDto.builder()
                        .id(saved.getId())
                        .name(saved.getName())
                        .stock(saved.getStock())
                        .branchId(saved.getBranchId())
                        .build());
    }
}
