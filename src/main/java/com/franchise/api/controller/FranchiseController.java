package com.franchise.api.controller;

import com.franchise.api.constant.HttpConstant;
import com.franchise.api.dto.FranchiseDto;
import com.franchise.api.dto.ResponseDto;
import com.franchise.api.entities.Franchise;
import com.franchise.api.service.FranchiseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(HttpConstant.PATH_FRANCHISE)
public class FranchiseController {
    private FranchiseService franchiseService;

    @Autowired
    public void setFranchiseService(FranchiseService franchiseService) {
        this.franchiseService = franchiseService;
    }

    @Operation(summary = "Crear una nueva franquicia", description = "Agrega una nueva franquicia con su nombre.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Franquicia creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Nombre inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/create")
    public Mono<ResponseEntity<ResponseDto>> createFranchise(@RequestBody FranchiseDto franchiseDto) {
        return Mono.fromCallable(() -> {
            if (franchiseDto.getName() == null || franchiseDto.getName().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ResponseDto(false, "El nombre de la franquicia es requerido", null));
            }

            Franchise created = franchiseService.createFranchise(franchiseDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDto(true, "Franquicia creada exitosamente", created));
        });
    }

    @Operation(summary = "Actualizar nombre de una franquicia", description = "Actualiza el nombre de una franquicia existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Franquicia actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID o nombre inválido"),
            @ApiResponse(responseCode = "404", description = "Franquicia no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}")
    public Mono<ResponseEntity<ResponseDto>> updateFranchise(
            @PathVariable Long id,
            @RequestBody FranchiseDto franchiseDto) {

        return Mono.fromCallable(() -> {
            if (franchiseDto.getName() == null || franchiseDto.getName().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ResponseDto(false, "El nombre de la franquicia es requerido", null));
            }

            Franchise updated = franchiseService.updateFranchise(id, franchiseDto);
            return ResponseEntity.ok(new ResponseDto(true, "Franquicia actualizada exitosamente", updated));
        });
    }

    @Operation(summary = "Obtener franquicia por ID", description = "Devuelve los datos de una franquicia específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Franquicia encontrada"),
            @ApiResponse(responseCode = "404", description = "Franquicia no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ResponseDto>> getFranchise(@PathVariable Long id) {
        return Mono.fromCallable(() -> {
            Franchise franchise = franchiseService.getFranchiseById(id);
            if (franchise == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDto(false, "Franquicia no encontrada", null));
            }
            return ResponseEntity.ok(new ResponseDto(true, "Franquicia recuperada exitosamente", franchise));
        });
    }
}
