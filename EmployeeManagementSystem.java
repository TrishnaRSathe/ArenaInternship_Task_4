package LibraryManagementSystem;
import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
import java.time.LocalDate;

class Employee implements Serializable {
    String id;
    String name;
    String department;
    String position;
    double salary;
    LocalDate joinDate;

    public Employee(String id, String name, String department, String position, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.position = position;
        this.salary = salary;
        this.joinDate = LocalDate.now();
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#,###.00");
        return String.format("ID: %s, Name: %s, Dept: %s, Position: %s, Salary: ₹%s, Joined: %s",
                id, name, department, position, df.format(salary), joinDate);
    }
}

public class EmployeeManagementSystem {
    static ArrayList<Employee> employees = new ArrayList<>();
    static final String FILE_NAME = "employees.dat";
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadFromFile();
        while (true) {
            System.out.println("=== EMPLOYEE MANAGEMENT SYSTEM ===");
            System.out.println("1. Add New Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Search Employee");
            System.out.println("4. Update Employee");
            System.out.println("5. Delete Employee");
            System.out.println("6. Generate Reports");
            System.out.println("7. Save to File");
            System.out.println("8. Load from File");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1": addEmployee(); break;
                case "2": viewEmployees(); break;
                case "3": searchEmployee(); break;
                case "4": updateEmployee(); break;
                case "5": deleteEmployee(); break;
                case "6": generateReports(); break;
                case "7": saveToFile(); break;
                case "8": loadFromFile(); break;
                case "9": System.out.println("Exiting program. Goodbye! "); return;
                default: System.out.println("Invalid choice, try again!\n");
            }
        }
    }

    //  Employee Operations 
    public static void addEmployee() {
        System.out.println("\n=== ADD NEW EMPLOYEE ===");
        System.out.print("Enter Employee ID: ");
        String id = sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Department: ");
        String dept = sc.nextLine();
        System.out.print("Enter Position: ");
        String pos = sc.nextLine();
        System.out.print("Enter Salary: ");
        double salary = Double.parseDouble(sc.nextLine());

        employees.add(new Employee(id, name, dept, pos, salary));
        System.out.println("Employee added successfully!");
        saveToFile();
    }

    public static void viewEmployees() {
        System.out.println("\n=== ALL EMPLOYEES ===");
        if (employees.isEmpty()) {
            System.out.println("No employees found.\n");
            return;
        }
        System.out.printf("%-10s%-20s%-15s%-20s%-12s%-12s\n", "ID", "Name", "Department", "Position", "Salary", "Join Date");
        System.out.println("----------------------------------------------------------------------------------------");
        DecimalFormat df = new DecimalFormat("#,###.00");
        for (Employee e : employees) {
            System.out.printf("%-10s%-20s%-15s%-20s₹%-11s%-12s\n",
                    e.id, e.name, e.department, e.position, df.format(e.salary), e.joinDate);
        }
        System.out.println();
    }

    public static void searchEmployee() {
        System.out.println("\n=== SEARCH EMPLOYEE ===");
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        System.out.println("3. Search by Department");
        System.out.print("Enter choice: ");
        String choice = sc.nextLine();

        ArrayList<Employee> results = new ArrayList<>();
        switch (choice) {
            case "1":
                System.out.print("Enter Employee ID: ");
                String id = sc.nextLine();
                for (Employee e : employees) if (e.id.equalsIgnoreCase(id)) results.add(e);
                break;
            case "2":
                System.out.print("Enter Name: ");
                String name = sc.nextLine().toLowerCase();
                for (Employee e : employees) if (e.name.toLowerCase().contains(name)) results.add(e);
                break;
            case "3":
                System.out.print("Enter Department: ");
                String dept = sc.nextLine().toLowerCase();
                for (Employee e : employees) if (e.department.toLowerCase().contains(dept)) results.add(e);
                break;
            default:
                System.out.println("Invalid choice!"); return;
        }

        System.out.printf("\n Search Results (%d employee(s) found):\n\n", results.size());
        for (Employee e : results) System.out.println(e);
        System.out.println();
    }

    public static void updateEmployee() {
        System.out.print("Enter Employee ID to update: ");
        String id = sc.nextLine();
        Employee emp = null;
        for (Employee e : employees) if (e.id.equalsIgnoreCase(id)) emp = e;

        if (emp == null) {
            System.out.println(" Employee not found!\n");
            return;
        }

        System.out.println("Leave blank to keep current value.");
        System.out.print("Name (" + emp.name + "): ");
        String input = sc.nextLine(); if (!input.isEmpty()) emp.name = input;
        System.out.print("Department (" + emp.department + "): ");
        input = sc.nextLine(); if (!input.isEmpty()) emp.department = input;
        System.out.print("Position (" + emp.position + "): ");
        input = sc.nextLine(); if (!input.isEmpty()) emp.position = input;
        System.out.print("Salary (" + emp.salary + "): ");
        input = sc.nextLine(); if (!input.isEmpty()) emp.salary = Double.parseDouble(input);

        System.out.println("Employee updated successfully!\n");
        saveToFile();
    }

    public static void deleteEmployee() {
        System.out.print("Enter Employee ID to delete: ");
        String id = sc.nextLine();
        employees.removeIf(e -> e.id.equalsIgnoreCase(id));
        System.out.println(" Employee deleted successfully (if existed).\n");
        saveToFile();
    }

    public static void generateReports() {
        System.out.println("\n EMPLOYEE REPORTS ");
        System.out.println("1. Department-wise Summary");
        System.out.println("2. Salary Statistics");
        System.out.println("3. Employee Count by Position");
        System.out.print("Enter choice: ");
        String choice = sc.nextLine();

        switch (choice) {
            case "1":
                HashMap<String, ArrayList<Double>> deptSummary = new HashMap<>();
                for (Employee e : employees)
                    deptSummary.computeIfAbsent(e.department, k -> new ArrayList<>()).add(e.salary);

                System.out.println("\n DEPARTMENT SUMMARY:");
                for (String dept : deptSummary.keySet()) {
                    ArrayList<Double> sals = deptSummary.get(dept);
                    double avg = sals.stream().mapToDouble(Double::doubleValue).average().orElse(0);
                    System.out.printf("• %s: %d employee(s), Average: ₹%,.2f\n", dept, sals.size(), avg);
                }
                System.out.println();
                break;

            case "2":
                if (employees.isEmpty()) { System.out.println("No employees to calculate stats.\n"); return; }
                double totalSalary = employees.stream().mapToDouble(e -> e.salary).sum();
                double avgSalary = totalSalary / employees.size();
                Employee highest = Collections.max(employees, Comparator.comparingDouble(e -> e.salary));
                Employee lowest = Collections.min(employees, Comparator.comparingDouble(e -> e.salary));

                System.out.println("\nSALARY STATISTICS:");
                System.out.println("• Total Employees: " + employees.size());
                System.out.printf("• Total Salary: ₹%,.2f\n", totalSalary);
                System.out.printf("• Average Salary: ₹%,.2f\n", avgSalary);
                System.out.printf("• Highest Salary: ₹%,.2f (%s)\n", highest.salary, highest.name);
                System.out.printf("• Lowest Salary: ₹%,.2f (%s)\n\n", lowest.salary, lowest.name);
                break;

            case "3":
                HashMap<String, Integer> posCount = new HashMap<>();
                for (Employee e : employees) posCount.put(e.position, posCount.getOrDefault(e.position, 0) + 1);
                System.out.println("\n EMPLOYEE COUNT BY POSITION:");
                for (String pos : posCount.keySet()) System.out.printf("• %s: %d\n", pos, posCount.get(pos));
                System.out.println();
                break;

            default: System.out.println("Invalid choice!\n");
        }
    }

    //  File Operations 
    public static void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(employees);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    public static void loadFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            employees = (ArrayList<Employee>) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println(" No file found. Starting with empty data.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

