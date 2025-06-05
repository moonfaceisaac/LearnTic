package com.example.codingCamp.security.entrypoint;



import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.codingCamp.dto.BaseResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint{

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {
        var baseResponseDTO = new BaseResponseDTO<>();
        baseResponseDTO.setStatus(HttpStatus.UNAUTHORIZED.value());
        baseResponseDTO.setMessage("Unauthorized");
        baseResponseDTO.setTimestamp(new java.util.Date());
        
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(baseResponseDTO);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.getWriter().write(jsonString);
    }
    
}
