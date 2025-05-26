package com.franchise.api.controller;

import com.franchise.api.constant.HttpConstant;
import com.franchise.api.dto.branch.BranchDto;
import com.franchise.api.dto.branch.BranchResponseDto;
import com.franchise.api.dto.branch.BranchUpdateDto;
import com.franchise.api.dto.ResponseDto;
import com.franchise.api.service.BranchService;
import jakarta.validation.Valid;
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

    @PostMapping("/add")
    public Mono<ResponseEntity<ResponseDto>> addBranch(@Valid @RequestBody BranchDto branchDto) {
        return Mono.fromCallable(() -> {
            BranchResponseDto created = branchService.addBranch(branchDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDto(true, "Sucursal agregada exitosamente", created));
        });
    }

    @PatchMapping("/{id}")
    public Mono<ResponseEntity<ResponseDto>> updateBranch(
            @PathVariable Long id,
            @Valid @RequestBody BranchUpdateDto dto) {
        return Mono.fromCallable(() -> {
            BranchResponseDto updated = branchService.updateBranchName(id, dto);
            return ResponseEntity.ok(new ResponseDto(true, "Sucursal actualizada exitosamente", updated));
        });
    }
}
