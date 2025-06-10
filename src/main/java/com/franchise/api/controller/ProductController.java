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

    public ProductController(ProductService productService, FranchiseService franchiseService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    public Mono<ResponseEntity<ResponseDto>> addProductToBranch(@Valid @RequestBody ProductDto productDto) {
        return productService.addProductToBranch(productDto)
                .map(saved -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDto(true, "Producto agregado exitosamente", saved)));
    }

    @PatchMapping("/update-stock")
    public Mono<ResponseEntity<ResponseDto>> updateStock(@Valid @RequestBody StockUpdateDto dto) {
        return productService.updateStock(dto)
                .map(updated -> ResponseEntity.ok(new ResponseDto(true, "Stock actualizado correctamente", updated)));
    }

    @DeleteMapping("/{productId}")
    public Mono<ResponseEntity<ResponseDto>> deleteProduct(@PathVariable Long productId) {
        return productService.deleteProduct(productId)
                .map(deleted -> deleted
                        ? ResponseEntity.ok(new ResponseDto(true, "Producto eliminado correctamente", null))
                        : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(false, "Producto no encontrado", null)));
    }

    @GetMapping("/{franchiseId}/max-stock-products")
    public Mono<ResponseEntity<ResponseDto>> getMaxStockProducts(@PathVariable Long franchiseId) {
        return productService.getMaxStockProductsByFranchise(franchiseId)
                .collectList()
                .map(list -> ResponseEntity.ok(new ResponseDto(true, "Productos con mayor stock por sucursal", list)));
    }

    @PatchMapping("/update-name")
    public Mono<ResponseEntity<ResponseDto>> updateProductName(@Valid @RequestBody ProductUpdateNameDto dto) {
        return productService.updateProductName(dto)
                .map(updated -> ResponseEntity.ok(new ResponseDto(true, "Nombre actualizado correctamente", updated)));
    }
}
