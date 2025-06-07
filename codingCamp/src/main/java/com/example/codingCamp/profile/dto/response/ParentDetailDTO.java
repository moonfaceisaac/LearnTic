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
public class ParentDetailDTO extends UserResponseDTO {
    private String namaAnak;

    public ParentDetailDTO(Long id, String name, String username, String email, 
                           String phone, String role, Date createdAt, Date updatedAt, String namaAnak) {
        super(id, name, username, email, phone, role, createdAt, updatedAt);
        this.namaAnak = namaAnak;
    }
}
