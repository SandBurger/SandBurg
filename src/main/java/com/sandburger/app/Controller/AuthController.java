package com.sandburger.app.Controller;

import com.sandburger.app.Util.JwtUtil;
import com.sandburger.app.Util.RedisUtil;
import com.sandburger.app.model.RoleType;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private static final String REFRESH_HEADER = "REFRESH-TOKEN";
    private static final long THREE_DAYS_MSEC = 259200000; // 3일

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
//
//    @GetMapping("/oauth2login")
//    public String login() {
//        return "oauth2login";
//    }
//
//    @GetMapping("/")
//    public String loginSuccess(HttpServletRequest request, HttpServletResponse response) {
//        System.out.println(response.getHeader("Authentication"));
//        return "main";
//    }

    @GetMapping("/refresh")
    public String refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String accessToken = jwtUtil.resolveToken(request);
        if (jwtUtil.validToken(accessToken)) {
            throw new Exception("invalid access token.");
        }

        Claims accessClaims = jwtUtil.getExpiredTokenClaims(accessToken);
        if (accessClaims == null) {
            throw new AccessDeniedException("토큰이 아직 만료되지 않았습니다.");
        }

        String email = accessClaims.get("uid", String.class);
        RoleType role = RoleType.of(accessClaims.get("role", String.class));

        String refreshToken = request.getHeader(REFRESH_HEADER);

        Claims refreshClaims = jwtUtil.getExpiredTokenClaims(refreshToken);
        if (jwtUtil.validToken(refreshToken)) {
            throw new Exception("invalid refresh token");
        }

        // Redis에 있는 Refresh Token과 비교
        String emailFromRefreshToken = jwtUtil.getUid(refreshToken);
        String emailFromRedis = redisUtil.getData(refreshToken);
        if (emailFromRefreshToken.equals(emailFromRedis)) {
            // 새로운 Token 발급
            jwtUtil.createAccessToken(email, role.getCode());
        }

        Date now = new Date();
        long validTime = refreshClaims.getExpiration().getTime() - now.getTime();

        if (validTime <= THREE_DAYS_MSEC) {
            String newRefreshToken = jwtUtil.createRefreshToken(email);

            redisUtil.setDataExpire(newRefreshToken, email, JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
        }

        return accessToken;
    }
}
