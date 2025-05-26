package com.franchise.api.exceptions;

import com.franchise.api.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Maneja errores generales (RuntimeException, NullPointerException, etc.)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDto> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDto(false, ex.getMessage(), null));
    }

    // Maneja errores cuando un recurso no se encuentra (opcional si tienes excepciones personalizadas)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDto> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto(false, ex.getMessage(), null));
    }

    // Maneja errores por argumentos inválidos en el cuerpo de la solicitud (por ejemplo, validaciones con @Valid)
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleValidation(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Datos inválidos");

        return ResponseEntity.badRequest()
                .body(new ResponseDto(false, errorMessage, null));
    }

    // Maneja errores cuando una entidad ya existe, si decides usar una excepción personalizada para eso
    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<ResponseDto> handleDuplicate(DuplicateEntityException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ResponseDto(false, ex.getMessage(), null));
    }
}
