package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.dto.RentalCreateRequest;
import com.openclassrooms.chatop.dto.RentalResponse;
import com.openclassrooms.chatop.exceptions.UnauthorizedException;
import com.openclassrooms.chatop.model.Rental;
import com.openclassrooms.chatop.model.User;
import com.openclassrooms.chatop.repository.RentalRepository;
import com.openclassrooms.chatop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RentalService {
  private final UserRepository userRepository;
  private final RentalRepository rentalRepository;
  private final FileStorageService fileStorageService;

  public RentalService(UserRepository userRepository, RentalRepository rentalRepository, FileStorageService fileStorageService) {
    this.userRepository = userRepository;
    this.rentalRepository = rentalRepository;
    this.fileStorageService = fileStorageService;
  }

  public List<RentalResponse> getAllRentals() {
    return rentalRepository.findAll()
      .stream()
      .map(RentalResponse::from)
      .toList();
  }

  public Optional<RentalResponse> getRentalById(Integer id) {
    return rentalRepository.findById(id).map(RentalResponse::from);
  }

  public void rentalCreate(RentalCreateRequest request, String ownerEmail) {
    User owner = userRepository.findByEmail(ownerEmail)
      .orElseThrow(() -> new UnauthorizedException("Owner non trouvé"));

    String pictureUrl = fileStorageService.storeFile(request.getPicture());

    Rental rental = new Rental();
    rental.setName(request.getName());
    rental.setSurface(request.getSurface());
    rental.setPrice(request.getPrice());
    rental.setDescription(request.getDescription());
    rental.setPicture(pictureUrl);
    rental.setUser(owner);
    rental.setCreatedAt(LocalDateTime.now());
    rental.setUpdatedAt(LocalDateTime.now());

    rentalRepository.save(rental);
  }
}
