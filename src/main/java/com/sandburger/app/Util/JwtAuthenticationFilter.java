package com.sandburger.app.Util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private RequestMatcher mainRequestMatcher = new AntPathRequestMatcher("/main/**");
    private RequestMatcher refreshRequestMatcher = new AntPathRequestMatcher("/refresh/**");
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!mainRequestMatcher.matches(request) && !refreshRequestMatcher.matches(request)) {
            String accessToken = jwtUtil.resolveToken(request);

            String email = null;

            try {
                if (accessToken != null) {
                    email = jwtUtil.getUid(accessToken);
                }
                if (email != null) {

                    if (jwtUtil.validToken(accessToken) || !jwtUtil.isTokenExpired(accessToken)) {
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
