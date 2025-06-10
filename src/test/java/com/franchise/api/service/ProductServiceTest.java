package com.franchise.api.service;

import com.franchise.api.dto.product.ProductDto;
import com.franchise.api.dto.product.ProductResponseDto;
import com.franchise.api.dto.product.ProductUpdateNameDto;
import com.franchise.api.dto.product.StockUpdateDto;
import com.franchise.api.entities.Branch;
import com.franchise.api.entities.Product;
import com.franchise.api.repositories.BranchRepository;
import com.franchise.api.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void updateStock_shouldUpdateAndReturnProduct() {
        Long productId = 1L;
        Integer newStock = 50;

        Product existingProduct = Product.builder()
                .id(productId)
                .name("Café")
                .stock(20)
                .branchId(1L)
                .build();

        Product updatedProduct = Product.builder()
                .id(productId)
                .name("Café")
                .stock(newStock)
                .branchId(1L)
                .build();

        when(productRepository.findById(productId)).thenReturn(Mono.just(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(updatedProduct));

        StockUpdateDto dto = new StockUpdateDto(productId, newStock);

        StepVerifier.create(productService.updateStock(dto))
                .expectNextMatches(result ->
                        result.getStock().equals(newStock) &&
                                result.getName().equals("Café") &&
                                result.getBranchId().equals(1L))
                .verifyComplete();
    }

    @Test
    void updateStock_shouldThrowIfProductNotFound() {
        Long productId = 99L;
        Integer newStock = 100;

        when(productRepository.findById(productId)).thenReturn(Mono.empty());

        StepVerifier.create(productService.updateStock(new StockUpdateDto(productId, newStock)))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Producto no encontrado"))
                .verify();
    }

    @Test
    void addProductToBranch_shouldAddProductCorrectly() {
        Long branchId = 1L;

        Branch branch = Branch.builder()
                .id(branchId)
                .name("Sucursal 1")
                .build();

        ProductDto dto = ProductDto.builder()
                .branchId(branchId)
                .name("Producto A")
                .stock(20)
                .build();

        Product savedProduct = Product.builder()
                .id(1L)
                .name("Producto A")
                .stock(20)
                .branchId(branchId)
                .build();

        when(branchRepository.findById(branchId)).thenReturn(Mono.just(branch));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(savedProduct));

        StepVerifier.create(productService.addProductToBranch(dto))
                .expectNextMatches(result ->
                        result.getId().equals(1L) &&
                                result.getName().equals("Producto A") &&
                                result.getStock().equals(20) &&
                                result.getBranchId().equals(branchId))
                .verifyComplete();
    }

    @Test
    void addProductToBranch_shouldThrowIfBranchNotFound() {
        ProductDto dto = ProductDto.builder()
                .branchId(99L)
                .name("Producto X")
                .stock(5)
                .build();

        when(branchRepository.findById(dto.getBranchId())).thenReturn(Mono.empty());

        StepVerifier.create(productService.addProductToBranch(dto))
                .expectErrorMatches(error -> error instanceof RuntimeException &&
                        error.getMessage().equals("Sucursal no encontrada"))
                .verify();
    }

    @Test
    void updateProductName_shouldUpdateCorrectly() {
        Long productId = 1L;
        String newName = "Producto Editado";

        Product existingProduct = Product.builder()
                .id(productId)
                .name("Producto A")
                .stock(10)
                .branchId(1L)
                .build();

        Product updatedProduct = Product.builder()
                .id(productId)
                .name(newName)
                .stock(10)
                .branchId(1L)
                .build();

        when(productRepository.findById(productId)).thenReturn(Mono.just(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(updatedProduct));

        ProductUpdateNameDto dto = ProductUpdateNameDto.builder()
                .id(productId)
                .name(newName)
                .build();

        StepVerifier.create(productService.updateProductName(dto))
                .expectNextMatches(result ->
                        result.getId().equals(productId) &&
                                result.getName().equals(newName) &&
                                result.getBranchId().equals(1L))
                .verifyComplete();
    }
}
