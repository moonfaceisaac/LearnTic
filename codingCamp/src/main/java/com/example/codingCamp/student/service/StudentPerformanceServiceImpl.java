package com.example.codingCamp.student.service;

import com.example.codingCamp.profile.model.Student;
import com.example.codingCamp.profile.repository.StudentRepository;
import com.example.codingCamp.student.dto.request.CreateStudentPerformanceRequestDTO;
import com.example.codingCamp.student.dto.response.StudentPerformanceResponseDTO;
import com.example.codingCamp.student.model.StudentPerformance;
import com.example.codingCamp.student.repository.StudentPerformanceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Service
public class StudentPerformanceServiceImpl implements StudentPerformanceService {

    @Autowired
    private StudentPerformanceRepository studentPerformanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public StudentPerformanceResponseDTO createPerformance(CreateStudentPerformanceRequestDTO request) {
        Student siswa = studentRepository.findById(request.getSiswaId())
                .orElseThrow(() -> new RuntimeException("Siswa tidak ditemukan"));

        double avgUjian = avg(request.getNilaiUjianPerMapel());
        double avgTugas = avg(request.getNilaiTugasPerMapel());
        double avgKuis = avg(request.getNilaiKuisPerMapel());

        double nilaiAkhir = (avgUjian * 0.4) + (avgTugas * 0.3) + (avgKuis * 0.3);

        StudentPerformance data = new StudentPerformance();
        data.setSiswa(siswa);
        data.setNilaiUjianPerMapel(request.getNilaiUjianPerMapel());
        data.setNilaiTugasPerMapel(request.getNilaiTugasPerMapel());
        data.setNilaiKuisPerMapel(request.getNilaiKuisPerMapel());
        data.setJumlahKehadiran(request.getJumlahKehadiran());
        data.setPersentaseTugas(request.getPersentaseTugas());
        data.setNilaiAkhirRataRata(nilaiAkhir);
        data.setSubmittedForPrediction(false);
        data.setCreatedAt(new Date());
        data.setDeletedAt(request.getDeletedAt());

        StudentPerformance saved = studentPerformanceRepository.save(data);

        return toStudentPerformanceResponse(saved);
    }

    private double avg(Map<String, Double> mapel) {
        if (mapel == null || mapel.isEmpty())
            return 0.0;
        return mapel.values().stream()
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    private StudentPerformanceResponseDTO toStudentPerformanceResponse(StudentPerformance performance) {
        return new StudentPerformanceResponseDTO(
                performance.getId(),
                performance.getSiswa().getId(),
                performance.getSiswa().getName(),
                performance.getNilaiUjianPerMapel(),
                performance.getNilaiTugasPerMapel(),
                performance.getNilaiKuisPerMapel(),
                performance.getNilaiAkhirRataRata(),
                performance.getJumlahKehadiran(),
                performance.getPersentaseTugas(),
                performance.getStatusPrediksi(),
                performance.getCreatedAt(),
                performance.getDeletedAt()
        );
    }
}
