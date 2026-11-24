package com.openclassrooms.chatop.dto;

import com.openclassrooms.chatop.model.User;

import java.time.LocalDateTime;

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
