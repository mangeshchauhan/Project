package com.example.sms.controller.api;
import com.example.sms.dto.StudentDto;
import com.example.sms.entity.Student;
import com.example.sms.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/students") @RequiredArgsConstructor
public class StudentApiController {
    private final StudentService service;

    @GetMapping
    public Page<Student> list(@RequestParam(defaultValue="0") int page,
                              @RequestParam(defaultValue="10") int size,
                              @RequestParam(required=false) String q) {
        return service.list(q, PageRequest.of(page, size, Sort.by("id").descending()));
    }

    @GetMapping("/{id}")
    public Student get(@PathVariable Long id){ return service.getById(id); }

    @PostMapping
    public ResponseEntity<Student> create(@Valid @RequestBody StudentDto dto){
        return ResponseEntity.status(201).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @Valid @RequestBody StudentDto dto){
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id); return ResponseEntity.noContent().build();
    }
}
