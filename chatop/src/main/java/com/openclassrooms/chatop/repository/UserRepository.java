package com.openclassrooms.chatop.repository;

import org.springframework.data.repository.CrudRepository;
import com.openclassrooms.chatop.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
  Optional<User> findByEmail(String email);
  boolean existsByEmail(String email);
}
