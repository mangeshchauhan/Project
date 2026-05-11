package util;

/**
 * ─────────────────────────────────────────────────────────────────────────────
 * CLASS: ValidationUtil  (Utility / Helper Class)
 * ─────────────────────────────────────────────────────────────────────────────
 * Purpose  : Centralises all input-validation logic so that other classes
 *            stay clean and focused on their own responsibilities.
 *            (Single Responsibility Principle – beginner OOP best-practice)
 *
 * Design   : All methods are STATIC – no need to instantiate this class.
 *            Private constructor prevents accidental instantiation.
 *
 * Validates: student name, age, course name, grade, and student ID.
 * ─────────────────────────────────────────────────────────────────────────────
 */
public class ValidationUtil {

    // ── Private constructor – prevents instantiation ─────────────────────────
    private ValidationUtil() {
        throw new UnsupportedOperationException("Utility class – do not instantiate.");
    }

    // ────────────────────────────────────────────────────────────────────────
    // NAME VALIDATION
    // ────────────────────────────────────────────────────────────────────────

    /**
     * Validates that a student name is non-null, non-blank, and
     * contains only letters and spaces (no digits or special characters).
     *
     * @param name The name string to validate
     * @return     true if the name is valid, false otherwise
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        // Only alphabetic characters and single spaces allowed
        return name.trim().matches("[a-zA-Z]+(\\s[a-zA-Z]+)*");
    }

    // ────────────────────────────────────────────────────────────────────────
    // AGE VALIDATION
    // ────────────────────────────────────────────────────────────────────────

    /**
     * Validates that the student's age falls within a realistic range
     * for a student (5 – 100 inclusive).
     *
     * @param age The age integer to validate
     * @return    true if age is between 5 and 100, false otherwise
     */
    public static boolean isValidAge(int age) {
        return age >= 5 && age <= 100;
    }

    // ────────────────────────────────────────────────────────────────────────
    // COURSE VALIDATION
    // ────────────────────────────────────────────────────────────────────────

    /**
     * Validates that a course name is non-null and non-blank.
     * Allows alphanumeric characters, spaces, and common punctuation.
     *
     * @param course The course name string to validate
     * @return       true if the course name is non-empty, false otherwise
     */
    public static boolean isValidCourse(String course) {
        if (course == null || course.trim().isEmpty()) {
            return false;
        }
        return course.trim().length() >= 2;   // At least 2 characters
    }

    // ────────────────────────────────────────────────────────────────────────
    // GRADE VALIDATION
    // ────────────────────────────────────────────────────────────────────────

    /**
     * Validates that a grade value is within the allowed range of 0.0 – 100.0.
     *
     * @param grade The grade (GPA / percentage) to validate
     * @return      true if grade is between 0.0 and 100.0 inclusive
     */
    public static boolean isValidGrade(double grade) {
        return grade >= 0.0 && grade <= 100.0;
    }

    // ────────────────────────────────────────────────────────────────────────
    // STUDENT ID VALIDATION
    // ────────────────────────────────────────────────────────────────────────

    /**
     * Validates that a student ID is a positive integer (greater than zero).
     *
     * @param id The student ID to validate
     * @return   true if id > 0, false otherwise
     */
    public static boolean isValidStudentId(int id) {
        return id > 0;
    }

    // ────────────────────────────────────────────────────────────────────────
    // GRADE LETTER HELPER
    // ────────────────────────────────────────────────────────────────────────

    /**
     * Converts a numeric grade (0–100) to a letter grade (A+, A, B, C, D, F).
     * Used when displaying student records.
     *
     * @param grade Numeric grade between 0.0 and 100.0
     * @return      Corresponding letter grade as a String
     */
    public static String getLetterGrade(double grade) {
        if (grade >= 90) return "A+";
        else if (grade >= 80) return "A";
        else if (grade >= 70) return "B";
        else if (grade >= 60) return "C";
        else if (grade >= 50) return "D";
        else                  return "F";
    }
}
