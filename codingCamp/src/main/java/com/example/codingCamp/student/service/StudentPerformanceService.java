package com.example.codingCamp.student.service;

import com.example.codingCamp.student.dto.request.CreateStudentPerformanceRequestDTO;
import com.example.codingCamp.student.dto.response.StudentPerformanceResponseDTO;

public interface StudentPerformanceService {
    StudentPerformanceResponseDTO createPerformance(CreateStudentPerformanceRequestDTO request);
}
