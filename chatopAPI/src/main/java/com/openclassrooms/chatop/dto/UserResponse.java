package com.openclassrooms.chatop.dto;

import com.openclassrooms.chatop.model.User;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informations publiques d'un utilisateur")
public record UserResponse(
  Integer id,
  String name,
  String email,
  LocalDateTime createdAt,
  LocalDateTime updatedAt) {

  public static UserResponse from(User user) {
    return new UserResponse(
      user.getId(),
      user.getName(),
      user.getEmail(),
      user.getCreatedAt(),
      user.getUpdatedAt());
  }
}
