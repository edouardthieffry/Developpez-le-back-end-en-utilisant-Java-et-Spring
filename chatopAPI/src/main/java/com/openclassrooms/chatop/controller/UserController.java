package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.UserResponse;
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

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "Utilisateurs", description = "Consultation des utilisateurs")
public class UserController {
  private final UserService userService;
  @Operation(summary = "Consulter un utilisateur")
  @ApiResponse(responseCode = "200", description = "Utilisateur trouvé")
  @ApiResponse(responseCode = "404", description = "Utilisateur introuvable")
  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id) {

    UserResponse userResponse = UserResponse.from(userService.getUser(id).orElseThrow(()-> new RuntimeException("Utilisateur non trouvé")));;
    return ResponseEntity.ok(userResponse);
  }
}
