package javaguides.comparable;

import java.util.Objects;

public class Person implements Comparable<Person> {

  private final String name;
  private final int age;
  private final String email;

  public Person(String name, int age, String email) {
    this.name = Objects.requireNonNull(name, "Name cannot be null");
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

  /**
   * Defines natural ordering for Person.
   * Rule:
   * 1. Sort by age
   * 2. If ages equal → sort by name
   */
  @Override
  public int compareTo(Person other) {

    int ageComparison = Integer.compare(this.age, other.age);

    if (ageComparison != 0) {
      return ageComparison;
    }

    return this.name.compareToIgnoreCase(other.name);
  }

  @Override
  public String toString() {
    return "Person{name='%s', age=%d, email='%s'}"
        .formatted(name, age, email);
  }
}
