package com.springboot.blog.security;

import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.utils.AppUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.ExpiredJwtException;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtHelper {
    private AppUtils appUtils;

    public JwtHelper(AppUtils appUtils) {
        this.appUtils = appUtils;
    }

    @Value("${app.jwt.secret-key}")
    private String jwtSecret;

    //generate token
    public String generateToken(Authentication authentication){
        String userEmail = authentication.getName();
        Date expDate = appUtils.getJwtExpirationDate();

        String jwtToken = Jwts.builder()
                .setSubject(userEmail)
                .setIssuedAt(new Date())
                .setExpiration(expDate)
                .signWith(key())
                .compact();

        return jwtToken;
    }

    private Key key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    //get userEmail from JWT token
    public String getUserEmailFromToken(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    //validate jwt token
    public Boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
        }
    }
}
