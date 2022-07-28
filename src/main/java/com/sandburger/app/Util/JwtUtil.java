package com.sandburger.app.Util;

import io.jsonwebtoken.*;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Component
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORITIES_KEY = "auth";
    @Value("${jwt.secret}")
    private String secret;
    @Value(("${jwt.expiration}"))
    private Integer expiration;

    private Key getSigningKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createRefreshKey(OAuth2User oAuth2User) {
        return Jwts.builder()
                .setSubject((String) oAuth2User.getAttributes().get("sub"))
                .signWith(getSigningKey(secret), SignatureAlgorithm.HS256)
                .setExpiration(new Date(System.currentTimeMillis() + expiration*1000))
                .compact();
    }

    public String createAccessKey(String refreshKey) {
        return Jwts.builder()
                .claim("refreshKey", refreshKey)
                .setIssuedAt(new Date())
                .signWith(getSigningKey(secret), SignatureAlgorithm.HS256)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();

        Map<String, Object> attributes = getBody(token);

        String userNameAttributeName = "sub";

        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        OAuth2User principal = new DefaultOAuth2User(authorities, attributes, userNameAttributeName);

        OAuth2AuthenticationToken authenticationToken = new OAuth2AuthenticationToken(principal, authorities, userNameAttributeName);
        authenticationToken.setDetails(principal);
        return authenticationToken;
    }

    private Map<String, Object> getBody(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        try {
            final Date expire_Time = new Date(expiration);
            return expire_Time.before(new Date());
        } catch (Exception exception) {
            return false;
        }
    }
}
