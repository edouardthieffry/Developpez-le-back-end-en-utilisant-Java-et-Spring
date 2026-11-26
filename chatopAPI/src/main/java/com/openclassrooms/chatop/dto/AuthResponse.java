package com.openclassrooms.chatop.dto;

import lombok.Builder;
import io.swagger.v3.oas.annotations.media.Schema;

@Builder
@Schema(description = "Jeton d'accès retourné après authentification")
public record AuthResponse(String token) {
}
