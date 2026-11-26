package com.openclassrooms.chatop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Informations nécessaires à la création d'un compte")
public class RegisterRequest {

  @NotBlank
  @Schema(description = "Nom affiché", example = "Jean Dupont", requiredMode = Schema.RequiredMode.REQUIRED)
  private String name;

  @NotBlank
  @Email
  @Schema(description = "Adresse e-mail", example = "utilisateur@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
  private String email;

  @NotBlank
  @Schema(description = "Mot de passe", example = "motdepasse", requiredMode = Schema.RequiredMode.REQUIRED)
  private String password;
}
