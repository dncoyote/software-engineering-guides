package lld.designpatterns.creational.builder;

public class User {

  private final String name;
  private final String email;
  private final String phone;
  private final int age;

  private User(Builder builder) {
    this.name = builder.name;
    this.email = builder.email;
    this.phone = builder.phone;
    this.age = builder.age;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String name;
    private String email;
    private String phone;
    private int age;

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder phone(String phone) {
      this.phone = phone;
      return this;
    }

    public Builder age(int age) {
      this.age = age;
      return this;
    }

    public User build() {
      if (name == null || email == null) {
        throw new IllegalStateException("Name and Email are required.");
      }
      return new User(this);
    }
  }

  @Override
  public String toString() {
    return "User{name='" + name + "', email='" + email +
        "', phone='" + phone + "', age=" + age + "}";
  }
}
