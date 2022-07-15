package com.sandburger.app.Controller;

import com.sandburger.app.Service.AuthService;
import com.sandburger.app.model.Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;


    @GetMapping(value = "/{provider}")
    public void getProvider(@PathVariable(name = "provider") Provider provider) {
        log.info(">> 사용자로부터 SNS 로그인 요청을 받음 :: {} Social Login", provider);
        authService.request(provider);
    }

    @GetMapping(value = "/{provider}/callback")
    public String getCallback(
            @PathVariable(name = "provider") Provider provider,
            @RequestParam(name = "code") String code) {
        log.info(">> 소셜 로그인 API 서버로부터 받은 code :: {}", code);
        return authService.requestAccessToken(provider, code);
    }
}
