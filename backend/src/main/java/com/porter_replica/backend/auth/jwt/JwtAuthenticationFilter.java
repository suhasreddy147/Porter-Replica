package com.porter_replica.backend.auth.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.porter_replica.backend.auth.security.CustomUserPrincipal;
import com.porter_replica.backend.auth.session.SessionService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    
    @Autowired
    private SessionService sessionService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            		throws ServletException, IOException {

    	String header = request.getHeader("Authorization");

    	if (header != null && header.startsWith("Bearer ")) {
    		String token = header.substring(7);

    		try {
    			Claims claims = jwtUtil.validateToken(token);
    			String userId = claims.getSubject();
    			String role = claims.get("role", String.class);
    			String sessionId = claims.get("sid", String.class);
    			
    			CustomUserPrincipal customUserPrincipal = 
    					new CustomUserPrincipal(
    							Long.parseLong(userId),
    							sessionId
    					);

    			UsernamePasswordAuthenticationToken auth =
    					new UsernamePasswordAuthenticationToken(
    							customUserPrincipal,
    							null,
    							List.of(new SimpleGrantedAuthority("ROLE_" + role))
    							);

    			// Update activity
    			if (sessionId != null) {
    				UUID uuid= UUID.fromString(sessionId);
    				sessionService.updateActivity(uuid);
    			}
    			
				SecurityContextHolder.getContext().setAuthentication(auth);
    		
    		}catch (Exception ignored) {
    			// Invalid token → request will be rejected
    		}
    	}

    	filterChain.doFilter(request, response);
    }
}
