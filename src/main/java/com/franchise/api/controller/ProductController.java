package com.franchise.api.controller;
import com.franchise.api.constant.HttpConstant;
import com.franchise.api.dto.MaxStockProductDto;
import com.franchise.api.dto.ProductDto;
import com.franchise.api.dto.ResponseDto;
import com.franchise.api.dto.StockUpdateDto;
import com.franchise.api.entities.Product;
import com.franchise.api.service.FranchiseService;
import com.franchise.api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Agregar un producto a una sucursal", description = "Agrega un nuevo producto a una sucursal existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto agregado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/add")
    public Mono<ResponseEntity<ResponseDto>> addProduct(@RequestBody ProductDto productDto) {
        return Mono.fromCallable(() -> {
            if (productDto.getBranchId() == null || productDto.getName() == null || productDto.getStock() == null) {
                return ResponseEntity.badRequest()
                        .body(new ResponseDto(false, "Faltan datos obligatorios", null));
            }

            Product product = productService.addProductToBranch(productDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDto(true, "Producto agregado exitosamente", product));
        });
    }

    @Operation(summary = "Actualizar stock de un producto", description = "Modifica el stock de un producto existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{productId}/stock")
    public Mono<ResponseEntity<ResponseDto>> updateStock(
            @PathVariable Long productId,
            @RequestBody StockUpdateDto stockDto) {

        return Mono.fromCallable(() -> {
            if (stockDto.getStock() == null || stockDto.getStock() < 0) {
                return ResponseEntity.badRequest()
                        .body(new ResponseDto(false, "El valor de stock debe ser válido", null));
            }

            Product updated = productService.updateStock(productId, stockDto.getStock());
            return ResponseEntity.ok()
                    .body(new ResponseDto(true, "Stock actualizado correctamente", updated));
        });
    }

    @Operation(summary = "Eliminar un producto", description = "Elimina un producto de una sucursal por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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

    @Operation(summary = "Obtener productos con más stock por sucursal",
            description = "Retorna el producto con mayor stock en cada sucursal de una franquicia específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado generado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Franquicia no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{franchiseId}/max-stock-products")
    public Mono<ResponseEntity<ResponseDto>> getMaxStockProducts(@PathVariable Long franchiseId) {
        return Mono.fromCallable(() -> {
            List<MaxStockProductDto> result = franchiseService.getMaxStockProductsByFranchise(franchiseId);
            return ResponseEntity.ok(new ResponseDto(true, "Productos con mayor stock por sucursal", result));
        });
    }
}
