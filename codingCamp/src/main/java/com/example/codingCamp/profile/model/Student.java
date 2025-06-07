package com.example.codingCamp.profile.model;

import com.example.codingCamp.student.model.StudentPerformance;
import com.example.codingCamp.profile.model.Parent;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("STUDENT")
public class Student extends UserModel {

    // Relasi ke daftar nilai performa
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentPerformance> daftarNilai;

    // Relasi ke orang tua
    @OneToOne(mappedBy = "anak", fetch = FetchType.LAZY)
    private Parent orangTua;

    private String kelas;
    private String semester;



}
