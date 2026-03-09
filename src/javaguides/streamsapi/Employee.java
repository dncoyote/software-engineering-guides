package javaguides.streamsapi;

public class Employee {

  private String name;
  private String department;
  private String designation;
  private Integer salary;

  public Employee(String name, String department, String designation, Integer salary) {
    this.name = name;
    this.department = department;
    this.designation = designation;
    this.salary = salary;
  }

  public String getName() {
    return name;
  }

  public String getDepartment() {
    return department;
  }

  public String getDesignation() {
    return designation;
  }

  public Integer getSalary() {
    return salary;
  }

  @Override
  public String toString() {
    return "Employee{" +
        "name='" + name + '\'' +
        ", department='" + department + '\'' +
        ", designation='" + designation + '\'' +
        ", salary=" + salary +
        '}';
  }

}
