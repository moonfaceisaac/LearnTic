package com.example.codingCamp.student.controller;

import com.example.codingCamp.dto.BaseResponseDTO;
import com.example.codingCamp.student.dto.request.CreateStudentPerformanceRequestDTO;
import com.example.codingCamp.student.dto.response.StudentPerformanceResponseDTO;
import com.example.codingCamp.student.service.StudentPerformanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("api/student-performance")
public class StudentPerformanceController {

    @Autowired
    private StudentPerformanceService studentPerformanceService;

    @PostMapping("/create")
    public BaseResponseDTO<StudentPerformanceResponseDTO> createPerformance(@RequestBody CreateStudentPerformanceRequestDTO request) {
        StudentPerformanceResponseDTO response = studentPerformanceService.createPerformance(request);

        return BaseResponseDTO.<StudentPerformanceResponseDTO>builder()
                .data(response)
                .status(HttpStatus.OK.value())
                .message("Data performa siswa dengan ID " + response.getSiswaId() + " berhasil dibuat")
                .timestamp(new Date())
                .build();
    }
}
