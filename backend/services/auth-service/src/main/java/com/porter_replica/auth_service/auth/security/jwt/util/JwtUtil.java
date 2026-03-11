package com.porter_replica.auth_service.auth.security.jwt.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.porter_replica.auth_service.auth.constants.AuthConstants;
import com.porter_replica.auth_service.auth.entity.User;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

	private final SecretKey key;

    @Value(AuthConstants.PLACEHOLDER_JWT_EXPIRATION)
    private long expiration;
    
    public JwtUtil(
            @Value(AuthConstants.PLACEHOLDER_JWT_SECRET) String secret,
            @Value(AuthConstants.PLACEHOLDER_JWT_EXPIRATION) long expiration
    ) {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        this.key = Keys.hmacShaKeyFor(decodedKey);
        this.expiration = expiration;
    }

    public String generateToken(User user, UUID sessionId) {
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim(AuthConstants.ROLE_LOWER_CASE, user.getRole().name())
                .claim(AuthConstants.SID_LOWER_CASE, sessionId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

	public Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
	            .setSigningKey(key)   //same key used for signing
	            .build()
	            .parseClaimsJws(token)
	            .getBody();
	}
}
