package project.taskmanager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager manager = new TaskManager();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // 🔹 Step 1: Task Creation Loop
        while (true) {
            System.out.println("\n────────────────────────────────");
            System.out.println("📌 Enter Task Details:");
            System.out.println("────────────────────────────────");

            System.out.print("Description: ");
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                System.out.println("❌ Error: Description cannot be empty.");
                continue;
            }

            System.out.print("Due Date (yyyy-MM-dd HH:mm): ");
            LocalDateTime dueDate;
            try {
                dueDate = LocalDateTime.parse(scanner.nextLine().trim(), formatter);
                if (dueDate.isBefore(LocalDateTime.now())) {
                    System.out.println("❌ Error: Due date must be in the future!");
                    continue;
                }
            } catch (Exception e) {
                System.out.println("❌ Invalid date format. Use yyyy-MM-dd HH:mm");
                continue;
            }

            int priority = getIntInput(scanner, "Priority (1-High 🔴, 2-Medium 🟡, 3-Low 🟢): ", 1, 3);
            int type = getIntInput(scanner, "Task Type (1-Work 💼, 2-Personal 🏡): ", 1, 2);

            try {
                Task task = (type == 1) ? new WorkTask(description, dueDate, priority) :
                                          new PersonalTask(description, dueDate, priority);
                manager.addTask(task);
                System.out.println("✅ Task added successfully!");
            } catch (InvalidTaskException e) {
                System.out.println("❌ Error: " + e.getMessage());
            }

            System.out.print("\n➜ Do you want to add another task? (yes/no): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                break;
            }
        }

        // 🔹 Step 2: Task Menu
        while (true) {
            System.out.println("\n══════════════════════════════════════════");
            System.out.println("📌 TASK MANAGER MENU:");
            System.out.println("══════════════════════════════════════════");
            System.out.println("1️⃣ Show All Tasks");
            System.out.println("2️⃣ Show Work Tasks");
            System.out.println("3️⃣ Show Personal Tasks");
            System.out.println("4️⃣ Show Urgent Tasks (Due Soon)");
            System.out.println("5️⃣ Delete a Task");
            System.out.println("6️⃣ Exit");
            int option = getIntInput(scanner, "➜ Choose an option: ", 1, 6);

            switch (option) {
                case 1 -> manager.showTasks();
                case 2 -> manager.showWorkTasks();
                case 3 -> manager.showPersonalTasks();
                case 4 -> manager.showUrgentTasks();
                case 5 -> {
                    int idToDelete = getIntInput(scanner, "➜ Enter Task ID to delete: ", 1, Integer.MAX_VALUE);
                    manager.deleteTask(idToDelete);
                }
                case 6 -> {
                    System.out.println("👋 Exiting Task Manager...");
                    scanner.close();
                    return;
                }
            }
        }
    }

    // 🔹 **New Method: Handles Safe Integer Input with Validation**
    private static int getIntInput(Scanner scanner, String message, int min, int max) {
        int value;
        while (true) {
            System.out.print(message);
            try {
                value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("❌ Invalid input! Enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input! Please enter a valid number.");
            }
        }
    }
}
