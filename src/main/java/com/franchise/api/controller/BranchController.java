package com.franchise.api.controller;

import com.franchise.api.constant.HttpConstant;
import com.franchise.api.dto.BranchDto;
import com.franchise.api.dto.ResponseDto;
import com.franchise.api.entities.Branch;
import com.franchise.api.service.BranchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(HttpConstant.PATH_BRANCH)
public class BranchController {
    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @Operation(summary = "Agregar sucursal a una franquicia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sucursal creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Franquicia no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @PostMapping("/add")
    public Mono<ResponseEntity<ResponseDto>> addBranch(@RequestBody BranchDto branchDto) {
        return Mono.fromCallable(() -> {
            if (branchDto.getFranchiseId() == null || branchDto.getName() == null || branchDto.getName().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ResponseDto(false, "Faltan datos requeridos", null));
            }

            Branch created = branchService.addBranch(branchDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDto(true, "Sucursal agregada exitosamente", created));
        });
    }

    @Operation(summary = "Actualizar el nombre de una sucursal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucursal actualizada"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    @PatchMapping("/{branchId}")
    public Mono<ResponseEntity<ResponseDto>> updateBranchName(
            @PathVariable Long branchId,
            @RequestBody BranchDto branchDto) {

        return Mono.fromCallable(() -> {
            if (branchDto.getName() == null || branchDto.getName().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ResponseDto(false, "Nombre no puede estar vacío", null));
            }

            Branch updated = branchService.updateBranchName(branchId, branchDto.getName());
            return ResponseEntity.ok(new ResponseDto(true, "Sucursal actualizada", updated));
        });
    }
}
