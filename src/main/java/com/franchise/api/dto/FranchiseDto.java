package com.franchise.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FranchiseDto {
    private Long id;
    private String name;
    private Integer stock;
    private Long branchId;
    private Long productId;
    private String productName;
    private String branchName;
}
