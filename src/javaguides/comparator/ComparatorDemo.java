package javaguides.comparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ComparatorDemo {

  public static void main(String[] args) {

    List<Person> persons = new ArrayList<>();

    persons.add(new Person("Alice", 30, "alice@mail.com"));
    persons.add(new Person("Bob", 25, "bob@mail.com"));
    persons.add(new Person("Charlie", 30, "charlie@mail.com"));
    persons.add(new Person("David", 22, "david@mail.com"));

    System.out.println("Before Sorting:");
    persons.forEach(System.out::println);

    // Use custom comparator
    Collections.sort(persons, new PersonAgeNameComparator());

    System.out.println("\nAfter Sorting (Age → Name):");
    persons.forEach(System.out::println);

    System.out.println("\nIn modern codebases :");

    persons.sort(
        Comparator
            .comparing(Person::getAge)
            .thenComparing(Person::getName));

    System.out.println("\nAfter Sorting (Age → Name):");
    persons.forEach(System.out::println);

    System.out.println("\nIn modern codebases we can define multiple sorting strategies :");

    Comparator<Person> sortByName = Comparator.comparing(Person::getName);

    Comparator<Person> sortByEmail = Comparator.comparing(Person::getEmail);

    persons.sort(sortByName);

    System.out.println("\nAfter Sorting (Name):");
    persons.forEach(System.out::println);

    persons.sort(sortByEmail);

    System.out.println("\nAfter Sorting (Email):");
    persons.forEach(System.out::println);
  }
}
