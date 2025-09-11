package com.dawood.finance.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue
  private Long id;

  private String fullname;

  @Column(unique = true)
  private String email;

  private String password;

  private String photoUrl;

  private Boolean isActive;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @PrePersist
  private void prePersit() {
    this.isActive = false;
  }

}
