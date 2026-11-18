package com.openclassrooms.chatop.repository;

import com.openclassrooms.chatop.model.Rental;
import org.springframework.data.repository.CrudRepository;

public interface RentalRepository extends CrudRepository<Rental, Long> {
}
