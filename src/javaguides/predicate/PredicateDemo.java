package javaguides.predicate;

import java.util.List;
import java.util.function.Predicate;
import java.util.Arrays;

public class PredicateDemo {

  public static void main(String[] args) {
    List<String> words = Arrays.asList("apple", "banana", "orange", "grape", "watermelon");

    // Create a Predicate to filter words with length greater than 5
    Predicate<String> lengthGreaterThan5 = word -> word.length() > 5;

    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    // Creating a Predicate to filter even numbers
    Predicate<Integer> isEven = num -> num % 2 == 0;

    System.out.println(findWordLengthGreaterThan5(words, lengthGreaterThan5));
    System.out.println(findEvenNumbers(numbers, isEven));

  }

  public static List<String> findWordLengthGreaterThan5(List<String> words, Predicate<String> lengthGreaterThan5) {
    return words.stream().filter(lengthGreaterThan5).toList();
  }

  public static List<Integer> findEvenNumbers(List<Integer> numbers, Predicate<Integer> isEven) {
    return numbers.stream().filter(isEven).toList();
  }
}
