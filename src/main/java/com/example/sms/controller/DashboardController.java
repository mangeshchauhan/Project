package com.example.sms.controller;
import com.example.sms.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller @RequiredArgsConstructor
public class DashboardController {
    private final StudentService service;
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("total", service.count());
        model.addAttribute("recent", service.recent());
        return "dashboard";
    }
}
