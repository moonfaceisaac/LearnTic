package com.example.codingCamp.student.dto.request;



import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentPerformanceRequestDTO {
    @NotNull(message = "siswaId tidak boleh null")
    private Long siswaId;

    private Map<String, Double> nilaiUjianPerMapel;
    private Map<String, Double> nilaiTugasPerMapel;
    private Map<String, Double> nilaiKuisPerMapel;

    private Integer jumlahKehadiran;
    private Double persentaseTugas;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date deletedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date updatedAt;
}
