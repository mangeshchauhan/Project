package model;

/**
 * ─────────────────────────────────────────────────────────────────────────────
 * CLASS: Person  (Base / Parent Class)
 * ─────────────────────────────────────────────────────────────────────────────
 * Demonstrates INHERITANCE – Student extends Person.
 * Holds common attributes shared by any person in the system.
 * ─────────────────────────────────────────────────────────────────────────────
 */
class Person {

    // Private fields → ENCAPSULATION (data hiding)
    private String name;
    private int    age;

    /**
     * Parameterized constructor for Person.
     *
     * @param name Full name of the person
     * @param age  Age of the person
     */
    public Person(String name, int age) {
        this.name = name;
        this.age  = age;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────

    /** Returns the name of the person. */
    public String getName() { return name; }

    /** Updates the name of the person. */
    public void setName(String name) { this.name = name; }

    /** Returns the age of the person. */
    public int getAge() { return age; }

    /** Updates the age of the person. */
    public void setAge(int age) { this.age = age; }

    /**
     * Returns a basic string representation of a Person.
     * Overridden in Student (POLYMORPHISM).
     */
    @Override
    public String toString() {
        return "Name: " + name + " | Age: " + age;
    }
}


/**
 * ─────────────────────────────────────────────────────────────────────────────
 * CLASS: Student  (Child / Sub-class)
 * ─────────────────────────────────────────────────────────────────────────────
 * Extends Person → demonstrates INHERITANCE.
 * Adds student-specific fields: studentId, course, grade.
 * Overrides toString() → demonstrates POLYMORPHISM.
 * Private fields with public getters/setters → ENCAPSULATION.
 * ─────────────────────────────────────────────────────────────────────────────
 */
public class Student extends Person {

    // ── Student-specific private fields ─────────────────────────────────────
    private int    studentId;   // Unique identifier for each student
    private String course;      // Course / department enrolled in
    private double grade;       // Current GPA or grade percentage

    /**
     * Parameterized constructor – creates a fully populated Student object.
     *
     * @param studentId Unique ID assigned to the student
     * @param name      Full name (passed up to Person via super())
     * @param age       Age      (passed up to Person via super())
     * @param course    Course the student is enrolled in
     * @param grade     Academic grade / GPA of the student
     */
    public Student(int studentId, String name, int age, String course, double grade) {
        super(name, age);           // Call Person's constructor (Inheritance)
        this.studentId = studentId;
        this.course    = course;
        this.grade     = grade;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────

    /** Returns the unique student ID. */
    public int getStudentId() { return studentId; }

    /** Updates the student ID. */
    public void setStudentId(int studentId) { this.studentId = studentId; }

    /** Returns the course the student is enrolled in. */
    public String getCourse() { return course; }

    /** Updates the student's course. */
    public void setCourse(String course) { this.course = course; }

    /** Returns the student's grade / GPA. */
    public double getGrade() { return grade; }

    /** Updates the student's grade. */
    public void setGrade(double grade) { this.grade = grade; }

    /**
     * Overrides Person.toString() – POLYMORPHISM.
     * Returns a nicely formatted single-line summary of the student.
     */
    @Override
    public String toString() {
        return String.format(
            "┌─ ID: %-5d │ Name: %-20s │ Age: %-3d │ Course: %-15s │ Grade: %.2f",
            studentId, getName(), getAge(), course, grade
        );
    }
}
