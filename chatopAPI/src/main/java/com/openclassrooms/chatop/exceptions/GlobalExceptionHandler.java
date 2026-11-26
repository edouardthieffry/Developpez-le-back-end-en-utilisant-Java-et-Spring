package com.openclassrooms.chatop.exceptions;

import com.openclassrooms.chatop.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception,
                                                    HttpServletRequest request) {
    Map<String, String> errors = new TreeMap<>();
    for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
      errors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
    }
    return response(HttpStatus.BAD_REQUEST, "Les données fournies sont invalides.", request, errors);
  }

  @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
  public ResponseEntity<ApiError> handleMalformedRequest(Exception exception, HttpServletRequest request) {
    return response(HttpStatus.BAD_REQUEST, "La requête est invalide ou contient des données mal formées.", request, Map.of());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ApiError> handleBadRequest(BadRequestException exception, HttpServletRequest request) {
    return response(HttpStatus.BAD_REQUEST, exception.getMessage(), request, Map.of());
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiError> handleNotFound(NotFoundException exception, HttpServletRequest request) {
    return response(HttpStatus.NOT_FOUND, exception.getMessage(), request, Map.of());
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ApiError> handleUnauthorized(UnauthorizedException exception, HttpServletRequest request) {
    return response(HttpStatus.UNAUTHORIZED, exception.getMessage(), request, Map.of());
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ApiError> handleForbidden(ForbiddenException exception, HttpServletRequest request) {
    return response(HttpStatus.FORBIDDEN, exception.getMessage(), request, Map.of());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiError> handleConflict(DataIntegrityViolationException exception, HttpServletRequest request) {
    log.warn("Database constraint violation on {}", request.getRequestURI());
    return response(HttpStatus.CONFLICT, "La requête entre en conflit avec une donnée existante.", request, Map.of());
  }

  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<ApiError> handleDatabaseError(DataAccessException exception, HttpServletRequest request) {
    log.error("Database error on {}", request.getRequestURI(), exception);
    return response(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur de base de données est survenue.", request, Map.of());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleUnexpected(Exception exception, HttpServletRequest request) {
    log.error("Unexpected error on {}", request.getRequestURI(), exception);
    return response(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur interne est survenue.", request, Map.of());
  }

  private ResponseEntity<ApiError> response(HttpStatus status, String message, HttpServletRequest request,
                                            Map<String, String> validationErrors) {
    ApiError body = new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), message,
      request.getRequestURI(), validationErrors);
    return ResponseEntity.status(status).body(body);
  }
}
