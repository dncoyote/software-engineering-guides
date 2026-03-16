package javaguides.comparator;

import java.util.Objects;

public class Person {

  private final String name;
  private final int age;
  private final String email;

  public Person(String name, int age, String email) {
    this.name = Objects.requireNonNull(name, "name cannot be null");
    this.age = age;
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public String toString() {
    return "Person{name='%s', age=%d, email='%s'}"
        .formatted(name, age, email);
  }
}
