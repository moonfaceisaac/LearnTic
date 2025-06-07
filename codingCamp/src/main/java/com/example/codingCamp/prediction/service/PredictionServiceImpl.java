package com.example.codingCamp.prediction.service;

import com.example.codingCamp.prediction.dto.response.PredictionResponseDTO;
import com.example.codingCamp.prediction.model.Prediction;
import com.example.codingCamp.prediction.repository.PredictionRepository;
import com.example.codingCamp.profile.model.Student;
import com.example.codingCamp.profile.repository.StudentRepository;
import com.example.codingCamp.student.model.StudentPerformance;
import com.example.codingCamp.student.repository.StudentPerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PredictionServiceImpl implements PredictionService {

    @Autowired
    private StudentPerformanceRepository performanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PredictionRepository predictionRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String flaskApiUrl = "http://localhost:5000/predict";

    private double meanNilai;
    private double stdNilai;

    private double meanKehadiran;
    private double stdKehadiran;

    private double meanTugas;
    private double stdTugas;

    @Override
    public PredictionResponseDTO predict(Long siswaId) {
        // Update statistik mean dan std sebelum scaling
        updateStatistics();

        StudentPerformance performance = performanceRepository
                .findTopByStudent_IdAndDeletedAtIsNullOrderByCreatedAtDesc(siswaId)
                .orElseThrow(() -> new RuntimeException("Data performa siswa tidak ditemukan"));

        Student siswa = studentRepository.findById(siswaId)
                .orElseThrow(() -> new RuntimeException("Siswa tidak ditemukan"));

        // Scaling
        double scaledNilai = (performance.getNilaiAkhirRataRata() - meanNilai) / stdNilai;
        double scaledKehadiran = (performance.getJumlahKehadiran() - meanKehadiran) / stdKehadiran;
        double scaledTugas = (performance.getPersentaseTugas() - meanTugas) / stdTugas;

        // Kirim ke Flask API
        Map<String, Object> payload = new HashMap<>();
        payload.put("rataRataNilaiAkhir", scaledNilai);
        payload.put("jumlahKehadiran", scaledKehadiran);
        payload.put("persentaseTugas", scaledTugas);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        String predictionResult = "UNKNOWN";
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(flaskApiUrl, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                predictionResult = (String) response.getBody().get("prediction");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Prediction prediction = Prediction.builder()
                .siswaId(siswa.getId())
                .namaSiswa(siswa.getName())
                .semesterSiswa(siswa.getSemester())
                .scaledNilai(scaledNilai)
                .scaledKehadiran(scaledKehadiran)
                .scaledTugas(scaledTugas)
                .statusPrediksi(predictionResult)
                .createdAt(new Date())
                .build();

        predictionRepository.save(prediction);

        return toPredictionResponseDTO(prediction, siswa, scaledNilai, scaledKehadiran, scaledTugas);
    }

    private PredictionResponseDTO toPredictionResponseDTO(
            Prediction prediction,
            Student siswa,
            double scaledNilai,
            double scaledKehadiran,
            double scaledTugas) {
        PredictionResponseDTO response = new PredictionResponseDTO();
        response.setPredictionId(prediction.getId());
        response.setSiswaId(siswa.getId());
        response.setNamaSiswa(siswa.getName());
        response.setSemesterSiswa(siswa.getSemester());
        response.setScaledNilai(scaledNilai);
        response.setScaledKehadiran(scaledKehadiran);
        response.setScaledTugas(scaledTugas);
        response.setStatusPrediksi(prediction.getStatusPrediksi());
        return response;
    }

    private double mean(List<Double> values) {
        if (values.isEmpty())
            return 0;
        double sum = 0;
        for (Double v : values)
            sum += v;
        return sum / values.size();
    }

    private double stddev(List<Double> values, double mean) {
        if (values.size() < 2)
            return 1; // supaya tidak dibagi nol, set std minimal 1
        double varianceSum = 0;
        for (Double v : values) {
            varianceSum += Math.pow(v - mean, 2);
        }
        return Math.sqrt(varianceSum / (values.size() - 1));
    }

    private void updateStatistics() {
        List<StudentPerformance> allPerformances = performanceRepository.findAll();

        List<Double> nilaiList = allPerformances.stream()
                .map(StudentPerformance::getNilaiAkhirRataRata)
                .toList();

        List<Double> kehadiranList = allPerformances.stream()
                .map(sp -> sp.getJumlahKehadiran().doubleValue())
                .toList();

        List<Double> tugasList = allPerformances.stream()
                .map(StudentPerformance::getPersentaseTugas)
                .toList();

        meanNilai = mean(nilaiList);
        stdNilai = stddev(nilaiList, meanNilai);

        meanKehadiran = mean(kehadiranList);
        stdKehadiran = stddev(kehadiranList, meanKehadiran);

        meanTugas = mean(tugasList);
        stdTugas = stddev(tugasList, meanTugas);
    }
}
