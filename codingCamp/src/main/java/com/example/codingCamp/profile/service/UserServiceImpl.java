package com.example.codingCamp.profile.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.codingCamp.profile.dto.request.AddUserRequestDTO;
import com.example.codingCamp.profile.dto.request.UpdateUserRequestDTO;
import com.example.codingCamp.profile.dto.response.StudentDetailDTO;
import com.example.codingCamp.profile.dto.response.ParentDetailDTO;
import com.example.codingCamp.profile.dto.response.TeacherResponseDTO;
import com.example.codingCamp.profile.dto.response.UserResponseDTO;
import com.example.codingCamp.profile.model.Student;
import com.example.codingCamp.profile.model.Parent;
import com.example.codingCamp.profile.model.Role;
import com.example.codingCamp.profile.model.UserModel;
import com.example.codingCamp.profile.repository.StudentRepository;
import com.example.codingCamp.profile.repository.RoleRepository;
import com.example.codingCamp.profile.repository.UserRepository;
import com.example.codingCamp.student.dto.response.StudentPerformanceDTO;
import com.example.codingCamp.student.model.StudentPerformance;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserModel findUserByUsername(String username) {
        UserModel user = userRepository.findByUsername(username);
        return user;
    }

    @Override
    public UserModel findUserByEmail(String email) {
        UserModel user = userRepository.findByEmail(email);
        return user;
    }

    @Override
    public UserModel findUserByEmailOrUsername(String emailOrUsername) {
        UserModel user = userRepository.findByEmail(emailOrUsername);
        if (user == null) {
            user = userRepository.findByUsername(emailOrUsername);
        }
        return user;
    }

    @Override
    public String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean matchesPassword(String rawPassword, String hashedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(rawPassword, hashedPassword);
    }

    @Override
    public UserResponseDTO addUser(AddUserRequestDTO userDTO) {
        // Cek apakah username, email, atau phone sudah digunakan
        if (userDTO.getUsername().contains(" ")) {
            throw new RuntimeException("Username tidak boleh mengandung spasi");
        }

        if (!userDTO.getPhone().matches("^\\d{10,15}$")) {
            throw new RuntimeException("Nomor telepon harus terdiri dari 10-15 digit angka");
        }

        if (userRepository.findByUsername(userDTO.getUsername()) != null) {
            throw new RuntimeException("Username sudah digunakan");
        }
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new RuntimeException("Email sudah digunakan");
        }
        if (userRepository.findByPhone(userDTO.getPhone()).isPresent()) {
            throw new RuntimeException("Nomor telepon sudah digunakan");
        }

        // Cari role berdasarkan nama
        Role role = roleRepository.findByRole(userDTO.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role tidak ditemukan"));

        // Jika role adalah Teacher, buat instance Teacher
        UserModel user;
        if ("Student".equalsIgnoreCase(role.getRole())) {
            user = new Student();
        } else if ("Parent".equalsIgnoreCase(role.getRole())) {
            user = new Parent(); // **Gunakan Student**
        } else {
            user = new UserModel();
        }

        // Set data user
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Hash password
        user.setRole(role);

        // Simpan ke database
        UserModel newUser = userRepository.save(user);

        // Konversi ke DTO Response
        return new UserResponseDTO(
                newUser.getId(),
                newUser.getName(),
                newUser.getUsername(),
                newUser.getEmail(),
                newUser.getPhone(),
                newUser.getRole().getRole(),
                newUser.getCreatedAt(),
                newUser.getUpdatedAt());
    }

    @Override
    public UserResponseDTO updateUser(UpdateUserRequestDTO userDTO) {
        // Cari user berdasarkan ID
        UserModel user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        // Update hanya jika field tidak null dan ada perubahan
        if (userDTO.getName() != null && !userDTO.getName().equals(user.getName())) {
            user.setName(userDTO.getName());
        }

        if (userDTO.getUsername() != null && !userDTO.getUsername().equals(user.getUsername())) {
            if (userDTO.getUsername().contains(" ")) {
                throw new RuntimeException("Username tidak boleh mengandung spasi");
            }
            if (userRepository.findByUsername(userDTO.getUsername()) != null) {
                throw new RuntimeException("Username sudah digunakan");
            }
            user.setUsername(userDTO.getUsername());
        }

        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail())) {
            if (!userDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new RuntimeException("Format email tidak valid");
            }
            if (userRepository.findByEmail(userDTO.getEmail()) != null) {
                throw new RuntimeException("Email sudah digunakan");
            }
            user.setEmail(userDTO.getEmail());
        }

        if (userDTO.getPhone() != null && !userDTO.getPhone().equals(user.getPhone())) {
            if (!userDTO.getPhone().matches("^\\d{10,15}$")) {
                throw new RuntimeException("Nomor telepon harus terdiri dari 10-15 digit angka");
            }
            if (userRepository.findByPhone(userDTO.getPhone()).isPresent()) {
                throw new RuntimeException("Nomor telepon sudah digunakan");
            }
            user.setPhone(userDTO.getPhone());
        }

        // **Tidak mengupdate password dan role**

        // Simpan perubahan
        UserModel updatedUser = userRepository.save(user);

        // Konversi ke DTO Response
        return new UserResponseDTO(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getUsername(),
                updatedUser.getEmail(),
                updatedUser.getPhone(),
                updatedUser.getRole().getRole(),
                updatedUser.getCreatedAt(),
                updatedUser.getUpdatedAt());
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        Optional<UserModel> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            return null;
        }

        UserModel user = userOptional.get();

        if (user instanceof Parent) {
            Parent parent = (Parent) user;
            String namaAnak = parent.getAnak() != null ? parent.getAnak().getName() : null;

            return new ParentDetailDTO(
                    parent.getId(),
                    parent.getName(),
                    parent.getUsername(),
                    parent.getEmail(),
                    parent.getPhone(),
                    parent.getRole().getRole(),
                    parent.getCreatedAt(),
                    parent.getUpdatedAt(),
                    namaAnak);

        } else if (user instanceof Student student) {
            List<StudentPerformanceDTO> daftarNilaiDTO = student.getDaftarNilai().stream()
                    .map(perf -> new StudentPerformanceDTO(
                            perf.getKelas(),
                            perf.getNilaiUjianPerMapel(),
                            perf.getNilaiTugasPerMapel(),
                            perf.getNilaiKuisPerMapel(),
                            perf.getJumlahKehadiran(),
                            perf.getPersentaseTugas(),
                            perf.getNilaiAkhirRataRata(),
                            perf.getStatusPrediksi()))
                    .toList();

            return new StudentDetailDTO(
                    student.getId(),
                    student.getName(),
                    student.getUsername(),
                    student.getEmail(),
                    student.getPhone(),
                    student.getRole().getRole(),
                    student.getCreatedAt(),
                    student.getUpdatedAt(),
                    daftarNilaiDTO);
        } else {
            // User umum (bukan parent/student)
            return new UserResponseDTO(
                    user.getId(),
                    user.getName(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getRole().getRole(),
                    user.getCreatedAt(),
                    user.getUpdatedAt());
        }
    }

    @Override
    public UserResponseDTO deleteUser(Long userId) {
        // Cek apakah user ada
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        // Cek apakah user sudah dihapus sebelumnya
        if (user.getDeletedAt() != null) {
            throw new RuntimeException("User telah dihapus sebelumnya");
        }

        // Soft delete dengan mengupdate kolom deletedAt
        user.setDeletedAt(new Date());
        UserModel deletedUser = userRepository.save(user);

        // Konversi ke DTO Response
        return new UserResponseDTO(
                deletedUser.getId(),
                deletedUser.getName(),
                deletedUser.getUsername(),
                deletedUser.getEmail(),
                deletedUser.getPhone(),
                deletedUser.getRole().getRole(),
                deletedUser.getCreatedAt(),
                deletedUser.getUpdatedAt());
    }

    @Override
    public List<UserResponseDTO> getAllUsers(Long id, String name, String email, String role) {
        // Get all users that haven't been deleted
        List<UserModel> users = userRepository.findByDeletedAtIsNull();

        // Apply filters if provided
        List<UserModel> filteredUsers = users.stream()
                .filter(user -> id == null || user.getId().equals(id))
                .filter(user -> name == null || user.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(user -> email == null || user.getEmail().toLowerCase().contains(email.toLowerCase()))
                .filter(user -> role == null || user.getRole().getRole().toLowerCase().contains(role.toLowerCase()))
                .collect(Collectors.toList());

        // Convert to DTOs
        return filteredUsers.stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getRole().getRole(),
                        user.getCreatedAt(),
                        user.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDTO> getAllStudent(String search) {
        List<Student> students;

        if (search != null && !search.trim().isEmpty()) {
            students = studentRepository.searchByNameEmailOrPhone(search.trim());
        } else {
            students = studentRepository.findByDeletedAtIsNull();
        }

        return students.stream().map(student -> {
            UserResponseDTO dto = new UserResponseDTO();
            dto.setId(student.getId());
            dto.setName(student.getName());
            dto.setUsername(student.getUsername());
            dto.setEmail(student.getEmail());
            dto.setPhone(student.getPhone());
            dto.setCreatedAt(student.getCreatedAt());
            dto.setUpdatedAt(student.getUpdatedAt());
            dto.setRole("Student");
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public StudentDetailDTO getStudentById(Long id) {
        Student student = studentRepository.findWithDaftarNilaiById(id)
                .orElseThrow(() -> new NoSuchElementException("Student tidak ditemukan"));

        List<StudentPerformanceDTO> daftarNilaiDTO = student.getDaftarNilai().stream()
                .map(perf -> new StudentPerformanceDTO(
                        perf.getKelas(),
                        perf.getNilaiUjianPerMapel(),
                        perf.getNilaiTugasPerMapel(),
                        perf.getNilaiKuisPerMapel(),
                        perf.getJumlahKehadiran(),
                        perf.getPersentaseTugas(),
                        perf.getNilaiAkhirRataRata(),
                        perf.getStatusPrediksi()))
                .collect(Collectors.toList());

        return new StudentDetailDTO(
                student.getId(),
                student.getName(),
                student.getUsername(),
                student.getEmail(),
                student.getPhone(),
                "Student",
                student.getCreatedAt(),
                student.getUpdatedAt(),
                daftarNilaiDTO);
    }

}
