package com.openclassrooms.chatop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Identifiants de connexion")
public class LoginRequest {

  @NotBlank
  @Email
  @Schema(description = "Adresse e-mail", example = "utilisateur@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
  private String email;

  @NotBlank
  @Schema(description = "Mot de passe", example = "motdepasse", requiredMode = Schema.RequiredMode.REQUIRED)
  private String password;
}
