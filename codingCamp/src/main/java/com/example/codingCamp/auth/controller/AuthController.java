package com.example.codingCamp.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.codingCamp.auth.dto.request.LoginRequest;
import com.example.codingCamp.auth.dto.response.LoginResponse;
import com.example.codingCamp.dto.BaseResponseDTO;
import com.example.codingCamp.profile.model.UserModel;
import com.example.codingCamp.profile.service.UserService;
import com.example.codingCamp.security.jwt.JwtUtils;

import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequest requestDTO) {
        var baseResponseDTO = new BaseResponseDTO<LoginResponse>();
        try {
            // Ubah getEmailOrUsername ke getUsername sesuai LoginRequest
            authenticate(requestDTO.getEmailOrUsername(), requestDTO.getPassword());
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Login Berhasil");
            baseResponseDTO.setTimestamp(new Date());
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.FORBIDDEN.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.FORBIDDEN);
        }

        UserModel user = userService.findUserByEmailOrUsername(requestDTO.getEmailOrUsername());

        final String token = jwtUtils.generateJwtToken(
                                user.getUsername(), 
                                user.getId().toString(),
                                user.getRole().getRole()
                             );

        // Buat LoginResponse dengan semua field agar sesuai constructor
        baseResponseDTO.setData(new LoginResponse(token, user.getRole().getRole(), user.getId()));
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    private void authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        UserModel user = userService.findUserByEmailOrUsername(username);

        if (user == null) {
            throw new Exception("USER_NOT_FOUND");
        }

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(encoder);
        AuthenticationManager authenticationManager = new ProviderManager(authenticationProvider);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (Exception e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
