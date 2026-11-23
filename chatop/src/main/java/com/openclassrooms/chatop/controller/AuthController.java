package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.AuthResponse;
import com.openclassrooms.chatop.dto.RegisterRequest;
import com.openclassrooms.chatop.service.AuthService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @ApiResponse(responseCode = "200", description = "Inscription réussie")
  @ApiResponse(responseCode = "400", description = "Données invalides")
  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@Valid RegisterRequest request) {
    return ResponseEntity.ok(authService.register(request));
  }
}
