package com.sandburger.app.Controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping(value = "/")
    @ApiOperation(value = "test", notes = "swagger test")
    public String TestResponse(){

        return "slack test";
    }
}
