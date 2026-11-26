package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.UserResponse;
import com.openclassrooms.chatop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id) {

    UserResponse userResponse = UserResponse.from(userService.getUser(id).orElseThrow(()-> new RuntimeException("Utilisateur non trouvé")));;
    return ResponseEntity.ok(userResponse);
  }
}
