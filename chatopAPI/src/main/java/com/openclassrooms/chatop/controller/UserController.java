package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.UserResponse;
import com.openclassrooms.chatop.exceptions.NotFoundException;
import com.openclassrooms.chatop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "Utilisateurs", description = "Consultation des utilisateurs")
public class UserController {
  private final UserService userService;
  /** Retourne le profil public correspondant à l'identifiant fourni. */
  @Operation(summary = "Consulter un utilisateur", description = "Retourne le profil public d'un utilisateur. Un JWT Bearer valide est requis.", security = @SecurityRequirement(name = "bearerAuth"))
  @ApiResponse(responseCode = "200", description = "Utilisateur trouvé")
  @ApiResponse(responseCode = "404", description = "Utilisateur introuvable")
  @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié")
  @ApiResponse(responseCode = "500", description = "Erreur interne")
  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(@Parameter(description = "Identifiant de l'utilisateur", example = "1") @PathVariable Integer id) {

    UserResponse userResponse = UserResponse.from(userService.getUser(id)
      .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé")));
    return ResponseEntity.ok(userResponse);
  }
}
