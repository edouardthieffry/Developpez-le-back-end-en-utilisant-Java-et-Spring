package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.dto.MessageRequest;
import com.openclassrooms.chatop.model.Message;
import com.openclassrooms.chatop.model.Rental;
import com.openclassrooms.chatop.model.User;
import com.openclassrooms.chatop.repository.MessageRepository;
import com.openclassrooms.chatop.repository.RentalRepository;
import com.openclassrooms.chatop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final RentalRepository rentalRepository;

  public void createMessage(MessageRequest request) throws BadRequestException {
    User user = userRepository.findById(request.getUser_id())
      .orElseThrow(() -> new BadRequestException("User not found with id: "));

    Rental rental = rentalRepository.findById(request.getRental_id())
      .orElseThrow(() -> new BadRequestException("Rental not found"));

    Message message = new Message();

    message.setUser(user);
    message.setRental(rental);

    messageRepository.save(message);
  }
}
