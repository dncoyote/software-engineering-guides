package javaguides.streamsapi;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class StreamsDemo {
  public static void main(String[] args) {
    List<Employee> employeeList = Arrays.asList(
        new Employee("Alice", "Engineering", "Junior Engineer", 90000),
        new Employee("Alex", "HR", "Recruiter", 50000),
        new Employee("John", "Engineering", "Senior Engineer", 120000),
        new Employee("Jason", "Engineering", "Senior Engineer", 100000),
        new Employee("Sara", "HR", "Manager", 75000),
        new Employee("Ankit", "Finance", "Analyst", 110000),
        new Employee("Alice", "Finance", "Analyst", 110000));
    // 1. Filter employees whose salary is greater than 50,000 and return their
    // names.
    List<Employee> salaryGreaterThan50k = employeeList.stream().filter(e -> e.getSalary() > 50000).toList();
    List<String> salaryGreaterThan50kNames = employeeList.stream().filter(e -> e.getSalary() > 50000)
        .map(Employee::getName).toList();
    System.out.println("---------------------");
    System.out.println(" 1. Filter employees whose salary is greater than 50,000 and return their names.");
    System.out.println("---------------------");
    System.out.println(salaryGreaterThan50k);
    System.out.println(salaryGreaterThan50kNames);
    System.out.println("---------------------");
    // 2. Group a list of employees by department and return a map where the key is
    // department and value is list of employees.
    Map<String, List<Employee>> departmentMap = employeeList.stream()
        .collect(Collectors.groupingBy(emp -> emp.getDepartment()));
    System.out.println("---------------------");
    System.out.println(
        " 2. Group a list of employees by department and return a map where the key is department and value is list of employees.");
    System.out.println("---------------------");
    System.out.println(departmentMap);
    System.out.println("---------------------");
    // 3. From a list of employees, find the employee with the highest salary.
    Employee empHighestSalary = employeeList.stream().max(Comparator.comparingInt(emp -> emp.getSalary()))
        .orElseThrow(() -> new RuntimeException("No employee found"));
    System.out.println("---------------------");
    System.out.println(" 3. From a list of employees, find the employee with the highest salary.");
    System.out.println("---------------------");
    System.out.println(empHighestSalary);
    System.out.println("---------------------");
    // 4. Second highest salary employee
    Employee empSecondHighestSalary = employeeList.stream()
        .sorted(Comparator.comparingInt(Employee::getSalary).reversed()).skip(1).findFirst()
        .orElseThrow(() -> new RuntimeException("No employee found"));
    System.out.println("---------------------");
    System.out.println(" 4. From a list of employees, find the employee with the second highest salary.");
    System.out.println("---------------------");
    System.out.println(empSecondHighestSalary);
    System.out.println("---------------------");
    // 5. Remove duplicate employees from a list based on employee name.
    // This will only work if equals() and hashCode()
    // is implemented
    List<Employee> noDuplicatesEmployeeListwr = employeeList.stream().distinct().toList();
    Set<String> seenNames = new HashSet<>();
    List<Employee> noDuplicatesEmployeeList = employeeList.stream().filter(emp -> seenNames.add(emp.getName()))
        .toList();
    System.out.println("---------------------");
    System.out.println(" 5. Remove duplicate employees from a list based on employee name.");
    System.out.println("---------------------");
    System.out.println(noDuplicatesEmployeeList);
    System.out.println("---------------------");
    // 6. Sort a list of employees by salary in descending order and return the top
    // 3
    // highest paid employees.
    List<Employee> threeHighestPaidEmp = employeeList.stream()
        .sorted(Comparator.comparingInt(Employee::getSalary).reversed()).limit(3).toList();
    System.out.println("---------------------");
    System.out.println(
        " 6. Sort a list of employees by salary in descending order and return the top 3 highest paid employees.");
    System.out.println("---------------------");
    System.out.println(threeHighestPaidEmp);
    System.out.println("---------------------");
    // 7. Get top 3 highest paid employees per department
    Map<String, List<Employee>> top3ByDept = employeeList.stream()
        .collect(Collectors.groupingBy(
            Employee::getDepartment,
            Collectors.collectingAndThen(
                Collectors.toList(),
                list -> list.stream()
                    .sorted(Comparator.comparingInt(Employee::getSalary).reversed())
                    .limit(3)
                    .toList())));
    System.out.println("---------------------");
    System.out.println(
        "7. Get top 3 highest paid employees per department.");
    System.out.println("---------------------");
    System.out.println(top3ByDept);
    System.out.println("---------------------");
    // 8. Find the average salary of employees in each department.
    Map<String, Double> avgSalary = employeeList.stream()
        .collect(Collectors.groupingBy(
            Employee::getDepartment,
            Collectors.averagingInt(Employee::getSalary)));
    System.out.println("---------------------");
    System.out.println("8. Find the average salary of employees in each department.");
    System.out.println("---------------------");
    System.out.println(avgSalary);
    System.out.println("---------------------");
    // 9. Group employees by department and return the total
    // salary per department.
    Map<String, Double> totalSalaryPerDept = employeeList.stream()
        .collect(Collectors.groupingBy(
            Employee::getDepartment,
            Collectors.summingDouble(Employee::getSalary)));
    System.out.println("---------------------");
    System.out.println(" 9. Group employees by department and return the total salary per department.");
    System.out.println("---------------------");
    System.out.println(totalSalaryPerDept);
    System.out.println("---------------------");
    // 10. Count how many employees belong to each department.
    Map<String, Long> empCountPerDept = employeeList.stream()
        .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));
    System.out.println("---------------------");
    System.out.println("10. Count how many employees belong to each department.");
    System.out.println("---------------------");
    System.out.println(empCountPerDept);
    System.out.println("---------------------");
    // 11. Return department with the highest number of employees
    Optional<String> deptWithMostEmployees = employeeList.stream()
        .collect(Collectors.groupingBy(
            Employee::getDepartment,
            Collectors.counting()))
        .entrySet()
        .stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey);
    System.out.println("---------------------");
    System.out.println("11. Return department with the highest number of employees");
    System.out.println("---------------------");
    System.out.println(deptWithMostEmployees);
    System.out.println("---------------------");
    // 12. Group employees by department and find the highest paid employee in each
    // department.
    Map<String, Optional<Employee>> highestPaidByDept = employeeList.stream()
        .collect(Collectors.groupingBy(
            Employee::getDepartment,
            Collectors.maxBy(
                Comparator.comparingInt(Employee::getSalary))));
    System.out.println("---------------------");
    System.out.println("12. Group employees by department and find the highest paid employee in each department.");
    System.out.println("---------------------");
    System.out.println(highestPaidByDept);
    System.out.println("---------------------");
    // 13. Group employees by department and then by designation (two-level
    Map<String, Map<String, List<Employee>>> groupEmpByDeptAndDesig = employeeList.stream()
        .collect(Collectors.groupingBy(
            Employee::getDepartment,
            Collectors.groupingBy(Employee::getDesignation)));
    System.out.println("---------------------");
    System.out.println(" 13. Group employees by department and then by designation (two-level)");
    System.out.println("---------------------");
    System.out.println(groupEmpByDeptAndDesig);
    System.out.println("---------------------");
    // grouping).
    // 14. Convert a list of strings to uppercase using Stream API.
    List<String> names = List.of("John", "Mason", "Alex");
    List<String> uppercaseNames = names.stream().map(String::toUpperCase).toList();
    System.out.println("---------------------");
    System.out.println(" 14. Convert a list of strings to uppercase using Stream API.");
    System.out.println("---------------------");
    System.out.println(uppercaseNames);
    System.out.println("---------------------");
    // 15. Flatten a list of lists of integers into a single list.
    List<List<Integer>> list = List.of(
        List.of(1, 2),
        List.of(3, 4),
        List.of(5, 6));
    List<Integer> flattenMap = list.stream().flatMap(l -> l.stream()).toList();
    System.out.println("---------------------");
    System.out.println(" 15. Flatten a list of lists of integers into a single list.");
    System.out.println("---------------------");
    System.out.println(flattenMap);
    System.out.println("---------------------");
    // Find the first non-repeating character in a string using streams.
    // Given a list of transactions, group them by transaction type (CREDIT/DEBIT)
    // and collect unique transaction IDs for each type.
  }
}
