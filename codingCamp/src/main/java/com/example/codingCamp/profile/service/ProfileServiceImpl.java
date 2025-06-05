package com.example.codingCamp.profile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.codingCamp.profile.dto.request.UpdatePasswordRequestDTO;
import com.example.codingCamp.profile.model.UserModel;
import com.example.codingCamp.profile.repository.UserRepository;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Override
    public void updatePassword(Long id, UpdatePasswordRequestDTO request) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!userService.matchesPassword(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        String hashedNewPassword = userService.hashPassword(request.getNewPassword());
        user.setPassword(hashedNewPassword);
        userRepository.save(user);
    }
}
