package com.example.codingCamp.profile.dto.request; 

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserRequestDTO {
    @NotNull(message = "ID user tidak boleh kosong")
    private Long id;

    @Size(max = 50, message = "Nama maksimal 50 karakter")
    private String name;

    @Size(max = 30, message = "Username maksimal 30 karakter")
    @Pattern(regexp = "^[^\\s]+$", message = "Username tidak boleh mengandung spasi")
    private String username;

    @Email(message = "Format email tidak valid")
    private String email;

    @Pattern(regexp = "^\\d{10,15}$", message = "Nomor telepon harus terdiri dari 10-15 digit angka")
    private String phone;
}

