package com.example.sms.controller;
import com.example.sms.dto.StudentDto;
import com.example.sms.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller @RequestMapping("/students") @RequiredArgsConstructor
public class StudentWebController {
    private final StudentService service;

    @GetMapping
    public String list(@RequestParam(defaultValue="0") int page,
                       @RequestParam(defaultValue="10") int size,
                       @RequestParam(required=false) String q, Model model) {
        Page<?> p = service.list(q, PageRequest.of(page, size, Sort.by("id").descending()));
        model.addAttribute("page", p);
        model.addAttribute("q", q);
        return "students/list";
    }

    @GetMapping("/new")
    public String createForm(Model model){ model.addAttribute("student", new StudentDto()); return "students/form"; }

    @PostMapping
    public String create(@Valid @ModelAttribute("student") StudentDto dto, BindingResult br){
        if (br.hasErrors()) return "students/form";
        service.create(dto); return "redirect:/students";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model){
        var s = service.getById(id);
        model.addAttribute("student", StudentDto.builder().id(s.getId())
            .firstName(s.getFirstName()).lastName(s.getLastName()).email(s.getEmail())
            .phoneNumber(s.getPhoneNumber()).course(s.getCourse()).address(s.getAddress())
            .dateOfBirth(s.getDateOfBirth()).gender(s.getGender()).build());
        return "students/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("student") StudentDto dto, BindingResult br){
        if (br.hasErrors()) return "students/form";
        service.update(id, dto); return "redirect:/students";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id){ service.delete(id); return "redirect:/students"; }
}
