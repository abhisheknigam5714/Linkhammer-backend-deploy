package com.Link.security.jwt;


import java.security.Key;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;

import com.Link.service.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;


@Component
public class JwtUtils {
	
	@Value("${jwt.secret}")
	private String jwtSecret;
	
	
	@Value("${jwt.expiration}")
	private Long jwtExpiration;

	public String getJwtFromHeader(HttpServletRequest request) {
		//Authorization of Bearer token
		String bearerToken = request.getHeader("Authorization");
		if(bearerToken!=null && bearerToken.startsWith("Bearer ")) {
		return bearerToken.substring(7);
		}
		return null;
	}

    public String generateToken(UserDetailsImpl userDetailImpl) {

        String userName = userDetailImpl.getUsername();
        String role = userDetailImpl.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .subject(userName)
                .claim("roles", role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key())
                .compact();
    }

    //secret Key
	private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }


    public String getUserNameFromJwtToken(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();

    }


    public boolean validateToken(String authToken) throws Exception {
        try {
            Jwts.parser().verifyWith((SecretKey) key())
                    .build().parseSignedClaims(authToken);
        } catch (JwtException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        catch (Exception e){
            throw new Exception(e);
        }
        return true;
    }
}
