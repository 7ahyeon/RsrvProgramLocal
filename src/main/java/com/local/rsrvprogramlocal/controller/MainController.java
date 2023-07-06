package com.local.rsrvprogramlocal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @RequestMapping(value = {"/", "main", "main.do"})
    public String main () {
        return "main";
    }
}
