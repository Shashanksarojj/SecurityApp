package com.example.securityapp.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        // 🔓 Public endpoints — skip JWT validation
        if (path.contains("/api/v1/auth/login") ||
                path.contains("/api/v1/auth/register") ||
                path.contains("/api/v1/auth/refresh-token")) {

            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        // 🔒 No token → continue; @PreAuthorize will block unauthorized access
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            // Extract CLAIMS
            Claims claims = jwtUtil.extractAllClaims(token);
            String username = claims.getSubject();

            if (username != null && jwtUtil.validate(token)) {

                // Extract Role
                String role = claims.get("role", String.class);

                // Extract Permissions
                List<String> permissions =
                        claims.get("permissions", List.class);

                // Build Authorities
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

                if (permissions != null) {
                    permissions.forEach(p ->
                            authorities.add(new SimpleGrantedAuthority(p))
                    );
                }

                // Build Authentication Object
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                authorities
                        );

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (MalformedJwtException | IllegalArgumentException e) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            response.getWriter().write("""
            {
                "status": "ERROR",
                "message": "Invalid or malformed JWT token",
                "data": null,
                "path": "%s"
            }
            """.formatted(request.getRequestURI()));

            return;
        }

        chain.doFilter(request, response);
    }

}
