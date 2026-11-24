package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.dto.RentalResponse;
import com.openclassrooms.chatop.model.Rental;
import com.openclassrooms.chatop.repository.RentalRepository;
import com.openclassrooms.chatop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RentalService {
  private final UserRepository userRepository;
  private final RentalRepository rentalRepository;

  public RentalService(UserRepository userRepository, RentalRepository rentalRepository) {
    this.userRepository = userRepository;
    this.rentalRepository = rentalRepository;
  }

  public List<RentalResponse> getAllRentals() {
    return rentalRepository.findAll()
      .stream()
      .map(RentalResponse::from)
      .toList();
  }

  public Optional<RentalResponse> getRentalById(Integer id){
    return rentalRepository.findById(id).map(RentalResponse::from);
  }
}
