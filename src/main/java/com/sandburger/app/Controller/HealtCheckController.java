package com.sandburger.app.Controller;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealtCheckController {
    @GetMapping("/")
    public ResponseEntity HealthAlive(){
        return new ResponseEntity(HttpStatus.OK);
    }
}
