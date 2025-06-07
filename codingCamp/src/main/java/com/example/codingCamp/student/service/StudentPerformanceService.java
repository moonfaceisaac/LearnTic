package com.example.codingCamp.student.service;

import java.util.List;

import com.example.codingCamp.student.dto.request.CreateStudentPerformanceRequestDTO;
import com.example.codingCamp.student.dto.request.UpdateStudentPerformanceRequestDTO;
import com.example.codingCamp.student.dto.response.StudentPerformanceResponseDTO;

public interface StudentPerformanceService {
    StudentPerformanceResponseDTO createPerformance(CreateStudentPerformanceRequestDTO request);
    StudentPerformanceResponseDTO updatePerformance(Long id, UpdateStudentPerformanceRequestDTO request);
    List<StudentPerformanceResponseDTO> getAllPerformance(String sortBy);
    StudentPerformanceResponseDTO getPerformanceById(Long id);
    void deletePerformance(Long id);

}
