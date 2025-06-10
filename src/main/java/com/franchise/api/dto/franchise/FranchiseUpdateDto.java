package com.franchise.api.dto.franchise;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FranchiseUpdateDto {
    @NotBlank (message = "El nombre de la franquicia no puede estar vacío")
    private String name;
}
