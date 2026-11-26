package com.openclassrooms.chatop.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Liste des locations")
public record RentalsResponse(List<RentalResponse> rentals) {
}
