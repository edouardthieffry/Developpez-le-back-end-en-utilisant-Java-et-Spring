package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.MessageResponse;
import com.openclassrooms.chatop.dto.RentalCreateRequest;
import com.openclassrooms.chatop.dto.RentalResponse;
import com.openclassrooms.chatop.dto.RentalsResponse;
import com.openclassrooms.chatop.dto.RentalUpdateRequest;
import com.openclassrooms.chatop.exceptions.NotFoundException;
import com.openclassrooms.chatop.service.RentalService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
@Tag(name = "Locations", description = "Consultation et gestion des locations")
public class RentalController {
  private final RentalService rentalService;

  /** Retourne la liste des locations disponibles. */
  @ApiResponse(responseCode = "200", description = "Liste des locations récupérée")
  @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié")
  @ApiResponse(responseCode = "500", description = "Erreur interne")
  @Operation(summary = "Lister les locations", description = "Retourne toutes les locations. Un JWT Bearer valide est requis.", security = @SecurityRequirement(name = "bearerAuth"))
  @GetMapping
  public ResponseEntity<RentalsResponse> getAll(){
    return ResponseEntity.ok(new RentalsResponse(rentalService.getAllRentals()));
  }

  /** Recherche une location à partir de son identifiant. */
  @ApiResponse(responseCode = "200", description = "Détail de la location récupéré")
  @ApiResponse(responseCode = "404", description = "Location introuvable")
  @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié")
  @ApiResponse(responseCode = "500", description = "Erreur interne")
  @Operation(summary = "Consulter une location", description = "Retourne le détail d'une location. Un JWT Bearer valide est requis.", security = @SecurityRequirement(name = "bearerAuth"))
  @GetMapping("/{id}")
  public ResponseEntity<RentalResponse> getOne(@Parameter(description = "Identifiant de la location", example = "1") @PathVariable Integer id){
    RentalResponse rentalResponse = rentalService.getRentalById(id)
      .orElseThrow(() -> new NotFoundException("Location non trouvée"));
    return ResponseEntity.ok(rentalResponse);
  }

  /** Crée une location à partir des champs et de la photo reçus en multipart. */
  @Operation(summary = "Créer une location", description = "La création nécessite un JWT et une image au format multipart/form-data.", security = @SecurityRequirement(name = "bearerAuth"))
  @ApiResponse(responseCode = "200", description = "Location créée")
  @ApiResponse(responseCode = "400", description = "Données invalides")
  @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié")
  @ApiResponse(responseCode = "500", description = "Erreur interne lors du stockage de l'image")
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
    description = "Champs de la location et image envoyés en multipart/form-data",
    required = true,
    content = @io.swagger.v3.oas.annotations.media.Content(
      mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
      schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = RentalCreateRequest.class)
    )
  )
  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<MessageResponse> createRental(@Valid @ModelAttribute RentalCreateRequest request, @AuthenticationPrincipal Jwt jwt
  ) throws IOException {
    String email = jwt.getSubject();
    rentalService.rentalCreate(request, email);
    return ResponseEntity.ok(new MessageResponse("Location créée !"));
  }
  /** Met à jour une location existante ; seul son propriétaire peut la modifier. */
  @Operation(summary = "Modifier une location", description = "Modifie une location en multipart/form-data. Seul son propriétaire peut la modifier ; un JWT Bearer valide est requis.", security = @SecurityRequirement(name = "bearerAuth"))
  @ApiResponse(responseCode = "200", description = "Location modifiée")
  @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié")
  @ApiResponse(responseCode = "403", description = "L'utilisateur connecté n'est pas le propriétaire")
  @ApiResponse(responseCode = "404", description = "Location introuvable")
  @ApiResponse(responseCode = "400", description = "Données de modification invalides")
  @ApiResponse(responseCode = "500", description = "Erreur interne")
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
    description = "Champs modifiés envoyés en multipart/form-data",
    required = true,
    content = @io.swagger.v3.oas.annotations.media.Content(
      mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
      schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = RentalUpdateRequest.class)
    )
  )
  @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageResponse> update(
    @Parameter(description = "Identifiant de la location", example = "1") @PathVariable Integer id,
    @Valid @ModelAttribute RentalUpdateRequest request,
    @AuthenticationPrincipal Jwt jwt
  ) {
    String email = jwt.getSubject();
    rentalService.updateRental(id, request, email);
    return ResponseEntity.ok(new MessageResponse("Rental updated !"));
  }
}
