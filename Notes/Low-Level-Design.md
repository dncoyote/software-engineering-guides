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
