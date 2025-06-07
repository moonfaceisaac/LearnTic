package com.example.codingCamp.prediction.dto.request;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionRequestDTO {
    private Long siswaId;
    private String semester;
    private Double rataRataNilaiAkhir;
    private Integer jumlahKehadiran;
    private Double persentaseTugas;
}
