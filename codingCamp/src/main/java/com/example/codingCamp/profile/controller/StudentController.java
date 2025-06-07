package com.example.codingCamp.profile.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.codingCamp.dto.BaseResponseDTO;
import com.example.codingCamp.profile.dto.response.StudentDetailDTO;
import com.example.codingCamp.profile.dto.response.UserResponseDTO;
import com.example.codingCamp.profile.service.UserService;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<BaseResponseDTO<List<StudentDetailDTO>>> getAllStudent(@RequestParam(value = "search", required = false) String search) {
        BaseResponseDTO<List<StudentDetailDTO>> baseResponseDTO = new BaseResponseDTO<>();
        try {
            List<StudentDetailDTO> students = userService.getAllStudent(search);

            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Berhasil mengambil data student");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(students);

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Gagal mengambil data student");
            baseResponseDTO.setTimestamp(new Date());

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        var baseResponseDTO = new BaseResponseDTO<UserResponseDTO>();
        try {
            UserResponseDTO student = userService.getStudentById(id);

            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Berhasil mengambil detail student");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(student);

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage("student tidak ditemukan");
            baseResponseDTO.setTimestamp(new Date());

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Gagal mengambil detail student");
            baseResponseDTO.setTimestamp(new Date());

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
