package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.MessageRequest;
import com.openclassrooms.chatop.dto.MessageResponse;
import com.openclassrooms.chatop.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Messages", description = "Envoi de messages au sujet des locations")
public class MessageController {
  private final MessageService messageService;

  @Operation(summary = "Envoyer un message", description = "Enregistre un message à partir d'un corps JSON.", security = @SecurityRequirement(name = "bearerAuth"))
  @ApiResponse(responseCode = "200", description = "Message envoyé")
  @ApiResponse(responseCode = "400", description = "Données invalides")
  @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié")
  @ApiResponse(responseCode = "500", description = "Erreur interne")
  @PostMapping
  public ResponseEntity<MessageResponse> create(@Valid @RequestBody MessageRequest request) {
    messageService.createMessage(request);
    return ResponseEntity.ok(new MessageResponse("Message send with success"));
  }
}
