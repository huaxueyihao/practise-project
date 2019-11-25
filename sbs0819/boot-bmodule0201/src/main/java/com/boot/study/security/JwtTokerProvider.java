package com.boot.study.security;

import io.jsonwebtoken.*;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokerProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokerProvider.class);

    private String jwtSecret = "123456";

    private int jwtExpirationInMs = 3600;

    public String generateToken(Authentication authentication) {
        JwtUser userPrincipal = (JwtUser) authentication.getPrincipal();

        Date date = new Date();
        Date expiryDate = DateUtils.addSeconds(date, jwtExpirationInMs);

        return Jwts.builder().setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date()).setExpiration(expiryDate).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();

    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            ex.printStackTrace();
            LOGGER.error("Invalid JWT signature", ex);
        } catch (MalformedJwtException ex) {
            ex.printStackTrace();
            LOGGER.error("Invalid JWT token", ex);
        } catch (ExpiredJwtException ex) {
            ex.printStackTrace();
            LOGGER.error("Expired JWT token", ex);
        } catch (UnsupportedJwtException ex) {
            ex.printStackTrace();
            LOGGER.error("Unsupported JWT token", ex);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            LOGGER.error("JWT claims string is empty.", ex);
        }
        return false;

    }


}
