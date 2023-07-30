package com.star.webssh.common;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

/**
 * @author Mr.Wang
 * @create 2023-05-23-22:11
 */
public class JWTUtils {
    private static String signKey="champion";
    private static Long expir=360000000L;

    /**
     * 生成JWT
     */
    public static String createJWT(Map<String,Object> cliam){
        String jwt = Jwts.builder()
                .addClaims(cliam)
                .signWith(SignatureAlgorithm.HS256,signKey)
                .setExpiration(new Date(System.currentTimeMillis() + expir))
                .compact();
        return jwt;

    }

    public static Claims parseJWT(String jwt){
        Claims c = Jwts.parser()
                .setSigningKey(signKey)
                .parseClaimsJws(jwt)
                .getBody();
        return c;
    }

}
