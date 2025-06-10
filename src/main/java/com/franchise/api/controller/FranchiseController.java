package com.franchise.api.controller;

import com.franchise.api.constant.HttpConstant;
import com.franchise.api.dto.franchise.FranchiseCreateDto;
import com.franchise.api.dto.franchise.FranchiseUpdateDto;
import com.franchise.api.dto.ResponseDto;
import com.franchise.api.service.FranchiseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(HttpConstant.PATH_FRANCHISE)
public class FranchiseController {
    private final FranchiseService franchiseService;

    public FranchiseController(FranchiseService franchiseService) {
        this.franchiseService = franchiseService;
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<ResponseDto>> createFranchise(@Valid @RequestBody FranchiseCreateDto dto) {
        return franchiseService.createFranchise(dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDto(true, "Franquicia creada exitosamente", created)));
    }

    @PatchMapping("/{id}")
    public Mono<ResponseEntity<ResponseDto>> updateFranchise(
            @PathVariable Long id,
            @Valid @RequestBody FranchiseUpdateDto dto) {
        return franchiseService.updateFranchise(id, dto)
                .map(updated -> ResponseEntity.ok(
                        new ResponseDto(true, "Franquicia actualizada exitosamente", updated)));
    }
}
