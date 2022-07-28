package com.sandburger.app.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class TestController {
    @GetMapping("/oauth2login")
    public String login() {
        return "oauth2login";
    }

    @GetMapping("/")
    public String loginSuccess(HttpServletRequest request, HttpServletResponse response) {
        System.out.println(response.getHeader("Authentication"));
        return "main";
    }
}
