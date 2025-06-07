package com.example.codingCamp.prediction.dto.response;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionResponseDTO {
    private Long predictionId;
    private Long siswaId;
    private String semesterSiswa;
    private String namaSiswa;
    private Double scaledNilai;
    private Double scaledKehadiran;
    private Double scaledTugas;
    private String statusPrediksi;

}
