package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.MessageRequest;
import com.openclassrooms.chatop.dto.MessageResponse;
import com.openclassrooms.chatop.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
  private final MessageService messageService;

  @PostMapping
  public ResponseEntity<MessageResponse> create(@Valid @RequestBody MessageRequest request) throws BadRequestException {
    messageService.createMessage(request);
    return ResponseEntity.ok(new MessageResponse("Message send with success"));
  }
}
