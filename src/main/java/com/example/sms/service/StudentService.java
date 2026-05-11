package com.example.sms.service;
import com.example.sms.dto.StudentDto;
import com.example.sms.entity.Student;
import com.example.sms.exception.ResourceNotFoundException;
import com.example.sms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service @RequiredArgsConstructor
public class StudentService {
    private static final Logger log = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository repo;

    public Student create(StudentDto d) {
        log.info("Creating student {}", d.getEmail());
        return repo.save(toEntity(d, new Student()));
    }
    public Student update(Long id, StudentDto d) {
        Student s = getById(id); toEntity(d, s); return repo.save(s);
    }
    public void delete(Long id) { repo.delete(getById(id)); }
    public Student getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));
    }
    public Page<Student> list(String q, Pageable p) {
        if (q == null || q.isBlank()) return repo.findAll(p);
        return repo.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrCourseContainingIgnoreCase(q,q,q,q,p);
    }
    public long count() { return repo.count(); }
    public List<Student> recent() { return repo.findTop5ByOrderByCreatedDateDesc(); }

    private Student toEntity(StudentDto d, Student s) {
        s.setFirstName(d.getFirstName()); s.setLastName(d.getLastName());
        s.setEmail(d.getEmail()); s.setPhoneNumber(d.getPhoneNumber());
        s.setCourse(d.getCourse()); s.setAddress(d.getAddress());
        s.setDateOfBirth(d.getDateOfBirth()); s.setGender(d.getGender());
        return s;
    }
}
