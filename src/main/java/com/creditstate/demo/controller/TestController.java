package com.creditstate.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weiming.zhu
 * @date 2020/4/26 14:03
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/testRequest")
    public String testRequest() {
        return "success";
    }
}

