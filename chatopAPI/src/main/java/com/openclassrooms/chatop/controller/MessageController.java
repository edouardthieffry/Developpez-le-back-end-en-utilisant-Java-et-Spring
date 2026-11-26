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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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

  /** Enregistre un message pour l'utilisateur authentifié et la location indiquée. */
  @Operation(summary = "Envoyer un message", description = "Enregistre un message pour l'utilisateur identifié par le JWT et la location indiquée.", security = @SecurityRequirement(name = "bearerAuth"))
  @ApiResponse(responseCode = "200", description = "Message envoyé")
  @ApiResponse(responseCode = "400", description = "Données invalides")
  @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié")
  @ApiResponse(responseCode = "403", description = "Le user_id fourni ne correspond pas à l'utilisateur authentifié")
  @ApiResponse(responseCode = "500", description = "Erreur interne")
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
    description = "Contenu du message et identifiants utilisateur/location au format JSON. Le user_id doit correspondre au JWT.",
    required = true,
    content = @io.swagger.v3.oas.annotations.media.Content(
      mediaType = "application/json",
      schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = MessageRequest.class)
    )
  )
  @PostMapping
  public ResponseEntity<MessageResponse> create(@Valid @RequestBody MessageRequest request,
                                                @AuthenticationPrincipal Jwt jwt) {
    messageService.createMessage(request, jwt.getSubject());
    return ResponseEntity.ok(new MessageResponse("Message send with success"));
  }
}
