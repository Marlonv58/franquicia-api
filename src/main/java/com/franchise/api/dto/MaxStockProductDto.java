package com.franchise.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaxStockProductDto {
    private String productName;
    private Integer stock;
    private String branchName;
}
