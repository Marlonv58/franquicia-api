package com.franchise.api.controller;
import com.franchise.api.constant.HttpConstant;
import com.franchise.api.dto.*;
import com.franchise.api.dto.product.*;
import com.franchise.api.service.FranchiseService;
import com.franchise.api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(HttpConstant.PATH_PRODUCT)
public class ProductController {

    private final ProductService productService;
    private final FranchiseService franchiseService;

    public ProductController(ProductService productService, FranchiseService franchiseService) {
        this.productService = productService;
        this.franchiseService = franchiseService;
    }

    @PostMapping("/add")
    public Mono<ResponseEntity<ResponseDto>> addProductToBranch(@Valid @RequestBody ProductDto productDto) {
        return Mono.fromCallable(() -> {
            ProductResponseDto saved = productService.addProductToBranch(productDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDto(true, "Producto agregado exitosamente", saved));
        });
    }

    @PatchMapping("/update-stock")
    public Mono<ResponseEntity<ResponseDto>> updateStock(@Valid @RequestBody StockUpdateDto dto) {
        return Mono.fromCallable(() -> {
            productService.updateStock(dto);
            return ResponseEntity.ok(new ResponseDto(true, "Stock actualizado correctamente", null));
        });
    }

    @DeleteMapping("/{productId}")
    public Mono<ResponseEntity<ResponseDto>> deleteProduct(@PathVariable Long productId) {
        return Mono.fromCallable(() -> {
            boolean deleted = productService.deleteProduct(productId);

            if (deleted) {
                return ResponseEntity.ok(new ResponseDto(true, "Producto eliminado correctamente", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDto(false, "Producto no encontrado", null));
            }
        });
    }

    @GetMapping("/{franchiseId}/max-stock-products")
    public Mono<ResponseEntity<ResponseDto>> getMaxStockProducts(@PathVariable Long franchiseId) {
        return Mono.fromCallable(() -> {
            List<MaxStockProductDto> result = productService.getMaxStockProductsByFranchise(franchiseId);
            return ResponseEntity.ok(new ResponseDto(true, "Productos con mayor stock por sucursal", result));
        });
    }

    @PatchMapping("/update-name")
    public Mono<ResponseEntity<ResponseDto>> updateProductName(@Valid @RequestBody ProductUpdateNameDto dto) {
        return Mono.fromCallable(() -> {
            ProductResponseDto updated = productService.updateProductName(dto);
            return ResponseEntity.ok(new ResponseDto(true, "Nombre actualizado correctamente", updated));
        });
    }
}
