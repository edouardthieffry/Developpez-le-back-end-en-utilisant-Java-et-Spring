package com.openclassrooms.chatop.dto;

import com.openclassrooms.chatop.model.Rental;

import java.time.format.DateTimeFormatter;

public record RentalResponse(
  Integer id,
  String name,
  Float surface,
  Float price,
  String picture,
  String description,
  Integer owner_id,
  String created_at,
  String updated_at
) {
  public static RentalResponse from(Rental rental) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    return new RentalResponse(
      rental.getId(),
      rental.getName(),
      rental.getSurface(),
      rental.getPrice(),
      rental.getPicture(),
      rental.getDescription(),
      rental.getUser().getId(),
      rental.getCreatedAt().format(formatter),
      rental.getUpdatedAt().format(formatter)
    );
  }
}
