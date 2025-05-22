package com.fingalden.blog.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.micrometer.common.util.StringUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JWTUtil {
    private long tokenExpiration; // 令牌过期时间
    private String tokenSignKey;

    public String createToken(Long userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .setSubject(userId.toString())
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration*1000*60*60*24))
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compact();
    }

    public Long getUserId(String token) {
        if (StringUtils.isEmpty(token)) return null;
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        Integer userId = (Integer) claims.get("userId");
        return userId.longValue();
    }

    public boolean isExpiration(String token) {
        try{
            boolean isExpire = Jwts.parser()
                    .setSigningKey(tokenSignKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration().before(new Date());
            return isExpire;
        }catch(Exception e) {
            return true;
        }
    }
}