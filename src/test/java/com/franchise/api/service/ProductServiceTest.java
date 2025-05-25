package com.franchise.api.service;

import com.franchise.api.dto.product.ProductDto;
import com.franchise.api.dto.product.ProductResponseDto;
import com.franchise.api.dto.product.StockUpdateDto;
import com.franchise.api.entities.Branch;
import com.franchise.api.entities.Product;
import com.franchise.api.repositories.BranchRepository;
import com.franchise.api.repositories.ProductRepository;
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
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void updateStock_shouldUpdateAndReturnProduct() {
        // Arrange
        Long productId = 1L;
        Integer newStock = 50;

        Product existingProduct = Product.builder()
                .id(productId)
                .name("Café")
                .stock(20)
                .build();

        Product updatedProduct = Product.builder()
                .id(productId)
                .name("Café")
                .stock(newStock)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // Act
        StockUpdateDto dto = new StockUpdateDto(productId, newStock);
        ProductResponseDto result = productService.updateStock(dto);

        // Assert
        assertNotNull(result);
        assertEquals(newStock, result.getStock());
        assertEquals("Café", result.getName());

        verify(productRepository).findById(productId);
        verify(productRepository).save(existingProduct);
    }

    @Test
    void updateStock_shouldThrowExceptionIfProductNotFound() {
        // Arrange
        Long productId = 99L;
        Integer newStock = 100;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.updateStock(new StockUpdateDto(productId, newStock));
        });

        assertEquals("Producto no encontrado", exception.getMessage());
        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(any());
    }

    @Test
    void addProductToBranch_shouldAddProductCorrectly() {
        Long branchId = 1L;
        Branch branch = Branch.builder().id(branchId).name("Sucursal 1").build();

        ProductDto dto = ProductDto.builder()
                .branchId(branchId)
                .name("Producto A")
                .stock(20)
                .build();

        Product savedProduct = Product.builder()
                .id(1L)
                .name("Producto A")
                .stock(20)
                .branch(branch)
                .build();

        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductResponseDto result = productService.addProductToBranch(dto);

        assertNotNull(result);
        assertEquals("Producto A", result.getName());
        assertEquals(20, result.getStock());
        assertEquals(branch.getId(), result.getBranchId());

        verify(branchRepository).findById(branchId);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void addProductToBranch_shouldThrowIfBranchNotFound() {
        ProductDto dto = ProductDto.builder()
                .branchId(99L)
                .name("Producto X")
                .stock(5)
                .build();

        when(branchRepository.findById(dto.getBranchId())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            productService.addProductToBranch(dto);
        });

        assertEquals("Sucursal no encontrada", ex.getMessage());
        verify(productRepository, never()).save(any());
    }
}
