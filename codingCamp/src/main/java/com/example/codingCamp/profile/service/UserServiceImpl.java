package com.example.codingCamp.profile.service;

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
import com.example.codingCamp.profile.dto.response.TeacherDetailDTO;
import com.example.codingCamp.profile.dto.response.TeacherResponseDTO;
import com.example.codingCamp.profile.dto.response.UserResponseDTO;
import com.example.codingCamp.profile.model.Student;
import com.example.codingCamp.profile.model.Role;
import com.example.codingCamp.profile.model.UserModel;
import com.example.codingCamp.profile.repository.StudentRepository;
import com.example.codingCamp.profile.repository.RoleRepository;
import com.example.codingCamp.profile.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;


    @Autowired
    RoleRepository roleRepository;

    @Autowired
    StudentRepository StudentRepository;

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
        if ("Teacher".equalsIgnoreCase(role.getRole())) {
            user = new Teacher();
        } else if ("Student".equalsIgnoreCase(role.getRole())) {
            user = new Student(); // **Gunakan Student**
        } else if ("Parent".equalsIgnoreCase(role.getRole())) {
            user = new Parent(); // **Gunakan Student**
        }else {
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

        // Membuat DTO sesuai dengan tipe user
        if (user instanceof Teacher) {
            Teacher teacher = (Teacher) user;

            // Buat TeacherDetailDTO yang diperluas
            TeacherDetailDTO teacherDetail = new TeacherDetailDTO(
                    user.getId(),
                    user.getName(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getRole().getRole(),
                    user.getCreatedAt(),
                    user.getUpdatedAt());

        
            return teacherDetail;
        } else if (user instanceof Student) {
            Student Student = (Student) user;

            // Buat StudentResponseDTO yang diperluas
            StudentDetailDTO StudentDetail = new StudentDetailDTO(
                    user.getId(),
                    user.getName(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getRole().getRole(),
                    user.getCreatedAt(),
                    user.getUpdatedAt());


            return StudentDetail;
        } else {
            // Jika user biasa
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
        List<Student> Students;

        if (search != null && !search.trim().isEmpty()) {
            Students = StudentRepository.searchByNameEmailOrPhone(search.trim());
        } else {
            Students = StudentRepository.findByDeletedAtIsNull();
        }

        return Students.stream().map(Student -> {
            UserResponseDTO dto = new UserResponseDTO();
            dto.setId(Student.getId());
            dto.setName(Student.getName());
            dto.setUsername(Student.getUsername());
            dto.setEmail(Student.getEmail());
            dto.setPhone(Student.getPhone());
            dto.setCreatedAt(Student.getCreatedAt());
            dto.setUpdatedAt(Student.getUpdatedAt());
            dto.setRole("Student");
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getStudentById(Long id) {
        Student Student = StudentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Student tidak ditemukan"));

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(Student.getId());
        dto.setName(Student.getName());
        dto.setUsername(Student.getUsername());
        dto.setEmail(Student.getEmail());
        dto.setPhone(Student.getPhone());
        dto.setCreatedAt(Student.getCreatedAt());
        dto.setUpdatedAt(Student.getUpdatedAt());
        dto.setRole("Student");
        return dto;
    }
}
