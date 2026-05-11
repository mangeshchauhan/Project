package com.example.sms.repository;
import com.example.sms.entity.Student;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Page<Student> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrCourseContainingIgnoreCase(
        String f, String l, String e, String c, Pageable p);
    List<Student> findTop5ByOrderByCreatedDateDesc();
}
