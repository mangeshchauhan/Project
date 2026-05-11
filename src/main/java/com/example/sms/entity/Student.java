package com.example.sms.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Table(name = "students")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank @Size(max=50) private String firstName;
    @NotBlank @Size(max=50) private String lastName;
    @NotBlank @Email @Column(unique=true) private String email;
    @NotBlank @Pattern(regexp="^[0-9+\\-\\s]{7,20}$") private String phoneNumber;
    @NotBlank private String course;
    @Size(max=255) private String address;
    @NotNull @Past private LocalDate dateOfBirth;
    @NotBlank private String gender;
    @Column(updatable=false) private LocalDateTime createdDate;
    @PrePersist void prePersist(){ createdDate = LocalDateTime.now(); }
}
