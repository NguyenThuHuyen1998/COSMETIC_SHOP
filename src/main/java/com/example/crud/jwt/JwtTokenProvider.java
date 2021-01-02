package com.example.crud.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import com.example.crud.entity.CustomUserDetails;


@Component
@Slf4j
public class JwtTokenProvider {
    private final String JWT_SECRET = "lodaaaaaa";
    private final long JWT_EXPIRATION = 604800000L;
    Log log= LogFactory.getLog(JwtTokenProvider.class);

    public String generateToken(CustomUserDetails userDetails) {
        // Lấy thông tin user
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        ArrayList<String> authsList = new ArrayList<>(authorities.size());

        for (GrantedAuthority authority : authorities) {
            authsList.add(authority.getAuthority());
        }
        // Tạo chuỗi json web token từ id của user.
        return Jwts.builder()
                   .setSubject(Long.toString(userDetails.getUser().getUserId()))
                    .claim("role", authsList)
                   .setIssuedAt(now)
                   .setExpiration(expiryDate)
                   .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                   .compact();
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                            .setSigningKey(JWT_SECRET)
                            .parseClaimsJws(token)
                            .getBody();

        return Long.parseLong(claims.getSubject());
    }


    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}
