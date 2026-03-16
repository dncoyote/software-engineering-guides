package javaguides.comparator;

import java.util.Comparator;

public class PersonAgeNameComparator implements Comparator<Person> {

  @Override
  public int compare(Person p1, Person p2) {

    // Primary comparison → age
    int ageComparison = Integer.compare(p1.getAge(), p2.getAge());

    if (ageComparison != 0) {
      return ageComparison;
    }

    // Secondary comparison → name
    return p1.getName().compareToIgnoreCase(p2.getName());
  }
}
