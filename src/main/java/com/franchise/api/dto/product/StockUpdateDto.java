package com.franchise.api.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockUpdateDto {
    @NotNull (message = "El ID del producto es requerido")
    private Long productId;
    @NotNull(message = "La nueva cantidad de stock es requerida")
    @Positive(message = "El stock debe ser mayor a 0")
    private Integer newStock;
}
