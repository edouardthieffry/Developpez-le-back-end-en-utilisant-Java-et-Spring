package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.dto.MessageRequest;
import com.openclassrooms.chatop.model.Message;
import com.openclassrooms.chatop.model.Rental;
import com.openclassrooms.chatop.model.User;
import com.openclassrooms.chatop.exceptions.BadRequestException;
import com.openclassrooms.chatop.repository.MessageRepository;
import com.openclassrooms.chatop.repository.RentalRepository;
import com.openclassrooms.chatop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final RentalRepository rentalRepository;

  public void createMessage(MessageRequest request) {
    User user = userRepository.findById(request.getUser_id())
      .orElseThrow(() -> new BadRequestException("Utilisateur introuvable."));

    Rental rental = rentalRepository.findById(request.getRental_id())
      .orElseThrow(() -> new BadRequestException("Location introuvable."));

    Message message = new Message();

    message.setUser(user);
    message.setRental(rental);

    messageRepository.save(message);
  }
}
