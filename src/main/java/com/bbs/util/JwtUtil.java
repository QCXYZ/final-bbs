package com.bbs.util;

import com.bbs.entity.User;
import com.bbs.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    private static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    @Resource
    private UserService userService;

    public static String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 1000L * 3600 * 24 * 30);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public static boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            log.error("无效的JWT令牌: {}", e.getMessage());
        }
        return false;
    }

    public static String getUsername(String jwt) {
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // 去除 "Bearer " 前缀
        }
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        return claims.getSubject();
    }

    public User getCurrentUser(HttpServletRequest request) {
        String jwt = request.getHeader("Authorization");
        String username = getUsername(jwt);
        return userService.getUser(username);
    }
}
