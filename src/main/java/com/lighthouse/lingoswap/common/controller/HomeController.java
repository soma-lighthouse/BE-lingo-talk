package com.lighthouse.lingoswap.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "Hello World\uD83E\uDEE0";
    }
}
