package com.example.codingCamp.student.service;

import com.example.codingCamp.profile.model.Student;
import com.example.codingCamp.profile.repository.StudentRepository;
import com.example.codingCamp.student.dto.request.CreateStudentPerformanceRequestDTO;
import com.example.codingCamp.student.dto.request.UpdateStudentPerformanceRequestDTO;
import com.example.codingCamp.student.dto.response.StudentPerformanceResponseDTO;
import com.example.codingCamp.student.model.StudentPerformance;
import com.example.codingCamp.student.repository.StudentPerformanceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import java.util.Optional;
import java.util.Collections;
import org.springframework.data.domain.Sort;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
        data.setStudent(siswa);
        data.setNilaiUjianPerMapel(request.getNilaiUjianPerMapel());
        data.setNilaiTugasPerMapel(request.getNilaiTugasPerMapel());
        data.setNilaiKuisPerMapel(request.getNilaiKuisPerMapel());
        data.setKelas(request.getKelas());
        data.setJumlahKehadiran(request.getJumlahKehadiran());
        data.setPersentaseTugas(request.getPersentaseTugas());
        data.setNilaiAkhirRataRata(nilaiAkhir);
        data.setSubmittedForPrediction(false);
        data.setCreatedAt(new Date());
        data.setUpdatedAt(new Date());
        data.setDeletedAt(request.getDeletedAt());

        StudentPerformance saved = studentPerformanceRepository.save(data);

        return toStudentPerformanceResponse(saved);
    }


    @Override
    public StudentPerformanceResponseDTO updatePerformance(Long id, UpdateStudentPerformanceRequestDTO request) {
        StudentPerformance performance = studentPerformanceRepository.findById(id)
                .orElseThrow(() -> new NoResourceFoundException("StudentPerformance dengan ID " + id + " tidak ditemukan"));

        Student student = studentRepository.findById(request.getSiswaId())
                .orElseThrow(() -> new NoResourceFoundException("Siswa dengan ID " + request.getSiswaId() + " tidak ditemukan"));

        double avgUjian = avg(request.getNilaiUjianPerMapel());
        double avgTugas = avg(request.getNilaiTugasPerMapel());
        double avgKuis = avg(request.getNilaiKuisPerMapel());

        double nilaiAkhir = (avgUjian * 0.4) + (avgTugas * 0.3) + (avgKuis * 0.3);

        performance.setStudent(student);
        performance.setNilaiUjianPerMapel(request.getNilaiUjianPerMapel());
        performance.setNilaiTugasPerMapel(request.getNilaiTugasPerMapel());
        performance.setNilaiKuisPerMapel(request.getNilaiKuisPerMapel());
        performance.setKelas(request.getKelas());
        performance.setJumlahKehadiran(request.getJumlahKehadiran());
        performance.setPersentaseTugas(request.getPersentaseTugas());
        performance.setNilaiAkhirRataRata(nilaiAkhir);
        performance.setUpdatedAt(new Date());
        performance.setDeletedAt(request.getDeletedAt());

        studentPerformanceRepository.save(performance);

        return toStudentPerformanceResponse(performance);
    }

    @Override
    public List<StudentPerformanceResponseDTO> getAllPerformance(String sortBy) {
        String sortField = (sortBy != null && !sortBy.isEmpty()) ? sortBy : "updatedAt";
        Sort sort = Sort.by(Sort.Direction.DESC, sortField);

        List<StudentPerformance> performanceList = studentPerformanceRepository.findAll(sort);

        return performanceList.stream()
                .map(this::toStudentPerformanceResponse)
                .collect(Collectors.toList());
    }

    @Override
    public StudentPerformanceResponseDTO getPerformanceById(Long id) {
        StudentPerformance performance = studentPerformanceRepository.findById(id)
                .orElseThrow(() -> new NoResourceFoundException("StudentPerformance dengan ID " + id + " tidak ditemukan"));

        return toStudentPerformanceResponse(performance);
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
                performance.getStudent().getId(),
                performance.getStudent().getName(),
                performance.getNilaiUjianPerMapel(),
                performance.getNilaiTugasPerMapel(),
                performance.getNilaiKuisPerMapel(),
                performance.getNilaiAkhirRataRata(),
                performance.getKelas(),
                performance.getJumlahKehadiran(),
                performance.getPersentaseTugas(),
                performance.getStatusPrediksi(),
                performance.getCreatedAt(),
                performance.getDeletedAt(),
                performance.getUpdatedAt());
    }

    @Override
    public void deletePerformance(Long id) {
        StudentPerformance performance = studentPerformanceRepository.findById(id)
                .orElseThrow(() -> new NoResourceFoundException("StudentPerformance dengan ID " + id + " tidak ditemukan"));

        studentPerformanceRepository.delete(performance);
    }


}
