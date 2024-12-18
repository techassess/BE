package com.example.sourcebase.filter;


import com.example.sourcebase.service.impl.UserService;
import com.example.sourcebase.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService authService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserService authService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = extractToken(request);
        if (jwt == null || !jwtTokenProvider.validateToken(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtTokenProvider.getUsernameFromToken(jwt);
        UserDetails userInfo = authService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userInfo, null, userInfo.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        //Bearer
        String jwtWithBarer = request.getHeader("Authorization");
        if (jwtWithBarer == null) return null;
        return jwtWithBarer.substring(7);
    }


}