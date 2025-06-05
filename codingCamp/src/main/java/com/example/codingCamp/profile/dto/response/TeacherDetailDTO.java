package com.example.codingCamp.profile.dto.response; 

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TeacherDetailDTO extends UserResponseDTO {

    
    public TeacherDetailDTO(Long id, String name, String username, String email, 
                                   String phone, String role, Date createdAt, Date updatedAt) {
        super(id, name, username, email, phone, role, createdAt, updatedAt);
      
    }
}