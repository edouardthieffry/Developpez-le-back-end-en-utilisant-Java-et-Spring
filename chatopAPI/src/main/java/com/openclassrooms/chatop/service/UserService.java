package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.openclassrooms.chatop.repository.UserRepository;
import com.openclassrooms.chatop.model.User;

import java.util.Optional;

@Data
@Service
public class UserService {
  @Autowired
  private UserRepository  userRepository;

  public Optional<User> getUser(final Integer id){
    return userRepository.findById(id);
  }

}
