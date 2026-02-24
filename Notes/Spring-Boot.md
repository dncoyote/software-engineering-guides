# Spring Boot

## What is Spring Boot? What is its significance? 
- Spring Boot is a framework built on top of Spring that simplifies application development by providing auto-configuration, embedded servers, and production-ready features, enabling developers to focus on business logic instead of boilerplate setup.
- Spring Boot is not a replacement for Spring, it merely simplifies it.
- It eliminates boilerplate and manual configuration and enables rapid, scalable and production-ready development.
- Spring Boot is build on three core foundational ideas
    - auto-configuration
    - starter dependencies
    - production readiness by default.

```
Your Code
   ↓
Spring Boot
   ↓
Spring Framework (IoC, DI, AOP)
   ↓
Embedded Server (Tomcat / Jetty / Netty)
   ↓
OS / Cloud / Container
```
## Spring MVC Design Pattern
- Spring MVC is an implementation of the Model-View-Controller (MVC) architectural pattern for building web applications.
- It separates the web application development into 3 layers:
    - Model → Business data
    - View → Presentation layer (HTML/JSON)
    - Controller → Request handling logic
- The most important component of the Spring MVC is Dispatcher Servlet
- It is the Front Controller in Spring MVC that receives all HTTP requests and delegates them to appropriate handlers
### Components of Spring MVC
- DispatcherServlet
    - It is the heart of the system and is the single entry point for all HTTP Requests.
- HandlerMapping
    - Maps the URL and matches it to the appropriate Controller method.
- HandlerAdapter
    - It invoked the matched method.
    - It also extracts the `@PathVariable` and injects parameters.
- Controller
    - Business Request Handling flow.
- Model
    - In REST apps Model -> Response Object
    - In HTML apps Model -> data passed to View
- ViewResolver
    - Used when returning HTML, not used in REST API's.
- HttpMessageConverters
    - Used in REST API's. It converts objects to JSON.

## DispatcherServlet
- DispatcherServlet is the Front Controller in Spring MVC that receives all HTTP requests and delegates them to appropriate handlers
- It is the single entry point for all HTTP Requests.
- DispatcherServlet is a standard servlet.
- In traditional Spring, you had to configure it manually in web.xml.
- In Spring Boot, it is auto-configured.
- DispatcherServlet bean is created during Spring Boot startup.
- If using `@Controller`, DispatcherServlet uses ViewResolver.
- If using `@RestController`, DispatcherServlet uses HttpMessageConverters.
#### Flow
- HTTP request arrives at server (Tomcat), Tomcat forwards to DispatcherServlet.
- DispatcherServlet receives request and HandlerMapping identifies which controller method handles this URL.
- HandlerMethod calls the identified method and resolves the arguments (`@PathVariable`, `@RequestBody`).
- Controller executes.
- Controller returns object after converting to JSON using HttpMessageConverters.
- DispatcherServlet writes JSON back to HTTP response.

## Inversion of Controller
- Inversion of Control is a design principle where the control of object creation and lifecycle is transferred from your application code to a framework/container.
- In Spring, this is implemented using Dependency Injection through the `ApplicationContext`, which manages beans and their lifecycle.
- Bean is simply an object managed by the IOC Container.
- During Spring Boot startup
    - Spring creates ApplicationContext
    - Scans for components
    - Creates bean definitions
    - Resolves dependencies
    - Instantiates beans
    - Injects dependencies
    - Starts the application 
- Traditional object control
    - `OrderService` controls lifecycle of `PaymentService`
    - It is tightly coupled
```java
class OrderService {
    private PaymentService paymentService = new PaymentService();
}
```
- IOC   
    - `OrderService` does not create `PaymentService`.
    - IOC instead provides `PaymentService` to `OrderService`.

```java
class OrderService {
    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```
- IOC is the principle and Dependency Injection is the technique used to implement IOC.

## Dependency Injection
- Dependency Injection is a technique where an object receives its dependencies from an external source instead of creating them itself.
- In Spring, Dependencies are injected by the IoC container (ApplicationContext)
### Types of Dependency Injection
#### Constructor Injection
```java
@Service
public class OrderService {

    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```
###### Why is Constructor Injection preferred
- Ensures immutability
- Makes dependencies explicit
- Improves testability
- Enables fail-fast behavior
- Detects circular dependencies early
#### Field Injection
```java
@Autowired
private PaymentService paymentService;
```
#### Setter Injection
```java
@Autowired
public void setPaymentService(PaymentService paymentService) {
    this.paymentService = paymentService;
}
```
## Circuit Breaker
- A Circuit Breaker is a design pattern used in distributed systems to prevent cascading failures by stopping calls to a failing service temporarily.
- Consider a system

```java
User Service → Payment Service → Bank API
```
- Now Bank API starts timing out.
    - Threads get blocked
    - Connection pool exhausts
    - Latency increases
    - Entire system slows down
    - Eventually → full outage
    - This is called : Cascading failure

### States
- CLOSED (Normal state)
    - Requests pass through
    - Failures are monitored
    - If failure threshold exceeded → OPEN
- OPEN (Fail Fast state)
    - Requests immediately fail
    - No call made to remote service
    - Wait for recovery time
    - Then move to HALF-OPEN
- HALF-OPEN (Trial state)
    - Allow limited requests
    - If successful → CLOSE
    - If failures again → OPEN
```java
@Service
public class PaymentService {

    @CircuitBreaker(name = "paymentService", fallbackMethod = "fallback")
    public PaymentResponse pay(PaymentRequest request) {
        return restTemplate.postForObject(
                "http://bank-service/pay",
                request,
                PaymentResponse.class
        );
    }

    public PaymentResponse fallback(PaymentRequest request, Throwable ex) {
        return new PaymentResponse("Payment service unavailable. Try later.");
    }
}

resilience4j:
  circuitbreaker:
    instances:
      paymentService:
        slidingWindowSize: 10
        failureRateThreshold: 50
        waitDurationInOpenState: 10s
        permittedNumberOfCallsInHalfOpenState: 3
```
### Flow
- When `pay()` is called:
- Proxy intercepts method
- Circuit breaker checks state
- If CLOSED → execute
- If failures exceed threshold → switch OPEN
- If OPEN → immediately call fallback
- After wait time → HALF-OPEN
- Trial calls determine next state

## End to End Spring Boot project
- So assuming that this a simple REST CRUD with DB (JPA) and validation, I’ll implement layered architecture: Controller → Service → Repository (DAO) → Entity, with DTOs, validation, and global exception handling.
- I am going to develop this for a User Management system.
- Structure
    - Entity: DB model + constraints
    - Repository (DAO): Spring Data JPA
    - DTOs: request/response (keep entities separate from API contracts using DTOs.)
    - Service: business rules + transactions
    - Controller: HTTP mapping + validation trigger
    - Global exception handler: consistent error payloads
```
com.company.app
  ├─ feature/user
  │   ├─ api (controller)
  │   ├─ dto (request/response)
  │   ├─ domain (entity)
  │   ├─ repo (dao)
  │   ├─ service
  └─ common/error (exceptions, handler)
```
- Dependencies 
    - spring-boot-starter-web (REST)
    - spring-boot-starter-validation (Bean Validation)
    - spring-boot-starter-data-jpa (JPA)
    - DB driver: postgresql (or h2 for speed)
    - Lombok
    - Actuator
    - Flyway
- API's
    - `POST /api/users` → create (201)
    - `GET /api/users/{id}` → fetch (200)
    - `GET /api/users` → list (200)
    - `PUT/PATCH /api/users/{id}` → update (200)
    - `DELETE /api/users/{id}` → delete (204) 
- Validations
    - Request DTO validation: `@NotBlank`, `@Email`, `@Size`
    - Service validation: uniqueness, existence checks
    - DB validation: unique constraint at DB level too
### Implementation
#### Project setup
- I will create a `pom.xml` with Spring Boot version 3.x, Java 17 and related necessary dependencies
    - spring-boot-starter-web (REST)
    - spring-boot-starter-validation (Bean Validation)
    - spring-boot-starter-data-jpa (JPA)
    - DB driver: postgresql driver 
    - Lombok
- I might add actuators or flyway dependencies when I am thinking production
- I will add `application.yml`
```
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/crud_demo
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update   # interview/dev only; prod uses Flyway + ddl-auto=validate
    properties:
      hibernate:
        format_sql: true
    show-sql: true
```
#### Entity layer
- I will create `User.java`

```java
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA needs it
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_users_email", columnNames = "email")
    },
    indexes = {
        @Index(name = "idx_users_email", columnList = "email")
    }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        this.active = true;
        this.createdAt = Instant.now();
    }
}
```
- Entity represents persistence model; I’m enforcing constraints at DB level too (unique email)
- I keep API contracts separate via DTOs to avoid exposing entities
- `@NoArgsConstructor(access = AccessLevel.PROTECTED)` - JPA/Hibernate needs a no-arg constructor to instantiate entities via reflection/proxying. 
- `@AllArgsConstructor` -  Generates a constructor with all fields.
- `@Builder` - Creates a builder API for fluent object construction.
- `@Entity` - Marks the class as a JPA entity mapped to a DB table.
- `@Table` - Defines table-level constraints and indexes.
- `@PrePersist` - JPA lifecycle callback that runs before INSERT. To set default fields consistently on creation.
#### Repository (DAO)
- I will create `UserRepository.java`

```java
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
```
- DAO/repository is only for persistence. Business rules stay in service layer.
- Spring Data generates queries for common predicates like `existsByEmail`
#### DTOs
- I will create `CreateUserRequest.java`, `UpdateUserRequest.java`, `UserResponse.java`

```java
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @NotBlank(message = "name is required")
    @Size(max = 100, message = "name must be <= 100 chars")
    private String name;

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    @Size(max = 150, message = "email must be <= 150 chars")
    private String email;
}
```

```java
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @Size(max = 100, message = "name must be <= 100 chars")
    private String name;

    @Email(message = "email must be valid")
    @Size(max = 150, message = "email must be <= 150 chars")
    private String email;

    private Boolean active;
}
```

```java
@Getter
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private boolean active;
    private Instant createdAt;
}
```

```java
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PutUserRequest {

    @NotBlank(message = "name is required")
    @Size(max = 100, message = "name must be <= 100 chars")
    private String name;

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    @Size(max = 150, message = "email must be <= 150 chars")
    private String email;

    @NotNull(message = "active is required")
    private Boolean active;
}
```
- I will also create the mapper classes `UserMapper.java`

```java
public final class UserMapper {

    private UserMapper() {}

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.isActive(),
                user.getCreatedAt()
        );
    }
}
```

- DTO validations are boundary checks; they prevent invalid request shapes early.
#### Exception layer

```java
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
}
```

```java
public class ConflictException extends RuntimeException {
    public ConflictException(String message) { super(message); }
}
```
#### Service layer
- I will create `UserService.java` and `UserServiceImpl.java`

```java
public interface UserService {
    UserResponse create(CreateUserRequest req);
    UserResponse get(Long id);
    List<UserResponse> list();
    UserResponse update(Long id, UpdateUserRequest req);
    UserResponse replace(Long id, PutUserRequest req);
    void delete(Long id);
}
```

```java
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    @Override
    @Transactional
    public UserResponse create(CreateUserRequest req) {
        // Business validation (semantic)
        if (repo.existsByEmail(req.getEmail())) {
            throw new ConflictException("email already exists");
        }

        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .build();

        User saved = repo.save(user);
        return UserMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse get(Long id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("user not found: " + id));
        return UserMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> list() {
        return repo.findAll().stream().map(UserMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public UserResponse update(Long id, UpdateUserRequest req) {
        User user = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("user not found: " + id));

        // PATCH semantics: only update provided fields
        if (req.getName() != null) user.setName(req.getName());

        if (req.getEmail() != null && !req.getEmail().equals(user.getEmail())) {
            if (repo.existsByEmail(req.getEmail())) {
                throw new ConflictException("email already exists");
            }
            user.setEmail(req.getEmail());
        }

        if (req.getActive() != null) user.setActive(req.getActive());

        User saved = repo.save(user);
        return UserMapper.toResponse(saved);
    }
    
    @Override
    @Transactional
    public UserResponse replace(Long id, PutUserRequest req) {
        User user = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("user not found: " + id));

        // PUT semantics: full replacement (except immutable fields like createdAt)
        // Ensure unique email if it changes
        if (!req.getEmail().equals(user.getEmail()) && repo.existsByEmail(req.getEmail())) {
            throw new ConflictException("email already exists");
        }

        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setActive(req.getActive());

        User saved = repo.save(user);
        return UserMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("user not found: " + id);
        }
        repo.deleteById(id);
    }
}
```
- Service owns business rules and transaction boundaries.
- I’m using `@Transactional` at service methods; `readOnly=true` for reads.
- `@Transactional` - Wraps a method in a DB transaction, method commits if success or roll backs incase of runtime exception.
- `@Transactional(readOnly = true)` - simple optimized for read operations like `get` or `list`.
#### Controller layer
- I will create `UserController.java`

```java
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody CreateUserRequest req) {
        return service.create(req);
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public List<UserResponse> list() {
        return service.list();
    }

    @PatchMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest req) {
        return service.update(id, req);
    }

    @PutMapping("/{id}")
    public UserResponse replace(@PathVariable Long id, @Valid @RequestBody PutUserRequest req) {
        return service.replace(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
```
- Controller stays thin: validation + request mapping only
- Using `@Valid` ensures request boundary validation, and service handles business rules
- `@RequestBody` - deserialize JSON → DTO using Jackson
- `PUT /users/{id}` - replaces the entire resource
- `PATCH /users/{id}` - Partial update
#### Global Exception handler 
- I will create `ApiError.java` and `GlobalExceptionHandler.java`

```java
@Getter
@AllArgsConstructor
public class ApiError {
    private Instant timestamp;
    private int status;
    private String message;
    private List<String> details;

    public static ApiError of(int status, String message, List<String> details) {
        return new ApiError(Instant.now(), status, message, details);
    }
}
```

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> notFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiError.of(404, ex.getMessage(), List.of()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> conflict(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiError.of(409, ex.getMessage(), List.of()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validation(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult().getAllErrors().stream()
                .map(err -> (err instanceof FieldError fe)
                        ? fe.getField() + ": " + fe.getDefaultMessage()
                        : err.getDefaultMessage()
                )
                .toList();

        return ResponseEntity.badRequest()
                .body(ApiError.of(400, "validation failed", details));
    }
}
```
- I centralize exception handling using `@RestControllerAdvice` to keep controllers clean and return consistent error contracts for clients.
- This keeps controllers clean and improves client-side reliability
- `@RestControllerAdvice` - It provides centralized exception handling across all controllers, ensuring consistent error responses, cleaner controller code, and better separation of concerns.
- `@ExceptionHandler(MethodArgumentNotValidException.class)` -  It tells Spring, If this type of exception occurs, use this method to handle it.
```java
@SpringBootApplication
public class CrudDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrudDemoApplication.class, args);
    }
}
```
