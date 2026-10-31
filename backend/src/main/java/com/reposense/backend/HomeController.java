package com.reposense.backend;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Hello, World!";
    }

    @GetMapping("/bhavesh")
    public String bhavesh() {
        return "Hello, Bhavesh is here!";
    }
    
}
