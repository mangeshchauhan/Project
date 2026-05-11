package service;

import model.Student;
import util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * ─────────────────────────────────────────────────────────────────────────────
 * CLASS: StudentService  (Service / Business-Logic Class)
 * ─────────────────────────────────────────────────────────────────────────────
 * Purpose  : Contains ALL business logic for Student CRUD operations.
 *            Keeps the Main class thin – Main only handles menus & I/O.
 *
 * Storage  : An in-memory ArrayList<Student> acts as our data store.
 *
 * Operations:
 *   ➤ addStudent      – Create
 *   ➤ getAllStudents   – Read (all)
 *   ➤ findStudentById – Read (single)
 *   ➤ updateStudent   – Update
 *   ➤ deleteStudent   – Delete
 *   ➤ displayAll      – Pretty-print the full list
 * ─────────────────────────────────────────────────────────────────────────────
 */
public class StudentService {

    // ── In-memory data store ─────────────────────────────────────────────────
    private final ArrayList<Student> studentList = new ArrayList<>();

    // ── Auto-increment ID counter ────────────────────────────────────────────
    private int idCounter = 1;


    // ════════════════════════════════════════════════════════════════════════
    //  CREATE
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Adds a new student to the in-memory list after validating all inputs.
     * The student ID is assigned automatically (auto-increment).
     *
     * @param name   Full name of the student
     * @param age    Age of the student
     * @param course Course the student is enrolled in
     * @param grade  Academic grade / GPA (0.0 – 100.0)
     * @throws IllegalArgumentException if any input fails validation
     */
    public void addStudent(String name, int age, String course, double grade) {

        // ── Input Validation ─────────────────────────────────────────────────
        if (!ValidationUtil.isValidName(name)) {
            throw new IllegalArgumentException(
                "Invalid name! Name must contain only letters and spaces.");
        }
        if (!ValidationUtil.isValidAge(age)) {
            throw new IllegalArgumentException(
                "Invalid age! Age must be between 5 and 100.");
        }
        if (!ValidationUtil.isValidCourse(course)) {
            throw new IllegalArgumentException(
                "Invalid course! Course name must be at least 2 characters.");
        }
        if (!ValidationUtil.isValidGrade(grade)) {
            throw new IllegalArgumentException(
                "Invalid grade! Grade must be between 0.0 and 100.0.");
        }

        // ── Create and store the new Student object ──────────────────────────
        Student newStudent = new Student(idCounter++, name.trim(), age, course.trim(), grade);
        studentList.add(newStudent);

        System.out.println("\n  ✅  Student added successfully! Assigned ID: "
            + newStudent.getStudentId());
    }


    // ════════════════════════════════════════════════════════════════════════
    //  READ – All Students
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Returns an unmodifiable view of all students currently in the system.
     * Returning a List interface (not ArrayList) is a good OOP practice –
     * callers don't need to know about the underlying implementation.
     *
     * @return List of all Student objects (may be empty, never null)
     */
    public List<Student> getAllStudents() {
        return studentList;   // Direct ref is fine for this beginner project
    }


    // ════════════════════════════════════════════════════════════════════════
    //  READ – Single Student by ID
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Searches the list for a student with the matching ID.
     * Uses Java's Optional to avoid returning null.
     *
     * @param id The student ID to search for
     * @return   Optional containing the Student if found, or empty Optional
     */
    public Optional<Student> findStudentById(int id) {
        return studentList.stream()
                          .filter(s -> s.getStudentId() == id)
                          .findFirst();
    }


    // ════════════════════════════════════════════════════════════════════════
    //  UPDATE
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Updates the details of an existing student identified by their ID.
     * Only non-null / non-blank values trigger a field update –
     * pass null to skip updating a particular field.
     *
     * @param id        ID of the student to update
     * @param newName   Updated name   (null → keep existing)
     * @param newAge    Updated age    (-1  → keep existing)
     * @param newCourse Updated course (null → keep existing)
     * @param newGrade  Updated grade  (-1  → keep existing)
     * @throws IllegalArgumentException if ID not found or new values are invalid
     */
    public void updateStudent(int id, String newName, int newAge,
                              String newCourse, double newGrade) {

        // ── Find the student ─────────────────────────────────────────────────
        Student student = findStudentById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "No student found with ID: " + id));

        // ── Validate & apply each field only if a new value is provided ──────
        if (newName != null && !newName.isBlank()) {
            if (!ValidationUtil.isValidName(newName)) {
                throw new IllegalArgumentException("Invalid name provided.");
            }
            student.setName(newName.trim());
        }

        if (newAge != -1) {
            if (!ValidationUtil.isValidAge(newAge)) {
                throw new IllegalArgumentException(
                    "Invalid age! Must be between 5 and 100.");
            }
            student.setAge(newAge);
        }

        if (newCourse != null && !newCourse.isBlank()) {
            if (!ValidationUtil.isValidCourse(newCourse)) {
                throw new IllegalArgumentException("Invalid course name.");
            }
            student.setCourse(newCourse.trim());
        }

        if (newGrade != -1) {
            if (!ValidationUtil.isValidGrade(newGrade)) {
                throw new IllegalArgumentException(
                    "Invalid grade! Must be between 0.0 and 100.0.");
            }
            student.setGrade(newGrade);
        }

        System.out.println("\n  ✅  Student with ID " + id + " updated successfully!");
    }


    // ════════════════════════════════════════════════════════════════════════
    //  DELETE
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Removes the student with the given ID from the list.
     *
     * @param id The student ID to delete
     * @throws IllegalArgumentException if no student is found with that ID
     */
    public void deleteStudent(int id) {

        // ── Locate the student first ─────────────────────────────────────────
        Student toDelete = findStudentById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "No student found with ID: " + id));

        studentList.remove(toDelete);
        System.out.println("\n  ✅  Student \"" + toDelete.getName()
            + "\" (ID: " + id + ") deleted successfully!");
    }


    // ════════════════════════════════════════════════════════════════════════
    //  DISPLAY – Pretty print all students
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Prints all students to the console in a formatted table-like layout.
     * Also shows the letter grade alongside the numeric grade.
     * If the list is empty, a friendly message is shown instead.
     */
    public void displayAllStudents() {

        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════════════════════════════════════╗");
        System.out.println("  ║                        📋  STUDENT RECORDS                              ║");
        System.out.println("  ╠══════════════════════════════════════════════════════════════════════════╣");

        if (studentList.isEmpty()) {
            System.out.println("  ║   No students found. Add a student first!                               ║");
        } else {
            System.out.printf("  ║  %-4s  %-20s  %-4s  %-15s  %-8s  %-3s  ║%n",
                "ID", "Name", "Age", "Course", "Grade%", "Ltr");
            System.out.println("  ╠══════════════════════════════════════════════════════════════════════════╣");

            for (Student s : studentList) {
                System.out.printf("  ║  %-4d  %-20s  %-4d  %-15s  %-8.2f  %-3s  ║%n",
                    s.getStudentId(),
                    s.getName(),
                    s.getAge(),
                    s.getCourse(),
                    s.getGrade(),
                    ValidationUtil.getLetterGrade(s.getGrade()));
            }
        }

        System.out.println("  ╚══════════════════════════════════════════════════════════════════════════╝");
        System.out.println("  Total Students: " + studentList.size());
    }


    // ════════════════════════════════════════════════════════════════════════
    //  DISPLAY – Single student details
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Prints detailed information for a single student identified by ID.
     *
     * @param id The student ID to display
     */
    public void displayStudentById(int id) {

        Student student = findStudentById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "No student found with ID: " + id));

        System.out.println();
        System.out.println("  ┌─────────────────────────────────────────┐");
        System.out.println("  │          🎓  Student Details             │");
        System.out.println("  ├─────────────────────────────────────────┤");
        System.out.printf ("  │  ID      : %-29d │%n", student.getStudentId());
        System.out.printf ("  │  Name    : %-29s │%n", student.getName());
        System.out.printf ("  │  Age     : %-29d │%n", student.getAge());
        System.out.printf ("  │  Course  : %-29s │%n", student.getCourse());
        System.out.printf ("  │  Grade   : %-26.2f %s  │%n",
            student.getGrade(),
            ValidationUtil.getLetterGrade(student.getGrade()));
        System.out.println("  └─────────────────────────────────────────┘");
    }
}
