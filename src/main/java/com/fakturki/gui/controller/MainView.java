package com.fakturki.gui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainView {

    @GetMapping("/")
    public String mainView(Model model) {
        model.addAttribute("password", "6b851058-e14c-46c9-8348-96f05ed567de");
        return "index";
    }

    @GetMapping("/panel")
    public String panelView(Model model) {
        model.addAttribute("password", "6b851058-e14c-46c9-8348-96f05ed567de");
        return "test";
    }

    @PostMapping("/hello")
    // @PostMapping(headers = "HX-Request")
    public String furtherInfo() {
        return "fragments/invoices";
        // return "test";
    }


    
}
