package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.dto.AuthResponse;
import com.openclassrooms.chatop.dto.RegisterRequest;
import com.openclassrooms.chatop.model.User;
import com.openclassrooms.chatop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JWTService jwtService;

  public AuthResponse register(RegisterRequest request){
    if (userRepository.existsByEmail(request.getEmail())){
      //send error
    }
    User newUser = new User();
    newUser.setEmail(request.getEmail());
    newUser.setName(request.getName());
    newUser.setPassword(passwordEncoder.encode(passwordEncoder.encode(request.getPassword())));
    userRepository.save(newUser);

    String token = jwtService.generateToken(newUser.getEmail());
    return new AuthResponse(token);
  }

}
