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

## `@Component`
- `@Component` is an annotation used to mark a class as a Spring-managed bean.
- When Spring Boot starts, it scans the project and automatically detects classes annotated with `@Component` and registers them in the `ApplicationContext`.
#### Internal flow
- App starts
- Spring Boot starts the `ApplicationContext`
-2️⃣ It performs Component Scanning
-3️⃣ It finds classes annotated with:

```
@Component
@Service
@Repository
@Controller
```
- 
- It creates objects (beans) and stores them in the IoC container
### Example
 
```java
@Component
public class PaymentService {

    public void processPayment() {
        System.out.println("Payment processed");
    }
}
```
- Now another class can use it via Dependency Injection.

```java
@Component
public class OrderService {

    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void placeOrder() {
        paymentService.processPayment();
    }
}
```
- Spring automatically injects `PaymentService`.
- Without `@Component`
```java
PaymentService paymentService = new PaymentService();
```
### Specialized Component Annotations

| Annotations| Used for|
| --- | --- |
|`@Component` | Generic component|
| `@Service`| Service layer|
| `@Repository`| Data access layer|
| `@Controller`| Web MVC controller|
| `@RestController`| Rest API controller|

## Bean
- A Spring Bean is simply an object that is created, managed, and stored by the Spring IoC container.
- Beans are stored in the Spring IoC Container, which is implemented by `ApplicationContext`.
### Bean Lifecycle
- Bean instantiation
    - Spring creates the Bean
- Dependency Injection
    - Spring resolves dependencies and injects `PaymentService` bean.
```java
@Service
public class OrderService {

    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```
- BeanPostProcessor
    - Spring allows custom logic to run before initialization. 
- Initialization
    - Spring now calls initialization callbacks `@PostConstruct`
- Bean in use
- Bean Destruction
    - When application shuts down, Spring destroys bean `@PreDestroy`.

```
ApplicationContext starts
        ↓
Instantiate Bean
        ↓
Inject Dependencies
        ↓
BeanPostProcessor (before init)
        ↓
@PostConstruct / init method
        ↓
BeanPostProcessor (after init)
        ↓
Bean ready
        ↓
Application shutdown
        ↓
@PreDestroy
```
### Bean Scopes
- By default, beans are singleton.

| Scope| Description|
| --- | --- |
| singleton| One instance per container|
| prototype| New instance every injection |
| request| One per HTTP request|
| session| One per HTTP session|

### `@Bean`
- `@Bean` is a core annotation in Spring used to explicitly define a bean inside a configuration class
- Spring can create beans in two main ways
    - Component Scanning
    - Java Configuration used in `@Bean`
- `@Bean` - When you cannot annotate the class, or you want manual control over bean creation.

```java
@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        return new HikariDataSource();
    }
}
```
## `@SpringBootApplication`
- `@SpringBootApplication` marks the typical Spring Boot entry point.
- It is a meta-annotation that combines three key Spring annotations
### `@Configuration`
- Marks the class as a configuration class and tells Spring that this class can define beans.
### `@EnableAutoConfiguration` 
- Tells Spring Boot to automatically configure beans based on dependencies in the class path.
- If you add `spring-boot-starter-web`, Spring Boot automatically configures
    - DispatcherServlet
    - Jackson ObjectMapper
    - Embedded Tomcat
    - Spring MVC configuration
### `@ComponentScan`
- Tells Spring to scan packages for components and detects `@Component` and specialized component classes.
 
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

## Filter 
- A Filter is a component that intercepts HTTP requests and responses at the Servlet container level before the request reaches Spring MVC.
- Filters are part of the Java Servlet specification, not Spring itself.
- They sits between the client and the web application and can modify requests and responses.
- Filters run before Spring MVC even starts processing the request.
- Filters are used for generic request processing tasks that apply to the entire application.
    - Authentication (Spring Security filters)
    - Logging
    - Request modification
    - Response modification
    - Compression
    - CORS handling
    - Rate limiting
    - Encoding Configuration
#### Filter interface
- Filters implement the Servlet API interface `jakarta.servlet.Filter`, it contains three main methods.
##### `init()`
- Runs when filter is initialized.
- Rarely used.

```java
@Override
public void init(FilterConfig filterConfig) {
    System.out.println("Filter initialized");
}
```
##### `doFilter()`
- `doFilter()` is where the filtering logic happens.
- `chain.doFilter()` passes control to the next filter or servlet.
##### `destry()`
- Called when filter is destroyed.
- Rarely used.
#### Example Filter

```java
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
                         throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        System.out.println("Incoming request: " + req.getRequestURI());

        chain.doFilter(request, response);
    }
}
```
- Spring Boot automatically registers filters annotated with `@Component`.
- Instead of `@Component`, you can register explicitly using `@Bean`.

## Interceptor
- An Interceptor in Spring MVC is a component that allows you to intercept HTTP requests before and after they reach the controller.
- It works within the Spring MVC layer and is managed by the DispatcherServlet.
- It can execute logic:
    - Before the controller method executes
    - After the controller method executes
    - After the complete request finishes
- Interceptors are used for cross-cutting concerns related to HTTP requests
    - Logging requests
    - Authentication checks
    - Rate limiting
    - Measuring response time
    - Adding headers
    - Request auditing
#### Lifecycle Methods of an Interceptor
- To create an interceptor in Spring MVC, you implement `HandlerInterceptor`, this interface provides three main methods.
##### `preHandle()`
- Runs before controller execution.
- Used for 
    - Authentication
    - Authorization
    - Logging
    - Blocking requests

```java
@Override
public boolean preHandle(HttpServletRequest request,
                         HttpServletResponse response,
                         Object handler) throws Exception {

    System.out.println("Request received: " + request.getRequestURI());

    return true; // continue request
}
```
- If `false` is returned, request stops here and the controller is never executed.

##### `postHandle()`
- Runs after controller execution but before response is sent.
- Used for
    - modifying response
    - adding attributes
    - logging response

```java
@Override
public void postHandle(HttpServletRequest request,
                       HttpServletResponse response,
                       Object handler,
                       ModelAndView modelAndView) {

    System.out.println("Controller executed");
}
```
#### `afterCompletion()`
- Runs after the request is fully completed.
- Used for
    - resource cleanup
    - logging
    - metrics

```java
@Override
public void afterCompletion(HttpServletRequest request,
                            HttpServletResponse response,
                            Object handler,
                            Exception ex) {

    System.out.println("Request completed");
}
```
### Example - Logging Interceptor

```java
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        System.out.println("Incoming request: " + request.getRequestURI());
        return true;
    }
}
```
- Telling Spring to run Interceptor for all `/api/**` endpoints.

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;

    public WebConfig(LoggingInterceptor loggingInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/api/**");
    }
}
```

## AOP
- AOP allows you to apply behavior around a method execution without modifying the method itself.
- AOP works using proxies that intercept method calls
- Many core Spring features—`@Transactional`, caching, security checks—are built on it.
###### What Problem AOP solves
- In large applications, certain kinds of logic appear everywhere in the codebase:
    - Logging
    - Transactions (`@Transactional`)
    - Security checks (`@PreAuthorize`)
    - Metrics
    - Caching (`@Cacheable`)
    - Retry logic (`@Retryable`)
- These concerns are called cross-cutting concerns because they cut across many modules.
- Without AOP, your business logic gets polluted and your core logic becomes mixed with infrastructure code.
```java
// Without AOP
public void transferMoney() {
    startTransaction();

    try {
        log("Transfer started");

        // business logic

        commitTransaction();
    } catch(Exception e) {
        rollbackTransaction();
    }
}

// With AOP
@Transactional
public void transferMoney() {
    // business logic only
}
```
- With AOP, Spring automatically starts a transaction, executes the method, commits or rolls back.
### Core concepts
##### Aspect
- A module containing cross-cutting logic like Transaction aspect, Logging aspect.
- It groups pointcuts and advice together.
##### Advice
- The actual code that runs when a join point is triggered.

| Advice| When it runs|
| --- | --- |
| @Before| before method|
| @After| after method|
| @AfterReturning | after successful execution|
| @AfterThrowing | after exception|
| @Around| wraps entire method (before + after)|
##### Joint point
- A point in program execution where advice can be applied.
```
paymentService.processPayment();
```
##### Pointcut
- Expression that determines which methods are intercepted.
```
execution(* com.example.service.*.*(..))
```
- This means intercept all methods in service package.
##### Target Object
- The original object whose method will run.
- This is the real class whose methods we want to intercept.
##### Proxy
- A wrapper object created by Spring that intercepts method calls.
#### Example
- Service class

```java
@Service
public class PaymentService {

    public void processPayment() {
        System.out.println("Processing payment...");
    }
}
```
- Aspect class

```java
@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.example.service.*.*(..))")
    public void logBefore() {

        System.out.println("Method execution started");
    }
}
```

```
Method execution started
Processing payment...
```
## Spring Proxy
- A Spring Proxy is a wrapper object created by Spring around your original bean to intercept method calls and apply additional behavior (like transactions, logging, security).
- Spring uses Spring Proxy when it needs to add behavior without modifying your code.
- @Transactional
- @Cacheable
- @Async
- @Retryable
- @CircuitBreaker
- Types
- JDK Dynamic Proxy
- CGLIB Proxy (default in Spring Boot)
```
userService.createUser();

// What Spring does
Proxy.createUser():
startTransaction()
try:
target.createUser()
commit()
catch:
rollback()
```

## `@Transactional`
- `@Transactional` is a Spring annotation used to manage database transactions declaratively.
- It ensures atomic operations.
- `@Transactional` ensures that a method executes within a database transaction.
- All DB operations inside the method are treated as one unit of work
- Either everything succeeds (commit)
- Or everything fails (rollback)
- It ensures that all operations within a method are executed within a transaction, and either all succeed or all fail.
- Spring implements it using AOP proxies that intercept method calls and handle transaction boundaries automatically.
- `@Transactional` should be placed in Service layer.
```
Client Request
↓
Controller
↓
Service (@Transactional)
↓
Proxy starts transaction
↓
Repository (DB operations)
↓
Commit / Rollback
↓
Response
```
### How Spring Implements `@Transactional`
- Spring implements `@Transactional` using AOP proxies + a Transaction Manager.
### Types of Transactions
##### Default Transaction
```
@Transactional
```
- It defines default behavior.
- propagation = REQUIRED
- isolation = DEFAULT
- rollback on `RuntimeException`
##### Read-only Transaction
```
@Transactional(readOnly = true)
```
- Its purpose is optimization
- Prevent accidental writes
- Hibernate skips dirty checking

##### Timeout
```
@Transactional(timeout = 5)
```
- Transaction fails if it exceeds 5 seconds.

##### Isolation Level
```
@Transactional(isolation = Isolation.READ_COMMITTED)
```
- Controls how transactions interact.
##### Rollback Rules
```
@Transactional(rollbackFor = Exception.class)
```
### Propagation
- Propagation defines how a transaction behaves when one transactional method calls another.
##### REQUIRED (Default)
```
@Transactional(propagation = Propagation.REQUIRED)
```
```
methodA (tx1)
↓
methodB joins tx1
```
- If transaction exists → join it
- Else → create new

##### REQUIRES_NEW
```
@Transactional(propagation = Propagation.REQUIRES_NEW)
```
```
methodA (tx1)
↓
methodB (tx2 new)
```
- Always creates a new transaction
- Suspends existing one

##### SUPPORTS
```
@Transactional(propagation = Propagation.SUPPORTS)
```
- If transaction exists → use it
- Else → run without transaction
-
##### NOT_SUPPORTED
```
@Transactional(propagation = Propagation.NOT_SUPPORTED)
```
```
methodA (tx1)
↓
methodB (no tx)
```
- Always runs without transaction
- Suspends existing transaction

##### MANDATORY
```
@Transactional(propagation = Propagation.MANDATORY)
```
- Must run inside existing transaction
- Else → throws exception

##### NEVER
```
@Transactional(propagation = Propagation.NEVER)
```
- Must NOT run inside transaction
- Else → throws exception
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
