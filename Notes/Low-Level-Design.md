# Low Level Design 
## Table of Contents

 1. [OOP Basics](#OOP-basics)
    1. Classes and Objects
    2. Access Modifiers (private, protected, public)
    3. Fields and Methods
    4. Constructors
    5. Static vs Instance
    6. Abstraction
    7. Encapsulation
    8. Inheritance
    9. Polymorphism (compile-time & runtime)
    10. Has-a vs Is-a
    11. Favoring delegation

## LLD
- Low-Level Design (LLD) is the process of designing how individual classes, objects, methods, and modules in a software system will work together to fulfill business and system requirements.
- It is the translation of the High-Level Design (HLD) into actual code-level architecture using OOP principles, design patterns, class diagrams, and real-world programming constructs.
#### Characteristics
- LLD bridges the Gap Between Architecture and Code.
- It encodes Domain Knowledge as Object Models.
- It enables Scalability and Maintainability, and improves Testability.

# OOP Basics
## Classes and Objects
- A <b>class</b> is a blueprint or template that defines the structure and behavior (data and methods) of real-world entities.
- An <b>object</b> is a concrete instance of a class — it holds actual values in memory based on the class definition.
- Think of a class as a Car design blueprint (defines engine type, color options, speed limits).
- An object is an actual car — say, a red Honda City that runs on petrol with a 1.5L engine.
 
```java
// Class → Car blueprint
class Car {
    String brand;
    String color;
    int topSpeed;

    void drive() {
        System.out.println("Driving the car...");
    }
}

// Object → Actual Car
Car hondaCity = new Car();
hondaCity.brand = "Honda";
hondaCity.color = "Red";
hondaCity.topSpeed = 180;
```
### Why Classes and Objects Matter in Low-Level Design?
- Classes allow you to model real-world entities like User, Product, Order, etc., by abstracting properties and behaviors.
- Once defined, a class can be reused to create multiple objects — reducing duplication.
- Classes encapsulate data (fields) and behavior (methods) together, enabling better modularity.
- In real systems, you may have UserController, UserService, and UserRepository — all modeled as classes, keeping code organized.
- You can extend or modify behavior using inheritance or composition — critical in scalable product design.
- Classes can be mocked/stubbed easily in unit testing, allowing clean test architecture.
- You design systems like Parking Lots or Cab Booking by identifying real-world entities as classes. Each class models a behavior clearly.
- LLD is all about thinking in terms of objects — identifying what entities exist in the system and how they interact. 

## Access Modifiers (private, protected, public)
- Access Modifiers in Java define how visible a class, field, constructor, or method is to other parts of your code.

| Modifier | Same class | Same package | Subclass(any package)| Anywhere |
| --- | --- | --- | --- | --- |
| `private` | :white_check_mark: | :x:| :x:| :x:|
| default | :white_check_mark:| :white_check_mark:| :x:| :x:|
| `protected` | :white_check_mark:| :white_check_mark:| :white_check_mark:| :x:|
| `public` | :white_check_mark:| :white_check_mark:| :white_check_mark:| :white_check_mark:|

- Take the example of a Bank Vault System.
    - `private` - Vault lock code (only the vault itself knows it)
    - `default` - Internal intercom system (used within the same bank branch)
    - `protected` - Branch Manager access card (shared with subordinates from related branches)
    - `public` - Front Desk Information (Any visitor can see).

### Significance of Access Modifiers in LLD
- It aids in encapsulation by exposing only whats necessary outside the classes.
- Internal/fields and methods are safe.
- `private` and `protected` reflect clear boundaries of responsibility.
- Public interfaces can be unit-tested and mocked easily.
- Private methods can be changed freely without affecting other modules.
- In a real product you want to protect critical internal logic from being invoked externally.
- It is ideal to keep the internal state of each class private and expose only domain-specific behavior via public methods. 
- If there's some shared logic within a package or for subclassing, It is optimal to consider protected or default.
```java
public class BankAccount {

    // private field: only this class can access it
    private double balance;

    // public constructor: accessible to anyone
    public BankAccount(double initialAmount) {
        if (initialAmount < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.balance = initialAmount;
    }

    // public method: safe way to get current balance
    public double getBalance() {
        return balance;
    }

    // public method: deposit money (validations done here)
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive.");
            return;
        }
        balance += amount;
    }

    // public method: withdraw with validation
    public void withdraw(double amount) {
        if (amount > balance) {
            System.out.println("Insufficient funds.");
            return;
        }
        balance -= amount;
    }

    // private method: internal logic for fraud detection
    private boolean isSuspiciousTransaction(double amount) {
        return amount > 1000000;
    }
}

---

public class Main {
    public static void main(String[] args) {
        BankAccount account = new BankAccount(5000);
        account.deposit(1000);
        System.out.println(account.getBalance());  // ✅ Works

        // account.balance = 0;        ❌ Compile error: balance is private
        // account.isSuspiciousTransaction(5000);  ❌ private method
    }
}
```
## Fields and Methods
- Fields (also known as attributes, instance variables, or data members) are variables defined inside a class that hold the state of an object.
- Methods are blocks of code inside a class that define the object’s behavior — what the object can do or what can be done to it.

## Constructors
- Constructor is a special method in a class that is used to create and initialize an object when it is instantiated.
    - It has the same name as the class.
    - It does not have a return type not even `void`.
    - It is automatically called when you use `new ClassName()`.

### Significance of Constructors in LLD
- Constructors help us in creating valid object with usable state.
- Constructors can enforce required fields (parametrized constructor).
- We can inject dependencies via constructors thereby improving testability.
- Constructors are used internally in Builder Pattern to create complex objects safely.

### Types
- Default Constructor or No-arg constructor.
- Parametrized Constructor - takes arguments to initialize the object.
- Constructor overloading - Multiple constructors with different parameter sets.
- Private constructor - Used in Singleton or static factory patterns. 
- Copy constructor (manual in Java) - Custom constructor to create a new object from an existing one.  

## Creational Design Pattern
- A Creational Design Pattern is a category of design patterns that focuses on how objects are created, rather than the objects themselves.
- They abstract and control the object creation process.
- They make systems that are more flexible, less tightly coupled and easier to maintain and extend.
- They provide better ways for creating objects without simply using `new` keyword.
#### Singleton
- They abstract and control the object creation process
- Useful for
    - Loggers
    - Configuration Manager
    - Cache Manager

#### Factory method
- Move object creation into a factory class.
#### Abstract Factory
- Creates families of related objects.
#### Builder
- Construct complex objects step-by-step.
- Useful when objects have many optional fields.

```java
User user = User.builder()
        .name("John")
        .email("john@email.com")
        .age(32)
        .build();
```
#### Prototype
- Create objects by cloning an existing object.
- Useful when object creation is expensive.
- Useful when many objects share similar structure.

## Singleton
- The Singleton Pattern is a Creational Design Pattern that ensures that a class has only one instance throughout the application lifecycle and provides a global access point to that instance.
- Useful for Loggers, Cache Manager, Database Connection Pool

```java
public class Singleton {

    private static volatile Singleton instance;  
    // volatile ensures visibility across threads

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {  // First check (no locking)
            synchronized (Singleton.class) {
                if (instance == null) {  // Second check (with locking)
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```
```java
public class SingletonTest{
    public static void main(String[] args){

        Singleton s1 = Singleton.getInstance();
        Singleton s2 = Singleton.getInstance();

        System.out.println("Are s1 and s2 references same - "+ s1 == s2);

        System.out.println("s1 - "+s1.hashCode);
        System.out.println("s2 - "+s2.hashCode);

        Runnable task = () ->{
            Singleton s3 = Singleton.getInstance();
            System.out.println(Thread.currentThread().getName() +" - "+ s3.hashCode());
        }
    
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        Thread t3 = new Thread(task);

        t1.start();
        t2.start();
        t3.start();
    }
}

//OP
Are s1 and s2 references same - true
s1 - 692404036
s2 - 692404036
Thread-1 - 692404036
Thread-0 - 692404036
Thread-2 - 692404036
```

## Builder Pattern
- The Builder Pattern is a creational design pattern used to construct complex objects step-by-step, especially when:
    - An object has many fields.
    - Some fields are optional.
    - Constructor parameter list become messy.
    - Immutability is required.
- It is useful for:
    - API Response Object
    - Config object
    - Domain Entity
    - Request detection
    - Event Object
- Without Builder, creating an object becomes hard and order dependent.
```java
User user = new User("John", "john@email.com", null, null, 32);
```
#### Characteristics
- Builder pattern improves readability.

```java
User user = User.builder()
                .name("John")
                .email("john@email.com")
                .age(32)
                .build();
```
- Builder pattern supports Immutability by making all fields final.
- Handles optional parameters cleanly.
- Avoids telescoping Constructor anti-pattern
```java
User(String name)
User(String name, String email)
User(String name, String email, String phone)
User(String name, String email, String phone, String address)
```
#### Implementation

```java
public class User {

    private final String name;
    private final String email;
    private final String phone;
    private final int age;

    // Private constructor — only Builder can create object and Developer can't directly call this constructor
    private User(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.phone = builder.phone;
        this.age = builder.age;
    }

    // Static method to get builder
    public static Builder builder() {
        return new Builder();
    }

    // Static inner Builder class
    public static class Builder {
        private String name;
        private String email;
        private String phone;
        private int age;

        public Builder name(String name) {
            this.name = name;
            return this;  // enables chaining
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

        // Final build method
        public User build() {
            // Optional validation
            if (name == null || email == null) {
                throw new IllegalStateException("Name and Email are required");
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
```

```java
public class Main {
    public static void main(String[] args) {

        User user = User.builder()
                        .name("John")
                        .email("john@email.com")
                        .age(32)
                        .build();

        System.out.println(user);
    }
}
```
##### Execution
- Assume that we are creating a user
```java
        User user = User.builder()
                        .name("John")
                        .email("john@email.com")
                        .age(32)
                        .build();
```
- When `User.builder()` is called, it invokes the static method `builder()`, this creates a new Builder object.
```java
public static Builder builder() {
    return new Builder();
}
```
- When `.name("John")` is called it invokes

```java
public Builder name(String name) {
    this.name = name;
    return this;
}
```
- The same is repeated for `.email(john@email.com)` and `.age(32)`.
- When `.build()` is called, it invokes
```java
public User build() {
    if (name == null || email == null) {
        throw new IllegalStateException("Name and Email are required");
    }
    return new User(this);
}
```
- `return new User(this);` invokes the private constructor `User(Builder builder)` which creates the Immutable user object.
```java
private User(Builder builder) {
    this.name = builder.name;
    this.email = builder.email;
    this.phone = builder.phone;
    this.age = builder.age;
}
```

## Factory Method Pattern
- The Factory Method Pattern provides an interface (or method) for creating objects, but lets subclasses or a dedicated factory decide which concrete object to instantiate.
- Instead of creating objects directly with `new`, you delegate the creation logic to a factory method.
#### Implementation

```java
// Product Interface
public interface Payment {
    void processPayment(double amount);
}
```

```java
//Concrete Implementation - 1
public class CardPayment implements Payment {

    @Override
    public void processPayment(double amount) {
        System.out.println("Processing card payment of ₹" + amount);
    }
}

//Concrete Implementation - 2
public class UpiPayment implements Payment {

    @Override
    public void processPayment(double amount) {
        System.out.println("Processing UPI payment of ₹" + amount);
    }
}

//Concrete Implementation - 3
public class NetBankingPayment implements Payment {

    @Override
    public void processPayment(double amount) {
        System.out.println("Processing Net Banking payment of ₹" + amount);
    }
}
```

```java
//Factory Class
public class PaymentFactory {

    public static Payment createPayment(String type){

        if(type.equalsIgnoreCase("CARD")){
            return new CardPayment();
        }

        if(type.equalsIgnoreCase("UPI")){
            return new UpiPayment();
        }

        if(type.equalsIgnoreCase("NETBANKING")){
            return new NetBankingPayment();
        }

        throw new IllegalArgumentException("Invalid payment type");
    }
}
```

```java
// Main class and method
public class Main {

    public static void main(String[] args) {

        Payment payment = PaymentFactory.createPayment("UPI");

        payment.processPayment(1000);
    }
}
```

## Structural Pattern
- A Structural Design Pattern focuses on how classes and objects are combined to build larger, flexible, and maintainable structures.
- They define how components are connected together.
- In real software systems, objects rarely exist alone. These components must interact in a structured way. Structural patterns provide clean ways to organize these relationships.

## Adapter Pattern
- The Adapter Pattern allows two incompatible interfaces to work together by converting the interface of one class into another interface that the client expects.
- It translates one interface into another.

## Facade Pattern
- The Facade Pattern provides a simple, unified interface to a complex subsystem.
- Instead of the client interacting with multiple classes, it interacts with one simplified interface (the facade) that coordinates everything.

## Behavioral Pattern
- A Behavioral Pattern focuses on how objects communicate, collaborate, and distribute responsibilities within a system.
- They deal with object interaction and behavior.
- In large systems, objects constantly interact
