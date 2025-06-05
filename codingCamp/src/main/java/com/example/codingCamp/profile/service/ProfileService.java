package com.example.codingCamp.profile.service;

import com.example.codingCamp.profile.dto.request.UpdatePasswordRequestDTO;

public interface ProfileService {
    void updatePassword(Long id, UpdatePasswordRequestDTO request);
}
