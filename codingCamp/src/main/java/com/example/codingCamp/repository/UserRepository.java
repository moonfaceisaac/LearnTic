package com.example.codingCamp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.codingCamp.model.Role;
import com.example.codingCamp.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    Optional<User> findById(Long id);    
    List<User> findAllByRole(Role role);
    List<User> findByDeletedAtIsNull();

}
