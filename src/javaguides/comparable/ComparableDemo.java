package javaguides.comparable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComparableDemo {

  public static void main(String[] args) {

    List<Person> persons = new ArrayList<>();

    persons.add(new Person("Alice", 30, "alice@mail.com"));
    persons.add(new Person("Bob", 25, "bob@mail.com"));
    persons.add(new Person("Charlie", 30, "charlie@mail.com"));
    persons.add(new Person("David", 22, "david@mail.com"));
    persons.add(new Person("ZAbbey", 30, "abbey@mail.com"));

    System.out.println("Before Sorting:");
    persons.forEach(System.out::println);

    // Uses compareTo() automatically
    Collections.sort(persons);

    System.out.println("\nAfter Sorting:");
    persons.forEach(System.out::println);
  }
}
