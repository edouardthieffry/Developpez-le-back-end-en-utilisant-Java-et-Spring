package com.openclassrooms.chatop.exceptions;

import com.openclassrooms.chatop.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(BindException.class)
  public ResponseEntity<ApiError> handleValidation(BindException exception, HttpServletRequest request) {
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

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ApiError> handleConflict(ConflictException exception, HttpServletRequest request) {
    return response(HttpStatus.CONFLICT, exception.getMessage(), request, Map.of());
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

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ApiError> handleResponseStatus(ResponseStatusException exception, HttpServletRequest request) {
    HttpStatus status = HttpStatus.valueOf(exception.getStatusCode().value());
    String message = exception.getReason() == null ? status.getReasonPhrase() : exception.getReason();
    return response(status, message, request, Map.of());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiError> handleDataConflict(DataIntegrityViolationException exception, HttpServletRequest request) {
    log.warn("Database constraint violation on {}", request.getRequestURI());
    return response(HttpStatus.CONFLICT, "La requête entre en conflit avec une donnée existante.", request, Map.of());
  }

  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<ApiError> handleDatabaseError(DataAccessException exception, HttpServletRequest request) {
    log.error("Database error on {}", request.getRequestURI(), exception);
    return response(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur de base de données est survenue.", request, Map.of());
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ApiError> handleUploadTooLarge(MaxUploadSizeExceededException exception, HttpServletRequest request) {
    return response(HttpStatus.PAYLOAD_TOO_LARGE, "Le fichier envoyé dépasse la taille autorisée.", request, Map.of());
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ApiError> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException exception,
                                                              HttpServletRequest request) {
    return response(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Le type de contenu envoyé n'est pas pris en charge.", request, Map.of());
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ApiError> handleMethodNotAllowed(HttpRequestMethodNotSupportedException exception,
                                                         HttpServletRequest request) {
    return response(HttpStatus.METHOD_NOT_ALLOWED, "La méthode HTTP n'est pas autorisée pour cette route.", request, Map.of());
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
