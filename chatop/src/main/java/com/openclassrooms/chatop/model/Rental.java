package com.openclassrooms.chatop.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "rentals")
public class Rental {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;
  private Float surface;
  private Float price;
  private String picture;

  @Column(columnDefinition = "TEXT")
  private String description;

  @ManyToOne
  @JoinColumn(name = "owner_id", nullable = false)
  private User user;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
