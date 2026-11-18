package com.openclassrooms.chatop.repository;

import org.springframework.data.repository.CrudRepository;
import com.openclassrooms.chatop.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
