package de.tp82.laufometer.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class MenuController {

    @RequestMapping(value = "")
    public String index() {
        return "menu/index";
    }
}