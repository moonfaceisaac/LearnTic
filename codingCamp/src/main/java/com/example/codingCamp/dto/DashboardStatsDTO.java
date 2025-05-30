package com.example.codingCamp.dto;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DashboardStatsDTO {
    private Long totalStudents;
    private Long totalTeachers;
    private Long totalParents;
    private Double averageScore;
    private Long presentToday;
}
