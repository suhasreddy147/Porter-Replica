package com.porter_replica.auth_service.auth.security.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.porter_replica.auth_service.auth.constants.AuthConstants;
import com.porter_replica.auth_service.auth.security.jwt.util.JwtUtil;
import com.porter_replica.auth_service.auth.security.principal.CustomUserPrincipal;
import com.porter_replica.auth_service.auth.service.SessionService;

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

    	String header = request.getHeader(AuthConstants.AUTHORIZATION);

    	if (header != null && header.startsWith(AuthConstants.BEARER+AuthConstants.BLANK_SPACE_SEPARATOR)) {
    		String token = header.substring(7);

    		try {
    			Claims claims = jwtUtil.validateToken(token);
    			String userId = claims.getSubject();
    			String role = claims.get(AuthConstants.ROLE_LOWER_CASE, String.class);
    			String sessionId = claims.get(AuthConstants.SID_LOWER_CASE, String.class);
   				UUID uuid= UUID.fromString(sessionId);
    			
    			CustomUserPrincipal customUserPrincipal = 
    					new CustomUserPrincipal(
    							Long.parseLong(userId),
    							uuid, List.of(new SimpleGrantedAuthority(AuthConstants.ROLE_UNDERSCORE + role))
    					);

    			UsernamePasswordAuthenticationToken auth =
    					new UsernamePasswordAuthenticationToken(
    							customUserPrincipal,
    							null,
    							List.of(new SimpleGrantedAuthority(AuthConstants.ROLE_UNDERSCORE + role))
    							);

    			// Update activity
    			if (sessionId != null) {
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
