package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.AuthResponse;
import com.openclassrooms.chatop.dto.LoginRequest;
import com.openclassrooms.chatop.dto.RegisterRequest;
import com.openclassrooms.chatop.dto.UserResponse;
import com.openclassrooms.chatop.repository.UserRepository;
import com.openclassrooms.chatop.service.AuthService;
import com.openclassrooms.chatop.service.UserService;
import com.openclassrooms.chatop.model.User;
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
  private final UserService userService;

  @Operation(summary = "Créer un compte", description = "Inscrit un utilisateur et retourne un JWT.")
  @ApiResponse(responseCode = "200", description = "Inscription réussie")
  @ApiResponse(responseCode = "400", description = "Données invalides")
  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
    return ResponseEntity.ok(authService.register(request));
  }

  @Operation(summary = "Se connecter", description = "Authentifie un utilisateur et retourne un JWT.")
  @ApiResponse(responseCode = "200", description = "Connexion réussie")
  @ApiResponse(responseCode = "401", description = "Identifiants invalides")
  @ApiResponse(responseCode = "400", description = "Données invalides")
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }

  @ApiResponse(responseCode = "200", description = "Utilisateur récupéré avec succès")
  @ApiResponse(responseCode = "401", description = "Token invalide ou utilisateur non connecté")
  @Operation(summary = "Afficher mon profil", security = @SecurityRequirement(name = "bearerAuth"))
  @GetMapping("/me")
  public ResponseEntity<UserResponse> getUser(Authentication authentication){
    UserResponse userResponse = authService.getUser(authentication.getName());
    return ResponseEntity.ok(userResponse);
  }
}
