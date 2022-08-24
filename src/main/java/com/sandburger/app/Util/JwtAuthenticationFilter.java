package com.sandburger.app.Util;

import com.sandburger.app.Entity.UserEntity;
import com.sandburger.app.Repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/main/**");
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!requestMatcher.matches(request)) {
            String accessToken = jwtUtil.resolveToken(request);

            String email = null;

            try {
                if (accessToken != null) {
                    email = jwtUtil.getUid(accessToken);
                }
                if (email != null) {
//                    UserEntity savedUser = userRepository.findByEmail(email);
//                    if (savedUser == null) {
//                        throw new UsernameNotFoundException("해당 이메일로 가입된 사용자가 없습니다.");
//                    }

                    if (jwtUtil.validToken(accessToken)) {
                        Authentication authentication = jwtUtil.getAuthentication(accessToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (Exception exception) {
                exception.getMessage();
            }
        }

        filterChain.doFilter(request, response);
    }
}
