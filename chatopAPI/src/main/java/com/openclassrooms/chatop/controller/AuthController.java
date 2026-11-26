package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.AuthResponse;
import com.openclassrooms.chatop.dto.LoginRequest;
import com.openclassrooms.chatop.dto.RegisterRequest;
import com.openclassrooms.chatop.dto.UserResponse;
import com.openclassrooms.chatop.service.AuthService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Inscription, connexion et profil de l'utilisateur connecté")
public class AuthController {

  private final AuthService authService;

  /**
   * Crée un compte à partir d'un corps JSON validé et retourne immédiatement son JWT.
   * Cette route est publique afin qu'un nouvel utilisateur puisse s'inscrire.
   */
  @Operation(summary = "Créer un compte", description = "Inscrit un utilisateur et retourne un JWT.")
  @ApiResponse(responseCode = "200", description = "Inscription réussie")
  @ApiResponse(responseCode = "400", description = "Données invalides")
  @ApiResponse(responseCode = "409", description = "Un compte utilise déjà cette adresse e-mail")
  @ApiResponse(responseCode = "500", description = "Erreur interne")
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
    description = "Nom, adresse e-mail et mot de passe au format JSON",
    required = true,
    content = @io.swagger.v3.oas.annotations.media.Content(
      mediaType = "application/json",
      schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = RegisterRequest.class)
    )
  )
  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
    return ResponseEntity.ok(authService.register(request));
  }

  /**
   * Vérifie les identifiants reçus en JSON et retourne un JWT en cas de succès.
   * Cette route est publique afin de permettre l'authentification.
   */
  @Operation(summary = "Se connecter", description = "Authentifie un utilisateur et retourne un JWT.")
  @ApiResponse(responseCode = "200", description = "Connexion réussie")
  @ApiResponse(responseCode = "401", description = "Identifiants invalides")
  @ApiResponse(responseCode = "400", description = "Données invalides")
  @ApiResponse(responseCode = "500", description = "Erreur interne")
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
    description = "Adresse e-mail et mot de passe au format JSON",
    required = true,
    content = @io.swagger.v3.oas.annotations.media.Content(
      mediaType = "application/json",
      schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = LoginRequest.class)
    )
  )
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }

  /** Lit le profil associé au sujet du JWT de la requête. */
  @ApiResponse(responseCode = "200", description = "Utilisateur récupéré avec succès")
  @ApiResponse(responseCode = "401", description = "Token invalide ou utilisateur non connecté")
  @ApiResponse(responseCode = "500", description = "Erreur interne")
  @Operation(summary = "Afficher mon profil", description = "Retourne le profil associé au JWT fourni.", security = @SecurityRequirement(name = "bearerAuth"))
  @GetMapping("/me")
  public ResponseEntity<UserResponse> getUser(Authentication authentication){
    UserResponse userResponse = authService.getUser(authentication.getName());
    return ResponseEntity.ok(userResponse);
  }
}
