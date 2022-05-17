package com.sandburger.app.Controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping(value = "/{name}")
    @ApiOperation(value = "test", notes = "swagger test")
    public String TestResponse(@PathVariable("name") String name){

        return "slack test" + name;
    }

    @GetMapping(value = "/")
    @ApiOperation(value = "test2", notes = "swagger test")
    public String TestResponse2(){
        return "slack test";
    }
}
