package es.jcyl.eclap.spring.backend.controladores;


import es.jcyl.eclap.spring.backend.dto.ErrorDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GestorExcepcion {

    // 404: la entidad pedida (tarea, usuario...) no existe
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> gestionNoEncontrado(EntityNotFoundException ex, HttpServletRequest request) {
        log.warn("Recurso no encontrado en {}: {}", request.getRequestURI(), ex.getMessage());
        ErrorDto error = ErrorDto.builder()
                .fecha(LocalDateTime.now())
                .estadoHttp(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .mensaje(ex.getMessage())
                .ruta(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // 400: falló alguna anotación de validación (@Valid) del @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> gestionValidacion(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("Validación fallida en {}", request.getRequestURI());
        Map<String, String> camposConError = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            camposConError.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorDto error = ErrorDto.builder()
                .fecha(LocalDateTime.now())
                .estadoHttp(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .mensaje("Hay campos que no cumplen las validaciones")
                .ruta(request.getRequestURI())
                .camposConError(camposConError)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> gestionarJsonMalFormado(HttpMessageNotReadableException ex, HttpServletRequest request) {
        ErrorDto error = ErrorDto.builder()
                .fecha(LocalDateTime.now())
                .estadoHttp(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .mensaje("El cuerpo de la petición no es un JSON válido o falta")
                .ruta(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 500: cualquier fallo viniendo de Spring Data / JDBC (constraints violadas, conexión perdida con Oracle, etc.)
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorDto> gestionErrorBaseDatos(DataAccessException ex, HttpServletRequest request) {
        log.error("Error de base de datos en {}", request.getRequestURI(), ex);
        ErrorDto error = ErrorDto.builder()
                .fecha(LocalDateTime.now())
                .estadoHttp(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .mensaje("Error de acceso a la base de datos")
                .ruta(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }


}
