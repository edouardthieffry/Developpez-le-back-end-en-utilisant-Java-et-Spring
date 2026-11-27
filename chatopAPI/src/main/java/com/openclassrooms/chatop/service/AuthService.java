package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.dto.AuthResponse;
import com.openclassrooms.chatop.dto.LoginRequest;
import com.openclassrooms.chatop.dto.RegisterRequest;
import com.openclassrooms.chatop.dto.UserResponse;
import com.openclassrooms.chatop.exceptions.ConflictException;
import com.openclassrooms.chatop.exceptions.UnauthorizedException;
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
      throw new ConflictException("Un compte existe déjà avec cette adresse e-mail.");
    }
    User newUser = new User();
    newUser.setEmail(request.getEmail());
    newUser.setName(request.getName());
    newUser.setPassword(passwordEncoder.encode(request.getPassword()));
    userRepository.save(newUser);

    String token = jwtService.generateToken(newUser.getEmail());
    return new AuthResponse(token);
  }

  public AuthResponse login(LoginRequest request){
    User loginUser = userRepository.findByEmail(request.getEmail())
      .orElseThrow(() -> new UnauthorizedException("Adresse e-mail ou mot de passe incorrect."));
    if (!passwordEncoder.matches(request.getPassword(),loginUser.getPassword())){
      throw new UnauthorizedException("Adresse e-mail ou mot de passe incorrect.");
    }
    String token = jwtService.generateToken(loginUser.getEmail());
    return AuthResponse.builder().token(token).build();
  }

  public UserResponse getUser(String email){
    User getUser = userRepository.findByEmail(email).orElseThrow(()-> new UnauthorizedException("User not found"));;
    return UserResponse.from(getUser);
  }

}
