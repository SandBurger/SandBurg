package com.sandburger.app.Util;

import com.sandburger.app.DTO.UserDTO;
import com.sandburger.app.Entity.UserEntity;
import com.sandburger.app.model.Token;
import io.jsonwebtoken.*;

import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtil {
    public final static long ACCESS_TOKEN_VALIDATION_SECOND = 1000L * 60 * 30;
    public final static long REFRESH_TOKEN_VALIDATION_SECOND = 1000L * 60 * 24 * 2;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";
    private static final String AUTHORITIES_KEY = "role";
    @Value("${jwt.secret}")
    private String secret;

    private Key getSigningKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public Token generateJWT(String uid, String role) {
        String refreshToken = createRefreshToken(uid);
        String accessToken = createAccessToken(uid, role);
        return new Token(
                accessToken,
                refreshToken
        );
    }

    public String createRefreshToken(String uid) {
        Claims claims = Jwts.claims();
        claims.put("uid", uid);

        return createToken(claims, REFRESH_TOKEN_VALIDATION_SECOND);
    }

    public String createAccessToken(String uid, String role) {
        Claims claims = Jwts.claims();
        claims.put("uid", uid);
        claims.put("role", role);

        return createToken(claims, ACCESS_TOKEN_VALIDATION_SECOND);
    }

    public String createToken(Claims claims, long expire) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .signWith(getSigningKey(secret), SignatureAlgorithm.HS256)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String headerValue = request.getHeader(AUTHORIZATION_HEADER);

        if (headerValue == null) {
            return null;
        }

        if (headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getBody(token);
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{claims.get(AUTHORITIES_KEY).toString()})
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User((String) claims.get("uid"), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "",
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    private Claims getBody(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return null;
    }

    public boolean isTokenExpired(String token) {
        final Date expiration = getBody(token).getExpiration();
        return expiration.before(new Date());
    }

    public String getUid(String accessToken) {
        return getBody(accessToken).get("uid", String.class);
    }

    public boolean validToken(String accessToken) {

        return this.getBody(accessToken) != null;
    }

    public Claims getExpiredTokenClaims(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException exception) {
            log.info("만료된 토큰");
            return exception.getClaims();
        }
        return null;
    }
}
