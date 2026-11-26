package com.openclassrooms.chatop.dto;

import java.time.Instant;
import java.util.Map;

/** Structure commune des réponses d'erreur de l'API. */
public record ApiError(
  Instant timestamp,
  int status,
  String error,
  String message,
  String path,
  Map<String, String> validationErrors
) {
}
