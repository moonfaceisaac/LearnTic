package com.example.codingCamp.student.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentPerformanceDTO {
    private Map<String, Double> nilaiUjian;
    private Map<String, Double> nilaiTugas;
    private Map<String, Double> nilaiKuis;
    private Integer jumlahKehadiran;
    private Double persentaseTugas;
    private Double nilaiAkhirRataRata;
    private String statusPrediksi;
}
