package com.franchise.api.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockUpdateDto {
    @NotNull (message = "El ID del producto es requerido")
    private Long productId;
    @NotBlank(message = "la nueva cantidad de stock no puede estar vacía")
    private Integer newStock;
}
