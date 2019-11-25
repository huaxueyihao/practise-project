package com.boot.study.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtils {

    public static final String TOKEN_HEADER = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";

    private static final String SECRET = "secret";

    private static final String ISS = "iss";

    private static final long EXPIRATION = 3600L;

    private static final long EXPRIRATION_REMEMBER = 604800L;

    public static String createToken(String username, boolean isRemeberMe) {
        long expiration = isRemeberMe ? EXPRIRATION_REMEMBER : EXPIRATION;
        return Jwts.builder().signWith(SignatureAlgorithm.HS512, SECRET)
                .setIssuer(ISS)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .compact();
    }

    public static String getUsername(String token) {
        return getTokenBody(token).getSubject();
    }

    public static boolean isExpiration(String token) {
        return getTokenBody(token).getExpiration().before(new Date());
    }

    private static Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }


}
