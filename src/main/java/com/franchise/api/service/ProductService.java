package com.franchise.api.service;

import com.franchise.api.dto.ProductDto;
import com.franchise.api.entities.Branch;
import com.franchise.api.entities.Product;
import com.franchise.api.repositories.BranchRepository;
import com.franchise.api.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;

    public ProductService(ProductRepository productRepository, BranchRepository branchRepository) {
        this.productRepository = productRepository;
        this.branchRepository = branchRepository;
    }

    public Product addProductToBranch(ProductDto dto) {
        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        Product product = Product.builder()
                .name(dto.getName())
                .stock(dto.getStock())
                .branch(branch)
                .build();

        return productRepository.save(product);
    }

    public Product updateStock(Long productId, Integer newStock) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        product.setStock(newStock);
        return productRepository.save(product);
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
}
