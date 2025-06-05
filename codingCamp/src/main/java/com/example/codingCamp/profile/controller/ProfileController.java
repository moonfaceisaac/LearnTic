package com.example.codingCamp.profile.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.codingCamp.profile.dto.request.UpdatePasswordRequestDTO;
import com.example.codingCamp.profile.dto.request.UpdateUserRequestDTO;
import com.example.codingCamp.profile.dto.response.UserResponseDTO;
import com.example.codingCamp.profile.service.ProfileService;
import com.example.codingCamp.profile.service.UserService;

import com.example.codingCamp.auth.service.AuthService;
import com.example.codingCamp.dto.response.BaseResponseDTO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    ProfileService profileService;
    
    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    @GetMapping("")
    public ResponseEntity<?> getProfile() {
        var baseResponseDTO = new BaseResponseDTO<UserResponseDTO>();
        var userId = authService.getCurrentUserId();
        try {
            var user = userService.getUserById(userId);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Berhasil mengambil data profile");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(user);
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Gagal mengambil data profile");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateUserRequestDTO request) {
        var baseResponseDTO = new BaseResponseDTO<UserResponseDTO>();
        try {
            UserResponseDTO updatedUser = userService.updateUser(request);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Berhasil memperbarui data profile");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(updatedUser);
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch(Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Gagal mengambil data profile");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequestDTO request) {
        var baseResponseDTO = new BaseResponseDTO<>();
        var userId = authService.getCurrentUserId();
        try {
            profileService.updatePassword(userId, request);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Berhasil memperbarui password untuk id " + userId);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch(RuntimeException e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Gagal mengubah password" + e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }   
}
