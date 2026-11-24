package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.RentalResponse;
import com.openclassrooms.chatop.service.RentalService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Min;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {
  private final RentalService rentalService;

  @ApiResponse(responseCode = "200", description = "Liste des locations récupérée")
  @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié")
  @GetMapping
  public ResponseEntity<List> getAll(){
    return ResponseEntity.ok(rentalService.getAllRentals());
  }

  @ApiResponse(responseCode = "200", description = "Détail de la location récupéré")
  @ApiResponse(responseCode = "401", description = "Location non trouvée ou Utilisateur non authentifié")
  @GetMapping("/{id}")
  public ResponseEntity<RentalResponse> getOne(@PathVariable Integer id){
    RentalResponse rentalResponse = rentalService.getRentalById(id).orElseThrow(()->new RuntimeException("Location non trouvée ou utilisateur non authentifié"));
    return ResponseEntity.ok(rentalResponse);
  }
}
