package com.example.codingCamp.service;




import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.codingCamp.security.jwt.JwtUtils;

@Service
public class AuthService {
    
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtUtils jwtUtils;

    public Long getCurrentUserId() {
        String token = parseJwt(request);
        if (token != null && jwtUtils.validateJwtToken(token)) {
            String idString = jwtUtils.getClaimFromJwtToken(token, "id");
            return Long.parseLong(idString);
        }
        return null;
    }

    public String getCurrentUserRole() {
        String token = parseJwt(request);
        if (token != null && jwtUtils.validateJwtToken(token)) {
            return jwtUtils.getClaimFromJwtToken(token, "role");
        }
        return null;
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Remove "Bearer "
        }
        return null;
    }
}
