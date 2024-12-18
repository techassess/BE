package com.example.sourcebase.util;


import com.example.sourcebase.domain.Rank;
import com.example.sourcebase.domain.dto.resdto.FileInfoResDTO;
import com.example.sourcebase.domain.dto.resdto.user.UserProjectResDTO;
import com.example.sourcebase.domain.dto.resdto.user.UserRoleResDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;


    public String generateToken(Long id , String fullName, String urlImage, String email, String phoneNumber, String username, Rank rank, List<UserRoleResDTO> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .claim("id", id)
                .claim("fullName", fullName)
                .claim("urlImage", urlImage)
                .claim("email", email)
                .claim("phone", phoneNumber)
                .claim("username", username)
                .claim("rank", rank)
                .claim("roles", roles)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes())).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
