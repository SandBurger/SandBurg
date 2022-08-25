package com.sandburger.app.Controller;

import com.sandburger.app.Util.CommonResponse;
import com.sandburger.app.Util.JwtUtil;
import com.sandburger.app.Util.RedisUtil;
import com.sandburger.app.model.RoleType;
import com.sandburger.app.model.Token;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private static final String REFRESH_HEADER = "REFRESH-TOKEN";
    private static final long THREE_DAYS_MSEC = 259200000; // 3일

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    @GetMapping("/refresh")
    @ApiOperation(value = "get tokens by old access token and old refresh token", notes = "put tokens in header (Authorization: Bearer {access-token}, REFRESH-TOKEN: {refresh-token})")
    public CommonResponse<Token> refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Token token = new Token();
        Boolean isAccessTokenExpired = false;
        Boolean isRefreshTokenExpired = false;

        String accessToken = jwtUtil.resolveToken(request);

        Claims accessClaims = jwtUtil.getExpiredTokenClaims(accessToken);
        if (accessClaims == null) {
            log.info("만료되지 않은 access 토큰입니다.");
        } else { // access 토큰 만료
            isAccessTokenExpired = true;
        }

        String email = accessClaims.get("uid", String.class);
        RoleType role = RoleType.of(accessClaims.get("role", String.class));

        String refreshToken = request.getHeader(REFRESH_HEADER);

        Claims refreshClaims = jwtUtil.getExpiredTokenClaims(refreshToken);
        if (refreshClaims == null) {
            log.info("만료되지 않은 refresh 토큰입니다.");
        } else { // refresh 토큰 만료
            isRefreshTokenExpired = true;
        }

        // 둘 다 만료 안된 경우
        if (!isAccessTokenExpired && !isRefreshTokenExpired) {
            token.setAccessToken(accessToken);
            token.setRefreshToken(refreshToken);
            return new CommonResponse<>(token, HttpStatus.OK);
        }

        // Redis에 있는 Refresh Token과 비교
        String emailFromRefreshToken;
        if (!isRefreshTokenExpired) {
            emailFromRefreshToken = jwtUtil.getUid(refreshToken);
        } else {
            emailFromRefreshToken = jwtUtil.getExpiredTokenClaims(refreshToken).get("uid", String.class);
        }
        String emailFromRedis = redisUtil.getData(refreshToken);
        if (emailFromRefreshToken.equals(emailFromRedis)) {
            if (isAccessTokenExpired) {
                // 새로운 Token 발급
                token.setAccessToken(jwtUtil.createAccessToken(email, role.getCode()));
            } else {
                token.setAccessToken(accessToken);
            }
        }

        Date now = new Date();
        long validTime = refreshClaims.getExpiration().getTime() - now.getTime();

        if (validTime <= THREE_DAYS_MSEC) {
            if (isRefreshTokenExpired) {
                String newRefreshToken = jwtUtil.createRefreshToken(email);
                token.setRefreshToken(newRefreshToken);
                redisUtil.setDataExpire(newRefreshToken, email, JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
            } else {
                token.setRefreshToken(refreshToken);
            }
        }

        return new CommonResponse<>(token, HttpStatus.OK);
    }
}
