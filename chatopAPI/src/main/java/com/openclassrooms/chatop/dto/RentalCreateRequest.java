package com.openclassrooms.chatop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Données d'une nouvelle location, envoyées en multipart/form-data")
public class RentalCreateRequest {
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

  @NotNull(message = "La photo est obligatoire pour créer une location")
  @Schema(description = "Photo de la location", type = "string", format = "binary", requiredMode = Schema.RequiredMode.REQUIRED)
  private MultipartFile picture;
}
