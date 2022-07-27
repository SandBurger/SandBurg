package com.sandburger.app.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping("/oauth2-login")
    public String login() {
        return "oauth2-login";
    }
}
