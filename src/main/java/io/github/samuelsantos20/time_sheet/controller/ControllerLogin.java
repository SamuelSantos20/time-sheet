package io.github.samuelsantos20.time_sheet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerLogin {


    @GetMapping("/login")
    public String pageLogin() {

        return "login";

    }

}
