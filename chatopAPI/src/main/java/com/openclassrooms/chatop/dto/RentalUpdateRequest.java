package com.openclassrooms.chatop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Données de modification d'une location")
public class RentalUpdateRequest {
  @NotBlank
  @Size(max = 255)
  @Schema(description = "Titre de la location", example = "Appartement lumineux", requiredMode = Schema.RequiredMode.REQUIRED)
  private String name;

  @NotNull
  @Min(1)
  @Schema(description = "Surface en mètres carrés", example = "45", requiredMode = Schema.RequiredMode.REQUIRED)
  private Float surface;

  @NotNull
  @Min(1)
  @Schema(description = "Loyer", example = "850", requiredMode = Schema.RequiredMode.REQUIRED)
  private Float price;

  @NotBlank
  @Size(max = 2000)
  @Schema(description = "Description de la location", requiredMode = Schema.RequiredMode.REQUIRED)
  private String description;
}
