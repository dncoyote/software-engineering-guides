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
