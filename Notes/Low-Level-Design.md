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

## OOP
- Object-Oriented Programming (OOP) is a programming paradigm that organizes software design around objects, which combine data (state) and behavior (methods).
- OOP emphasizes the organization of software as a collection of objects that interact with each other to perform tasks.
## Encapsulation
- Encapsulation is the process of bundling data (fields) and behavior (methods) together and restricting direct access to the internal state of an object.
- It protects the object state and enforces business rule.
- Encapsulation is not just getters and setters
```java
//Without encapsulation
class BankAccount {
    public double balance; // ❌ directly accessible
}

//usage
BankAccount acc = new BankAccount();
acc.balance = -10000; // ❌ invalid state possible

//With encapsulation
public class BankAccount {

    // Private field → hidden state
    private double balance;

    // Constructor
    public BankAccount(double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
        this.balance = initialBalance;
    }

    // Controlled behavior
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid deposit");
        }
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        balance -= amount;
    }

    // Read-only access
    public double getBalance() {
        return balance;
    }
}
```
## Abstraction
- Abstraction is the process of hiding implementation details and exposing only the essential features or behavior.
- Abstraction is achieved in java using Interfaces and Abstract classes.
- Abstraction achieves
    - Loose coupling
    - Replaceability - you can swap implementation.
    - Scalability
    - Cleaner code 

```java
public interface PaymentProcessor {
    void processPayment(double amount);
}
```

```java
// Implementation
public class UpiPayment implements PaymentProcessor {

    @Override
    public void processPayment(double amount) {
        System.out.println("Processing UPI payment: " + amount);
    }
}

public class CardPayment implements PaymentProcessor {

    @Override
    public void processPayment(double amount) {
        System.out.println("Processing Card payment: " + amount);
    }
}
```

```java
public class Main {

    public static void main(String[] args) {

        PaymentProcessor payment = new UpiPayment(); // can switch easily

        payment.processPayment(1000);
    }
}
```
## Inheritance
- Inheritance is the mechanism where one class (child/subclass/derived class) can acquire the properties and behaviors (fields and methods) of another class (parent/superclass/base class).
- Code reusability → reuse fields & methods instead of duplicating.
- Polymorphism → allows one reference type to point to multiple object types.
- Extensibility → build more specific classes from generic ones.
- Standardization → all subclasses inherit common behavior.
- `final` class cannot be inherited, `final` method cannot be overridden.

```java
class Animal {
    Animal() {
        System.out.println("Animal created");
    }
}

class Dog extends Animal {
    Dog() {
        super(); // calls Animal()
        System.out.println("Dog created");
    }
}
```

```java
class Vehicle {
    String brand = "Generic";

    void start() {
        System.out.println("Vehicle starting...");
    }
}

class Car extends Vehicle {
    int wheels = 4;

    @Override
    void start() {
        System.out.println(brand + " car starting with " + wheels + " wheels");
    }
}

public class Main {
    public static void main(String[] args) {
        Vehicle v = new Car();  // polymorphism
        v.start(); // Car’s overridden method
    }
}
```
#### Types of Inheritance
##### Single Inheritance
```java
class Parent {}
class Child extends Parent {}
```
##### Multilevel Inheritance
```java
class Grandparent {}
class Parent extends Grandparent {}
class Child extends Parent {}
```
##### Hierarchical Inheritance
```java
class Parent {}
class Child1 extends Parent {}
class Child2 extends Parent {}
```
##### Multiple Inheritance
- Unlike C++ or Python, Java does not allow multiple inheritance with classes (to avoid ambiguity, e.g., diamond problem). Instead, it uses interfaces.
```java
interface A {}
interface B {}
class C implements A, B {}
```
## Polymorphism
- The ability of an object to take multiple forms.
#### Method Overriding
- Method overriding happens when a subclass provides a new implementation for a method that is already defined in its superclass.
- It allows the subclass to customize or completely change behavior while keeping the same method signature.
- It should have the same methods name, parameter list and return type.
- It should not reduce visibility.
    - If parent method is `public`, then child method must be `public`.
    - If parent method is `protected` then child method can be `protected` or `public`.
- It should not throw new or broader checked exceptions than parent method.
```java
class Parent {
    void read() throws IOException {}
}

class Child extends Parent {
    @Override
    void read() throws FileNotFoundException {} // ✅ allowed (narrower)
}
```
```java
class Child2 extends Parent {
    @Override
    void read() throws Exception {} // ❌ broader checked exception not allowed
}
```
- Final methods, static methods, private methods and constructors cannot be overridden.

```java
class Animal {
    void sound() {
        System.out.println("Some generic sound");
    }
}

class Dog extends Animal {
    @Override
    void sound() {
        System.out.println("Bark");
    }
}

public class Main {
    public static void main(String[] args) {
        Animal a = new Dog();
        a.sound(); // Bark (runtime polymorphism)
    }
}
```
- Even though reference type is `Animal`, the actual object (`Dog`) determines which method is called.
- This is runtime polymorphism (dynamic dispatch).
- Use `@Override` Annotation for clarity, it gives a compile-time error if the method doesn’t correctly override. 
```java
class Cat extends Animal {
    @Override
    void sound() { // ✅ correct override
        System.out.println("Meow");
    }
}
```
#### Method Overriding vs Runtime Polymorphism vs Dynamic Method dispatch
##### Method Overriding (what you write in code) 
- Method overriding happens when a subclass provides a new implementation for a method that is already defined in its superclass.
##### Runtime Polymorphism (what you achieve)
- The ability of a superclass reference to point to objects of different subclasses, and the correct method implementation is chosen at runtime.
- This is achieved through method overriding.
##### Dynamic Method Dispatch (how JVM actually does this) 
- The mechanism inside the JVM that makes runtime polymorphism possible.
- When an overridden method is called through a superclass reference, Java uses dynamic method dispatch to determine which version to execute.
- The method call is resolved dynamically at runtime, not statically at compile time.

#### Method Overloading
- Method overloading allows multiple methods with the same name but different parameter lists, enabling compile-time polymorphism 

```java
class Calculator {

    int add(int a, int b) {
        return a + b;
    }

    double add(double a, double b) {
        return a + b;
    }

    int add(int a, int b, int c) {
        return a + b + c;
    }
}
```
- Valid ways to overloading
    - Different number of parameters.
    - Different parameter types.
    - Different parameter order.
```java
void log(String msg)
void log(String msg, int level)

    
void process(int x)
void process(double x)


void draw(int x, double y)
void draw(double y, int x)
```
- Simply changing the return type is not a valid way to overload.
```java
int add(int a, int b)
double add(int a, int b) // ❌ compile-time error
```

## SOLID principles
- SOLID is a set of 5 design principles that help you write clean, maintainable, scalable, and extensible code.
- S	Single Responsibility Principle
- O	Open/Closed Principle
- L	Liskov Substitution Principle
- I	Interface Segregation Principle
- D	Dependency Inversion Principle
- Without SOLID:
    - tightly coupled code
    - hard to extend
    - fragile systems
    - code breaks on small changes
- With SOLID:
    - flexible design
    - easy to extend
    - testable systems
    - reusable components
- SOLID is guidance, not religion.

## Single Responsibility Principle (SRP)
- A class should have only one reason to change.
- SRP improves maintainability, testability, and reduces side effects when requirements change.
#### Example

```java
public class InvoiceService {

    public void calculateTotal(Order order) {
        // Business logic for total calculation
    }

    public void saveToDatabase(Order order) {
        // Persistence logic
    }

    public void sendInvoiceEmail(Order order) {
        // Email sending logic
    }

    public void generatePdf(Order order) {
        // PDF generation logic
    }
}
```
- This class has multiple responsibilities and therefore multiple reasons to change.
    - billing calculation
    - database persistence
    - email notification
    - PDF generation

```java
public class InvoiceCalculator {

    public double calculateTotal(Order order) {
        // Business logic for total calculation
        return order.getItems()
                .stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
```
```java
public class InvoiceRepository {

    public void save(Order order) {
        // Save invoice/order to database
    }
}
```
```java
public class InvoiceEmailService {

    public void sendInvoice(Order order) {
        // Send invoice email
    }
}
```
```java
public class InvoicePdfGenerator {

    public byte[] generate(Order order) {
        // Generate PDF and return as bytes
        return new byte[0];
    }
}
```
- In a Spring Boot application, SRP usually shows up as separation of:
    - Controller → handles HTTP request/response
    - Service → contains business logic
    - Repository → persistence logic
    - Mapper → conversion between DTO and entity
    - Validator → validation logic
    - Client → external API integration

## Open/Closed Principle (OCP) 
- Software entities should be open for extension, closed for modification. 
- When a new requirement comes, the ideal design lets you add a new class, not edit a giant chain of `if-else`.
- This means you should be able to add new behavior, without changing already-tested stable code too much.
- OCP is commonly achieved using interfaces, composition, and design patterns like Strategy or Factory.
#### Example

```java
public class NotificationService {

    public void send(String type, String message) {
        if ("EMAIL".equals(type)) {
            System.out.println("Sending email: " + message);
        } else if ("SMS".equals(type)) {
            System.out.println("Sending SMS: " + message);
        } else if ("PUSH".equals(type)) {
            System.out.println("Sending Push Notification: " + message);
        }
    }
}
```
- Everytime a new Notification system is implemented
    - it modifies existing class
    - which risks breaking old logic 

```java
public interface NotificationSender {
    void send(String message);
}
```
```java
public class EmailNotificationSender implements NotificationSender {
    @Override
    public void send(String message) {
        System.out.println("Sending email: " + message);
    }
}
```
```java
public class SmsNotificationSender implements NotificationSender {
    @Override
    public void send(String message) {
        System.out.println("Sending SMS: " + message);
    }
}
```
```java
public class PushNotificationSender implements NotificationSender {
    @Override
    public void send(String message) {
        System.out.println("Sending push notification: " + message);
    }
}
```
- New channels like WhatsApp can be added without modifying the core abstraction.
## Liskov Substitution Principle (LSP)
- Subtypes must be replaceable with their base types without breaking behavior.
- If `B` is a subtype of `A`, then I should be able to use `B` wherever `A` is expected.
- If a subclass changes expected behavior, throws unsupported exceptions for inherited methods, or violates the parent contract, then inheritance is wrong and composition or better abstractions should be used.
#### Example

```java
public class PaymentProcessor {
    public void processRefund(double amount) {
        // default refund logic
    }
}

public class CryptoPaymentProcessor extends PaymentProcessor {
    @Override
    public void processRefund(double amount) {
        throw new UnsupportedOperationException("Refund not supported");
    }
}
```
- Here `CryptoPaymentProcessor` does not support `processRefund` but we are forced to implement it.

```java
public interface PaymentProcessor {
    void processPayment(double amount);
}
```
```java
public interface RefundablePaymentProcessor extends PaymentProcessor {
    void processRefund(double amount);
}
```
```java
public class CardPaymentProcessor implements RefundablePaymentProcessor {

    @Override
    public void processPayment(double amount) {
        System.out.println("Card payment processed: " + amount);
    }

    @Override
    public void processRefund(double amount) {
        System.out.println("Card refund processed: " + amount);
    }
}
```
```java
public class CryptoPaymentProcessor implements PaymentProcessor {

    @Override
    public void processPayment(double amount) {
        System.out.println("Crypto payment processed: " + amount);
    }
}
```
## Interface Segregation Principle (ISP)
- Clients should not be forced to depend on methods they do not use.
- ISP means we should design small, focused interfaces so clients only depend on what they actually use. Large interfaces often cause fragile implementations, dummy methods, and unsupported operations
#### Example

```java
// God Interface
public interface FileStorageService {

    void upload(String fileName, byte[] data);

    byte[] download(String fileName);

    void delete(String fileName);

    String generatePreSignedUrl(String fileName);

    void restoreVersion(String fileName, int version);

    void applyLifecyclePolicy(String fileName);
}
```

```java
public class LocalFileStorageService implements FileStorageService {

    @Override
    public void upload(String fileName, byte[] data) {
        System.out.println("Saving file locally: " + fileName);
    }

    @Override
    public byte[] download(String fileName) {
        System.out.println("Reading file locally: " + fileName);
        return new byte[0];
    }

    @Override
    public void delete(String fileName) {
        System.out.println("Deleting file locally: " + fileName);
    }

    @Override
    public String generatePreSignedUrl(String fileName) {
        // ❌ Not supported
        throw new UnsupportedOperationException("Pre-signed URL not supported");
    }

    @Override
    public void restoreVersion(String fileName, int version) {
        // ❌ Not supported
        throw new UnsupportedOperationException("Versioning not supported");
    }

    @Override
    public void applyLifecyclePolicy(String fileName) {
        // ❌ Not supported
        throw new UnsupportedOperationException("Lifecycle policy not supported");
    }
}
```
- Methods throw UnsupportedOperationException and there is tight coupling.

```java
public interface FileUploader {
    void upload(String fileName, byte[] data);
}
```
```java
public interface FileDownloader {
    byte[] download(String fileName);
}
```
```java
public interface FileDeleter {
    void delete(String fileName);
}
```
```java
public interface PreSignedUrlGenerator {
    String generatePreSignedUrl(String fileName);
}
```
```java
public interface VersioningSupport {
    void restoreVersion(String fileName, int version);
}
```
- Implement only what is supported.
```java
// Local Storage
public class LocalFileStorageService
        implements FileUploader, FileDownloader, FileDeleter {

    @Override
    public void upload(String fileName, byte[] data) {
        System.out.println("Saving file locally: " + fileName);
    }

    @Override
    public byte[] download(String fileName) {
        System.out.println("Reading file locally: " + fileName);
        return new byte[0];
    }

    @Override
    public void delete(String fileName) {
        System.out.println("Deleting file locally: " + fileName);
    }
}
```

```java
// AWS S3
public class S3FileStorageService
        implements FileUploader, FileDownloader, FileDeleter,
                   PreSignedUrlGenerator, VersioningSupport {

    @Override
    public void upload(String fileName, byte[] data) {
        System.out.println("Uploading to S3: " + fileName);
    }

    @Override
    public byte[] download(String fileName) {
        System.out.println("Downloading from S3: " + fileName);
        return new byte[0];
    }

    @Override
    public void delete(String fileName) {
        System.out.println("Deleting from S3: " + fileName);
    }

    @Override
    public String generatePreSignedUrl(String fileName) {
        return "https://s3-url/" + fileName;
    }

    @Override
    public void restoreVersion(String fileName, int version) {
        System.out.println("Restoring version " + version + " from S3");
    }
}
```
- Clients only depend on what they need.

```java
// Upload service
public class FileUploadService {

    private final FileUploader uploader;

    public FileUploadService(FileUploader uploader) {
        this.uploader = uploader;
    }

    public void uploadFile(String fileName, byte[] data) {
        uploader.upload(fileName, data);
    }
}
```

```java
public class SecureDownloadService {

    private final PreSignedUrlGenerator urlGenerator;

    public SecureDownloadService(PreSignedUrlGenerator urlGenerator) {
        this.urlGenerator = urlGenerator;
    }

    public String getDownloadLink(String fileName) {
        return urlGenerator.generatePreSignedUrl(fileName);
    }
}
```
- In order to resolve which implementation to choose, we can :
    - Set a `@Primary`
    - Use `@Qualifier("s3")` and `@Qualifier("local")`.

## Dependency Inversion Principle (DIP)
- High-level modules should not depend on low-level modules.
- Both should depend on abstractions.
- Business logic should not be tightly coupled to specific implementations.
- This reduces coupling and makes the system easier to test, extend, and maintain
- In Spring, DIP is the design principle and Dependency Injection is the technique used to implement it.
#### Example
 
```java
public class EmailService {
    public void sendEmail(String message) {
        System.out.println("Sending email: " + message);
    }
}

public class OrderService {

    private final EmailService emailService = new EmailService();

    public void placeOrder() {
        System.out.println("Order placed");
        emailService.sendEmail("Your order has been placed");
    }
}
```
- Here `OrderService` is tightly coupled with `EmailService`.

```java
// abstraction
public interface NotificationService {
    void send(String message);
}
```
```java
// Concrete implementation
public class EmailNotificationService implements NotificationService {

    @Override
    public void send(String message) {
        System.out.println("Sending email: " + message);
    }
}
```
```java
// High level module depends on interface
public class OrderService {

    private final NotificationService notificationService;

    public OrderService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void placeOrder() {
        System.out.println("Order placed");
        notificationService.send("Your order has been placed");
    }
}
```
```java
//Main
public class Main {
    public static void main(String[] args) {
        NotificationService notificationService = new EmailNotificationService();
        OrderService orderService = new OrderService(notificationService);

        orderService.placeOrder();
    }
}
```
- Now `OrderService` does not care whether notification happens via email, SMS, or mock.

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
- The Factory Method Pattern is a Creational Design Pattern that provides an interface (or method) for creating objects, but lets subclasses or a dedicated factory decide which concrete object to instantiate.
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
- The Adapter Pattern is a Structural Design Pattern that allows two incompatible interfaces to work together by converting the interface of one class into another interface that the client expects.
- It translates one interface into another.

## Facade Pattern
- The Facade Pattern is Structural Design Pattern that provides a simple, unified interface to a complex subsystem.
- Instead of the client interacting with multiple classes, it interacts with one simplified interface (the facade) that coordinates everything

## Behavioral Pattern
- A Behavioral Pattern focuses on how objects communicate, collaborate, and distribute responsibilities within a system.
- They deal with object interaction and behavior.

## Strategy Pattern
- Strategy Pattern is used when we have multiple algorithms or behaviors for the same task. Instead of using conditionals, we define a common interface and implement each behavior as a separate class. The client selects the appropriate strategy at runtime. This follows Open/Closed Principle and improves extensibility and maintainability.
- Instead of hardcoding behavior, you pass behavior as an object.
 
