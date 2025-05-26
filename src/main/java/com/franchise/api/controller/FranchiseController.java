package com.franchise.api.controller;

import com.franchise.api.constant.HttpConstant;
import com.franchise.api.dto.franchise.FranchiseCreateDto;
import com.franchise.api.dto.franchise.FranchiseUpdateDto;
import com.franchise.api.dto.ResponseDto;
import com.franchise.api.dto.franchise.FranchiseResponseDto;
import com.franchise.api.entities.Franchise;
import com.franchise.api.service.FranchiseService;
import jakarta.validation.Valid;
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

    @PostMapping("/create")
    public Mono<ResponseEntity<ResponseDto>> createFranchise(@Valid @RequestBody FranchiseCreateDto dto) {
        Franchise created = franchiseService.createFranchise(dto);
        return Mono.just(ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto(true, "Franquicia creada exitosamente", created)));
    }

    @PatchMapping("/{id}")
    public Mono<ResponseEntity<ResponseDto>> updateFranchise(@PathVariable Long id, @Valid @RequestBody FranchiseUpdateDto franchiseUpdateDto) {
        return Mono.fromCallable(() -> {
            FranchiseResponseDto updated = franchiseService.updateFranchise(id, franchiseUpdateDto);
            return ResponseEntity.ok(new ResponseDto(true, "Franquicia actualizada exitosamente", updated));
        });
    }
}
