package com.kava.kbpd.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Kris
 * @date 2025/4/20
 * @description:
 */
@RestController
public class TestController {

    @RequestMapping("/test")
    public Map test(){
        Map map = Map.of("name", "kris");
        return map;
    }
}
