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
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final UserService userService;

  @ApiResponse(responseCode = "200", description = "Inscription réussie")
  @ApiResponse(responseCode = "400", description = "Données invalides")
  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
    return ResponseEntity.ok(authService.register(request));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }

  @ApiResponse(responseCode = "200", description = "Utilisateur récupéré avec succès")
  @ApiResponse(responseCode = "401", description = "Token invalide ou utilisateur non connecté")
  @GetMapping("/me")
  public ResponseEntity<UserResponse> getUser(Authentication authentication){
    UserResponse userResponse = authService.getUser(authentication.getName());
    return ResponseEntity.ok(userResponse);
  }
}
