package javaguides.collections.Map;

import java.util.Map;
import java.util.TreeMap;

public class TreeMapDemo {
  public static void main(String[] args) {

    Map<Integer, String> map = new TreeMap<>();

    map.put(40, "Forty");
    map.put(10, "Ten");
    map.put(30, "Thirty");
    map.put(20, "Twenty");
    map.put(20, "Twenty Updated"); // overwrites

    for (Map.Entry<Integer, String> e : map.entrySet()) {
      System.out.println(e.getKey() + " => " + e.getValue());
    }
  }
}
