package com.example.sms.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StudentDto {
    private Long id;
    @NotBlank private String firstName;
    @NotBlank private String lastName;
    @NotBlank @Email private String email;
    @NotBlank private String phoneNumber;
    @NotBlank private String course;
    private String address;
    @NotNull @Past private LocalDate dateOfBirth;
    @NotBlank private String gender;
}
