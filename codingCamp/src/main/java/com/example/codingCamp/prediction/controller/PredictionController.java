package com.example.codingCamp.prediction.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.codingCamp.dto.BaseResponseDTO;
import com.example.codingCamp.prediction.dto.response.PredictionResponseDTO;
import com.example.codingCamp.prediction.service.PredictionService;
import com.example.codingCamp.profile.dto.response.StudentDetailDTO;
import com.example.codingCamp.profile.dto.response.UserResponseDTO;
import com.example.codingCamp.profile.service.UserService;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/prediction")
public class PredictionController {

    @Autowired
    private  PredictionService predictionService;

    @GetMapping("/{siswaId}")
    public ResponseEntity<BaseResponseDTO<PredictionResponseDTO>> predict(@PathVariable Long siswaId) {
        BaseResponseDTO<PredictionResponseDTO> responseDTO = new BaseResponseDTO<>();
        try {
            PredictionResponseDTO result = predictionService.predict(siswaId);
            responseDTO.setStatus(HttpStatus.OK.value());
            responseDTO.setMessage("Berhasil memprediksi performa siswa");
            responseDTO.setTimestamp(new Date());
            responseDTO.setData(result);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDTO.setMessage("Gagal memprediksi performa siswa: " + e.getMessage());
            responseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
}

