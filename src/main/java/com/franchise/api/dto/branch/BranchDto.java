package com.franchise.api.dto.branch;

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
public class BranchDto {

    @NotNull(message = "El ID de la franquicia es requerido")
    private Long franchiseId;

    @NotBlank(message = "El nombre de la sucursal no puede estar vacío")
    private String name;
}
