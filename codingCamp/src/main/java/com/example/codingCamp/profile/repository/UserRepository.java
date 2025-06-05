package com.example.codingCamp.profile.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.codingCamp.profile.model.Role;
import com.example.codingCamp.profile.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByUsername(String username);
    UserModel findByEmail(String email);
    Optional<UserModel> findById(Long id);    
    List<UserModel> findAllByRole(Role role);
    Optional<UserModel> findByPhone(String phone);
    List<UserModel> findByDeletedAtIsNull();

}
