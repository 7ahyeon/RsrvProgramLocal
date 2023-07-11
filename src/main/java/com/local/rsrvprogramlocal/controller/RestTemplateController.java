package com.local.rsrvprogramlocal.controller;

import com.local.rsrvprogramlocal.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// @RestController : JSON 형태로 객체 데이터를 반환하는 것이 주 목적
// @Controller + @ResponseBody
@RestController
public class RestTemplateController {
    @Autowired
    private final ConnectionService connectionService;

    public RestTemplateController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @PostMapping("/rsrvRequest")
    public String rsrvRequest(@RequestParam int select) {
        String responseTostring = connectionService.httpConnection(select);
        System.out.println(select);
        return responseTostring;
    }
}
