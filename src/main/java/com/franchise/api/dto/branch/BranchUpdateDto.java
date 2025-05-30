package com.franchise.api.dto.branch;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchUpdateDto {
    @NotBlank(message = "El nombre de la sucursal no puede estar vacío")
    private String name;
}
