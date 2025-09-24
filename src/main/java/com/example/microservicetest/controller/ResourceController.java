package com.example.microservicetest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ResourceController {

    @GetMapping("/resource")
    public List<String> getResource() {
        return Arrays.asList("hello", "world");
    }
}
