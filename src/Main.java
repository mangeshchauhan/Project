import service.StudentService;

import java.util.Scanner;

/**
 * CLASS: Main  (Entry Point / Controller Class)
 *
 * Purpose  : Entry point of the application.
 *            Handles all console interaction via Scanner and delegates
 *            all business logic to StudentService.
 *
 * Design   : Uses ONLY scanner.nextLine() – parses numbers manually.
 *            This avoids the classic Scanner-skipping bug that occurs
 *            when mixing nextInt()/nextDouble() with nextLine().
 *
 * Java 21  : Uses text blocks (""") and switch expressions.
 */
public class Main {

    // Shared Scanner – one instance for the whole application
    private static final Scanner scanner = new Scanner(System.in);

    // Service layer – all CRUD operations go here
    private static final StudentService studentService = new StudentService();

    // ========================================================================
    //  MAIN METHOD
    // ========================================================================

    /**
     * JVM entry point. Shows banner then loops the main menu.
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {

        printBanner();

        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt("  Enter your choice: ");

            // Switch expression (standard Java 14+, supported in Java 21)
            running = switch (choice) {
                case 1  -> { handleAddStudent(); yield true;  }
                case 2  -> { handleViewAll();    yield true;  }
                case 3  -> { handleViewById();   yield true;  }
                case 4  -> { handleUpdate();     yield true;  }
                case 5  -> { handleDelete();     yield true;  }
                case 6  -> { handleExit();       yield false; }
                default -> {
                    System.out.println("\n  [!] Invalid option. Please choose 1-6.\n");
                    yield true;
                }
            };
        }

        scanner.close();
    }

    // ========================================================================
    //  MENU DISPLAY METHODS
    // ========================================================================

    /**
     * Prints the welcome banner using a Java 21 text block.
     */
    private static void printBanner() {
        System.out.println("""
                
                +======================================================+
                |        STUDENT MANAGEMENT SYSTEM                     |
                |          Built with Core Java 21                     |
                +======================================================+
                """);
    }

    /**
     * Prints the numbered main menu options to the console.
     */
    private static void printMenu() {
        System.out.println("  +------------------------------------+");
        System.out.println("  |            MAIN MENU               |");
        System.out.println("  |------------------------------------|");
        System.out.println("  |  1. Add Student                    |");
        System.out.println("  |  2. View All Students              |");
        System.out.println("  |  3. Search Student by ID           |");
        System.out.println("  |  4. Update Student                 |");
        System.out.println("  |  5. Delete Student                 |");
        System.out.println("  |  6. Exit                           |");
        System.out.println("  +------------------------------------+");
    }

    // ========================================================================
    //  HANDLER: Add Student  (CREATE)
    // ========================================================================

    /**
     * Collects student details from the console and delegates to service.
     * All fields read as String, then parsed – no Scanner type-mismatch risk.
     */
    private static void handleAddStudent() {
        System.out.println("\n-- Add New Student ---------------------------------");
        try {
            String name   = readString("  Name        : ");
            int    age    = readInt   ("  Age         : ");
            String course = readString("  Course      : ");
            double grade  = readDouble("  Grade (0-100): ");

            studentService.addStudent(name, age, course, grade);

        } catch (IllegalArgumentException e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        }
        pause();
    }

    // ========================================================================
    //  HANDLER: View All Students  (READ – All)
    // ========================================================================

    /**
     * Asks the service to print all students to the console.
     */
    private static void handleViewAll() {
        studentService.displayAllStudents();
        pause();
    }

    // ========================================================================
    //  HANDLER: View Student by ID  (READ – Single)
    // ========================================================================

    /**
     * Reads a student ID from the user, then displays that student's details.
     */
    private static void handleViewById() {
        System.out.println("\n-- Search Student by ID ----------------------------");
        try {
            int id = readInt("  Student ID: ");
            studentService.displayStudentById(id);
        } catch (IllegalArgumentException e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        }
        pause();
    }

    // ========================================================================
    //  HANDLER: Update Student  (UPDATE)
    // ========================================================================

    /**
     * Reads a student ID then prompts for each field.
     * Pressing Enter (blank line) skips that field – keeps existing value.
     */
    private static void handleUpdate() {
        System.out.println("\n-- Update Student ----------------------------------");
        System.out.println("  (Press Enter to keep the existing value)");

        try {
            int id = readInt("  Student ID to update: ");
            studentService.displayStudentById(id);    // Show current record
            System.out.println();

            String newName   = readString("  New Name   (Enter to skip): ");
            String ageRaw    = readString("  New Age    (Enter to skip): ");
            String newCourse = readString("  New Course (Enter to skip): ");
            String gradeRaw  = readString("  New Grade  (Enter to skip): ");

            int    newAge   = ageRaw.isEmpty()   ? -1 : Integer.parseInt(ageRaw.trim());
            double newGrade = gradeRaw.isEmpty() ? -1 : Double.parseDouble(gradeRaw.trim());

            studentService.updateStudent(
                id,
                newName.isEmpty()   ? null : newName,
                newAge,
                newCourse.isEmpty() ? null : newCourse,
                newGrade
            );

        } catch (NumberFormatException e) {
            System.out.println("\n  [ERROR] Invalid number entered. Update cancelled.");
        } catch (IllegalArgumentException e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        }
        pause();
    }

    // ========================================================================
    //  HANDLER: Delete Student  (DELETE)
    // ========================================================================

    /**
     * Reads an ID, previews the student, asks for confirmation, then deletes.
     */
    private static void handleDelete() {
        System.out.println("\n-- Delete Student ----------------------------------");
        try {
            int id = readInt("  Student ID to delete: ");
            studentService.displayStudentById(id);    // Show before delete

            String confirm = readString("\n  Confirm delete? (yes/no): ").toLowerCase();
            if (confirm.equals("yes") || confirm.equals("y")) {
                studentService.deleteStudent(id);
            } else {
                System.out.println("  [INFO] Delete cancelled.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        }
        pause();
    }

    // ========================================================================
    //  HANDLER: Exit
    // ========================================================================

    /**
     * Prints a farewell message before the loop ends and the JVM exits.
     */
    private static void handleExit() {
        System.out.println("""
                
                +======================================================+
                |  Thank you for using Student Management System!      |
                |                   Goodbye!                           |
                +======================================================+
                """);
    }

    // ========================================================================
    //  INPUT HELPER METHODS
    // ========================================================================

    /**
     * Reads a line of text from the console.
     * Returns an empty String (never null) if the user just presses Enter.
     *
     * @param prompt Message printed before the cursor
     * @return Trimmed string from the user
     */
    private static String readString(String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine();
        return (line == null) ? "" : line.trim();
    }

    /**
     * Reads an integer from the console. Re-prompts on non-numeric input.
     *
     * @param prompt Message printed before the cursor
     * @return Valid integer entered by the user
     */
    private static int readInt(String prompt) {
        while (true) {
            String raw = readString(prompt);
            try {
                return Integer.parseInt(raw);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a whole number.");
            }
        }
    }

    /**
     * Reads a double from the console. Re-prompts on non-numeric input.
     *
     * @param prompt Message printed before the cursor
     * @return Valid double entered by the user
     */
    private static double readDouble(String prompt) {
        while (true) {
            String raw = readString(prompt);
            try {
                return Double.parseDouble(raw);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a decimal number (e.g. 85.5).");
            }
        }
    }

    /**
     * Pauses the program until the user presses Enter.
     * Prevents the next menu from instantly overwriting the current output.
     */
    private static void pause() {
        System.out.print("\n  Press Enter to continue...");
        scanner.nextLine();
    }
}
