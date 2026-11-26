package com.openclassrooms.chatop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Message associé à une location et à son auteur")
public class MessageRequest {
  @NotBlank
  @Schema(description = "Contenu du message", example = "Bonjour, cette location est-elle disponible ?", requiredMode = Schema.RequiredMode.REQUIRED)
  private String message;

  @NotNull
  @Schema(description = "Identifiant de l'utilisateur authentifié par le JWT", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
  private Integer user_id;

  @NotNull
  @Schema(description = "Identifiant de la location", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
  private Integer rental_id;
}
