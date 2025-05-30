package com.example.codingCamp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentResponseDTO {
    private Long id;
    private String name;
    private String grade;
}
