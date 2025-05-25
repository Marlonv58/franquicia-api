package com.franchise.api.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaxStockProductDto {
    private Long productId;
    private String productName;
    private Integer stock;
    private Long branchId;
    private String branchName;
}
