package lld.designpatterns.creational.builder;

public class BuilderDemo {
  public static void main(String[] args) {
    User user = User.builder()
        .name("Johnny")
        .email("john@email.com")
        .age(32)
        .build();

    System.out.println(user);
  }
}
