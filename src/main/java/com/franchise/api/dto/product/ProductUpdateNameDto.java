package com.franchise.api.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateNameDto {
    @NotNull(message = "El ID del producto es requerido")
    private Long id;
    @NotBlank(message = "El nombre del producto no puede estar vacío")
    private String name;
}
