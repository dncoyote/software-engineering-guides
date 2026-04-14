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

## Inversion of Control
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

## Constructor Injection vs Field Injection
### Constructor Injection
- Dependencies are provided via constructor parameters when the object is created.
```java
@Service
public class UserService {

    private final UserRepository userRepository;

    // Dependency is required at creation time
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```
- Spring resolves dependency first
- Object is created fully initialized
#### Characteristics
- Enforces Required Dependencies
- Supports Immutability - Once set, cannot be changed, so its thread safe and immutable
- Supports Clean Unit Testing
- Better Design when constructors have many dependencies.
### Field Injection
- Dependencies are injected directly into class fields using `@Autowired`.
```java
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository; // injected by Spring AFTER object creation

}
```
- Spring creates object → new `UserService()`
- Then uses reflection to inject `userRepository`
- Object is created in an incomplete state first

### Circular Dependency
- A circular dependency occurs when two or more beans depend on each other in a cycle.
- Constructor Injection exposes Circular Dependency.

## Application Properties 
- Configuration files used by Spring Boot to externalize settings like -
    - database configs
    - ports
    - feature flags
    - API keys
- We can store configuration in `application.properties` or `application.yml`

```
//application.properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/dev_db

//application.yml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/dev_db
```
- We can create different configs for different environments - application-dev.yml, application-prod.yml, application-test.yml
- We can mention which profile to use in command line interface or store and use in an env variable.
```
java -jar app.jar --spring.profiles.active=prod

//env var
export SPRING_PROFILES_ACTIVE=prod
```

### Accessing Properties
##### `@Value`
```java
@Value("${server.port}")
private int port;
```
##### `@ConfigurationProperties`
```java
@ConfigurationProperties(prefix = "app")
@Component
public class AppConfig {

    private String name;
    private int timeout;

    // getters and setters
}
```
```
app:
  name: MyApp
  timeout: 30
```

## Spring Profiles
- Spring Profile is a feature that allows you to activate different configurations and beans based on the environment your application is running in.
- This helps us to manage multiple environments in one application.
- We can have different configs for different environments - application.yml(default), application-dev.yml, application-prod.yml, application-test.yml
- During startup Spring checks active profile (dev) and loads application.yml, application-dev.yml
- Spring also merges configs (profile overrides base)
- We can also have multiple profiles as active by providing profiles as comma separated.

## `@Profile`
- `@Profile` is an annotation in Spring Framework used to conditionally load beans based on the active profile.
```java
@Configuration
public class DataSourceConfig {

    @Bean
    @Profile("dev")
    public DataSource devDataSource() {
        return new H2DataSource(); // in-memory DB
    }

    @Bean
    @Profile("prod")
    public DataSource prodDataSource() {
        return new MySQLDataSource(); // real DB
    }
}
```
- Only ONE bean is loaded depending on profile.

## `@RestController`
- `@RestController` is an annotation that marks a class as a REST API controller, where methods handle HTTP requests and return values are automatically converted to HTTP responses (usually JSON).
- `@RestController` is a combination of `@Controller` and `@ResponseBody`.
- Spring handles everything including request mapping, serialization, response building
```java
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) {
        Order order = service.createOrder(request);
        return ResponseEntity.ok(order);
    }
}
```

## `@Controller`
- Marks class as a web controller.
- Returns view (HTML) instead of JSON/XML.

## `@ResponseBody`
- `@ResponseBody` is an annotation used to convert return value of this method and write it directly to the HTTP response body (JSON/XML)
- Spring uses HttpMessageConverters to convert objects to JSON/XML.

## `@RequestBody`
- `@RequestBody`is an annotation used to bind the HTTP request body (usually JSON) to a Java object.
- Spring uses Jackson to covert JSON to Java object.
```java
//Clients sends JSON
{
  "name": "John",
  "email": "john@example.com"
}

// Spring automatically reads request body and converts JSON to Java object
@PostMapping("/users")
public User createUser(@RequestBody User user) {
    return user;
}
```

## `@PathVariable`
- `@PathVariable`is an annotation used to extract values from the URL path and bind them to method parameters.
- `@PathVariable` are used commonly for resource identifiers.
- Multiple path variables are possible.
```java
//Clients sends request
/users/101

// Spring automatically extracts {id} = 101 and converts Long
@GetMapping("/users/{id}")
public String getUser(@PathVariable Long id) {
    return "User ID: " + id;
}
```

## `@RequestParam`
- `@RequestParam`is an annotation used to extract values from the query parameters (or form data) of an HTTP request and bind them to method parameters.
- `@RequestParam` are used commonly for filtering / pagination / sorting / optional input.
- We can make it optional by `@RequestParam(required = false)`
```java
//Clients sends request
/users?page=1&size=10

// Spring automatically extracts page and size and converts Integer
@GetMapping("/users")
public String getUsers(@RequestParam Integer page,
                       @RequestParam Integer size) {
    return "Page: " + page + ", Size: " + size;
}
```


## `@RequestMapping`
- `@RequestMapping`is an annotation used to map HTTP requests (URL + method + other conditions) to handler methods or classes.
- Usually added at class level, we can add in method slso.
```java
@RestController
@RequestMapping("/orders")
public class OrderController {
}
```

## `@GetMapping`
- `@GetMapping` annotations is used to map HTTP requests of GET methods to controller methods.
- Retrieve data
- Idempotent - Yes
```java
@GetMapping("/users/{id}")
public User getUser(@PathVariable Long id) {
    return service.getUser(id);
}
```

## `@PostMapping`
- `@PostMapping` annotation is used to map HTTP requests of POST methods to controller methods.
- Create new resource
- Idempotent - No
- POST methods can be made idempotent by using Idempotency-key
```java
@PostMapping("/users")
public User createUser(@RequestBody CreateUserRequest req) {
    return service.create(req);
}
```

## `@PutMapping`
- `@PutMapping` annotation is used to map HTTP requests of PUT methods to controller methods.
- Update or replace resource
- Idempotent - Yes
```java
@PutMapping("/users/{id}")
public User updateUser(@PathVariable Long id,
                       @RequestBody UpdateUserRequest req) {
    return service.update(id, req);
}
```

## `@PatchMapping`
- `@PatchMapping` annotation is used to map HTTP requests of PATCH methods to controller methods.
- Updates only some fields
- Idempotent - No
```java
@PatchMapping("/users/{id}")
public User updateUser(@PathVariable Long id,
                       @RequestBody Map<String, Object> updates) {

    return service.partialUpdate(id, updates);
}
```

## `@DeleteMapping`
- `@DeleteMapping` annotation is used to map HTTP requests of DELETE methods to controller methods.
- Delete resource
- Idempotent - Yes
```java
@DeleteMapping("/users/{id}")
public void deleteUser(@PathVariable Long id) {
    service.delete(id);
}
```

## `@Valid`
- `@Valid` is an annotation used to trigger validation on an object using the constraints defined on its fields.
- Suppose we define object validations in DTO
```java
public class CreateUserRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email")
    private String email;

    // getters & setters
}
```
- Spring will NOT automatically validate unless told to by adding `@Valid`.
```java
@PostMapping("/users")
public User createUser(@Valid @RequestBody CreateUserRequest request) {
    return service.create(request);
}
```

## `@Primary`
- `@Primary` is an annotation  used to mark a bean as the default choice when multiple beans of the same type are present.
```java
@Service
@Primary
public class EmailService implements NotificationService {}

@Service
public class SmsService implements NotificationService {}


//Service layer
@Autowired
private NotificationService notificationService;

// Here EmailService is injected
```
- It also works with `@Bean`.

## `@Qualifier`
- `@Qualifier` is an annotation used to specify exactly which bean should be injected when multiple beans of the same type exist.
- It creates a default bean name from the class name in camelCase.
```java
@Service
public class EmailService implements NotificationService {}

@Service
public class SmsService implements NotificationService {}


// Service layer
@Service
public class UserService {

    private final NotificationService notificationService;

    public UserService(
        @Qualifier("smsService") NotificationService notificationService
    ) {
        this.notificationService = notificationService;
    }
}

// Here SmsService is injected
```

## Spring Data JPA
- Spring Data JPA is a module of Spring Boot that simplifies database access by providing abstraction over JPA (Java Persistence API) to interact with relational databases using repositories instead of boilerplate code.
- Without Spring Data JPA we will need to write too much boilerplate and error-prone code that is hard to maintain.
- Entities, Repository layer and Service layer form the core components of Spring Data JPA in an application.
- Spring Data JPA supports CRUD Operations (Out of the Box)
```java
repo.save(user);         // create/update
repo.findById(id);       // read
repo.findAll();          // read all
repo.deleteById(id);     // delete
```
- Spring Data JPA supports Query Methods that Spring generates automatically.
```java
List<User> findByEmail(String email);
List<User> findByNameAndAge(String name, int age);
```
- Spring Data JPA supports Custom queries using JPQL and Native SQL
```java
//JPQL
@Query("SELECT u FROM User u WHERE u.email = :email")
User findUserByEmail(@Param("email") String email);
```
```java
//Native SQL
@Query(value = "SELECT * FROM users WHERE email = ?", nativeQuery = true)
User findByEmail(String email);
```

## JpaRepository
- JpaRepository is an interface in Spring Data JPA that provides ready-to-use CRUD operations, pagination, and advanced database interaction methods for JPA entities.
```java
public interface UserRepository extends JpaRepository<User, Long> {
}
```
- CRUD Operations (Out of the Box)
```java
repo.save(user);         // create/update
repo.findById(id);       // read
repo.findAll();          // read all
repo.deleteById(id);     // delete
```
- Existence checks
```java
repo.existsById(id);
```
- Count
```java
repo.count();
```
- Batch Operations
```java
repo.saveAll(users);
repo.deleteAll();
```
- Pagination
```java
Page<User> page = repo.findAll(PageRequest.of(0, 10));
```
- Sorting
```java
repo.findAll(Sort.by("name").ascending());
```
- Query Methods
```java
List<User> findByEmail(String email);
List<User> findByNameAndAge(String name, int age);
```
- JPQL and Native SQL
```java
//JPQL
@Query("SELECT u FROM User u WHERE u.email = :email")
User findUserByEmail(@Param("email") String email);
```
```java
//Native SQL
@Query(value = "SELECT * FROM users WHERE email = ?", nativeQuery = true)
User findByEmail(String email);

```

## `@Component`
- `@Component` is an annotation used to mark a class as a Spring-managed bean.
- When Spring Boot starts, it scans the project and automatically detects classes annotated with `@Component` and registers them in the `ApplicationContext`.
#### Internal flow
- App starts
- Spring Boot starts the `ApplicationContext`
- It performs Component Scanning
- It finds classes annotated with:

```
@Component
@Service
@Repository
@Controller
```
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

## `@Bean` vs `@Component`
| Property   | `@Component`                                                                 | `@Bean`                                                                                   |
|------------|------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------|
| Definition | Marks a class so that Spring automatically detects and registers it as a bean during component scanning. | Marks a method inside a `@Configuration` class to manually create and register a bean. |
| Control    | Automatic — Spring controls object creation                                  | Manual — Developer decides how the bean is created                                       |
| Location   | On class                                                                     | On method                                                                                |
| Use case   | Your own classes                                                             | External / complex objects / third-party classes                                          |


## `@Stereotype`
- `@Stereotype` annotation is a marker annotation that tells Spring that a class is a Spring managed component (bean).

| Annotations| Used for|
| --- | --- |
|`@Component` | Generic component|
| `@Service`| Service layer|
| `@Repository`| Data access layer|
| `@Controller`| Web MVC controller|
| `@RestController`| Rest API controller|

## `@SpringBootApplication`
- `@SpringBootApplication` marks the typical Spring Boot entry point.
- It is a meta-annotation that combines three key Spring annotations
### `@Configuration`
- Marks the class as a configuration class and tells Spring that this class can define beans.
- Used for third-party classes, custom initialization, external configs, manual bean creation.
### `@EnableAutoConfiguration` 
- Tells Spring Boot to automatically configure beans based on dependencies in the class path.
- If you add `spring-boot-starter-web`, Spring Boot automatically configures
    - DispatcherServlet
    - Jackson ObjectMapper
    - Embedded Tomcat
    - Spring MVC configuration
### `@ComponentScan`
- Tells Spring to scan packages for components and detects `@Component` and specialized component classes like `@Component`, `@Service` etc.
- We can specify to scan specific packages or classes using `@ComponentScan(basePackages = "com")`, `@ComponentScan(basePackageClasses = UserService.class)`.

## `@ConfigurationProperties`
- `@ConfigurationProperties` is used to bind external configuration (application.yml/properties) to Java objects.
```java
app:
  name: MyApp
  timeout: 5000

@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String name;
    private int timeout;

    // getters/setters
}
```

## Spring Cloud 
- Spring Cloud is a set of tools and frameworks built on top of Spring Boot to help you build distributed systems and microservices.

#### Core Components
- Service Discovery

```java
@SpringBootApplication
@EnableDiscoveryClient
public class OrderServiceApplication {}
```

- API Gateway
- Load Balancer
```java
@Bean
@LoadBalanced
public RestTemplate restTemplate() {
    return new RestTemplate();
}
```
- Circuit Breaker
- Retry
- Config Server
- Distributed Tracing

## API Gateway
- API Gateway is an architectural component that acts as a single entry point for all client requests and routes them to appropriate backend services.
- Without API Gateway -
    - Client needs to keep track of too many endpoints.
    - Tight Coupling 
    - Security needs to be implemented across all microservices.
- Types of API Gateway 
    - Spring Cloud Gateway 
    - Kong
    - AWS API Gateway
    - Nginx
- Problems
    - Single Point of Failure
    - Increases Latency as there is an extra hop.
    - Increases Complexity.
```
Client (Web/Mobile)
        ↓
   API Gateway
   ↓    ↓    ↓
User  Order  Payment
Service Service Service
```    
##### Spring Cloud Gateway
- Spring Cloud Gateway is used to build an API Gateway that routes, filters, and manages traffic to microservices.
- Spring Cloud Gateway is built on Spring WebFlux, so its non-blocking, high throughput and scalable gateway.
### Features
##### Routing
```
/api/users → User Service  
/api/orders → Order Service

spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: http://localhost:8081
          predicates:
            - Path=/users/**
```
##### Authentication & Authorization
- Helps us implement a centralized security system using JWT validation, OAuth2 integration.
- IP Whitelisting / Blacklisting
- CORS Handling

##### Load Balancing
- Distribute requests across instances

##### Rate Limiting
- Prevent abuse and DDoS
- Throttle heavy users instead of blocking
- Implement Quotas by Limiting usage per API key/user
```
filters:
  - name: RequestRateLimiter
    args:
      redis-rate-limiter.replenishRate: 10
      redis-rate-limiter.burstCapacity: 20
```

##### Logging & Monitoring
- Helps us implement a centralized observability system.

##### Request/Response Processing using Filters
- Request/Response Transformation.
- Header manipulation.
- Payload validation.
```
filters:
  - AddRequestHeader=X-App, MyApp
```

##### Request Aggregation
- Combine multiple service calls

##### Fault Tolerance
- Circuit Breaker - Stop calling failing service
- Retry Mechanism - Retry failed requests
- Fallback Responses

##### Response Caching
- Implement Response Caching to reduce DB load.

##### Versioning
- Implement clean versioning to manage API evolution.


## Service Discovery
- Service Discovery is a mechanism that allows services to dynamically register, find and communicate with each other without hardcoding their locations (IP/port).
- Without Service Discovery, we will need to keep track of IP's of all microservices (containers) and all instances that come and go.
- Examples of API Gateway 
    - Netflix Eureka
    - Consul
    - Zookeeper
    - AWS ELB
- Types
    - Client-Side Discovery - client talks to registry and chooses the instance.
    - Server-Side Discovery - LB picks the instance
- Service Discovery also performs health checks to weed out dead instances.
- Service Discovery also performs Dynamic Scaling and Load Balancing activities.
```
          ┌──────────────┐
          │ Service      │
          │ Registry     │
          │ (Eureka)     │
          └──────┬───────┘
                 │
     ┌───────────┼───────────┐
     ↓           ↓           ↓
 User Service  Order Service Payment Service
     ↑
     │
   Client / Gateway
```
#### Implementation
- In Spring Boot, service discovery is typically implemented using Spring Cloud. A service registry such as Eureka or Consul is introduced, each service registers itself under a logical name like `order-service`, and consumer services discover instances through Spring’s `DiscoveryClient` abstraction or declarative clients like Feign. Spring Cloud LoadBalancer can then choose a specific instance for each request. In Kubernetes-based deployments, discovery is often backed by Kubernetes services instead of Eureka.
```
Order Service starts
   ↓
Registers itself in registry
   ↓
Registry stores: order-service -> [host1:8082, host2:8082]
   ↓
User Service asks registry for "order-service"
   ↓
Gets one or more instances
   ↓
Calls one of them
```
##### Add Spring Cloud discovery dependency
- For a Eureka-based setup, the client app adds the Eureka client starter, and the registry app adds the Eureka server starter.

##### Give the service a logical name
- That logical name is what other services discover, rather than using a hardcoded IP and port.
```
spring:
  application:
    name: order-service
```

##### Point the service to the registry
```
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

##### Start the registry
- A dedicated Spring Boot app runs as the registry server. 
- In the Eureka pattern, this is the central place where service instances register and where consumers look them up.

##### Register services automatically
- Once the discovery client dependency is on the classpath and configured, Spring Cloud wires in a discovery implementation.

##### Discover and call another service
- In code, this is commonly done through DiscoveryClient or OpenFeign with a logical service name
- Spring Cloud Gateway using discovery-based routing

## Load Balancer
- Load Balancer is a component that distributes incoming requests across multiple service instances to ensure high availability, scalability, and reliability.
- Without LB, a single service instance will be overloaded and if one system fails then the whole system fails.

### Types of LB
#### Layer 4 (Transport Layer)
- Works at TCP/UDP level
    - based on IP + Port
    - faster
    - less intelligent
- Example:
    - AWS NLB
#### Layer 7 (Application Layer)
- Works at HTTP level
    - path-based routing
    - header-based routing
    - smarter decisions
- Example:
    - Nginx
    - API Gateway
    - Spring Cloud Gateway

### LB Algorithms
#### Round Robin
```
Req1 → Instance1
Req2 → Instance2
Req3 → Instance3
```
- Simple, commonly used.

#### Least Connections
- Send request to least busy instance

#### Weighted Round Robin
```
Instance1 (weight 2)
Instance2 (weight 1)
```
- Instance1 gets more traffic.

#### IP Hash
- Same user gets to use the same server.

## Spring Cloud Config Server
- Spring Cloud Config Server is a centralized service that stores and serves configuration properties for multiple applications (microservices).
- Without Config Server, there is risk of inconsistency and config duplication, it is also hard to update config changes and we will need to redeploy all services for a single config change.
- It stores configuration in a Git repository and serves it to services at runtime. It supports environment-based configurations, dynamic refresh, and version control, helping maintain consistency and flexibility across distributed systems.
```
         +----------------------+
         |   Git Repository     |
         | (config files)       |
         +----------+-----------+
                    |
                    ↓
         +----------------------+
         | Config Server        |
         +----------+-----------+
                    |
     ----------------------------------
     ↓                ↓               ↓
order-service   payment-service   user-service
```
- Enable Config Server
```java
@SpringBootApplication
@EnableConfigServer  // Enables Config Server
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
```
- Configure Git Repository
```yaml
server:
  port: 8888

spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/your-repo/config-repo
```
- Git Repo Structure
```
config-repo/
 ├── application.yml
 ├── order-service.yml
 ├── payment-service.yml
 ├── user-service.yml
```
- Add some config in the yml files
```yaml
// order-service.yml
server:
  port: 8081

custom:
  message: "Hello from Config Server"
```
- In the Client app, add the config in yml and access them

```yaml
spring:
  application:
    name: order-service

  config:
    import: optional:configserver:http://localhost:8888
```

```java
@Value("${custom.message}")
private String message;

@GetMapping("/message")
public String getMessage() {
    return message;
}
```
## Rate Limiter
- Rate Limiter is a mechanism that restricts the number of requests a client can make within a specific time window.
- Without rate limiting -
    - server overload
    - DDoS attacks
    - resource exhaustion
- This mechanism protects API's, prevents abuse and scraping attempts.
- We can limit on the basis of IP,  user ID or API Key.
- Rate Limiter is generally applied at
    - API Gateway (most common)
    - Microservices
    - Load balancer layer
    - CDN (Cloudflare, Akamai)

### Implementation
##### Spring Cloud Gateway + Redis (Recommended)
- A Redis rate limiter is a distributed rate limiter that stores counters, tokens, timestamps, or bucket state in Redis so all app instances enforce the same limit.
- Without Redis, if you run 3 instances of your app and each keeps its own in-memory counter, the user can effectively get 3 times the intended quota. Redis fixes that by giving all instances one shared source of truth. This is why Redis-backed rate limiting is so common in gateways and public APIs
- Every request checks and updates rate-limit state in Redis, so the rule is enforced globally across all nodes (Shared state). 
- Token bucket is the most common algorithm used in Rate Limiter
###### Token Bucket
- Imagine a bucket:
    - max capacity = 20 tokens
    - refill rate = 10 tokens per second
    - each request consumes 1 token
- If the bucket has tokens, the request is allowed.
- If the bucket is empty, the request is rejected.
- This gives you both:
    - average sustained rate control
    - short burst tolerance
```
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/users/**
          filters:
            - name: RequestRateLimiter
              args:
                key-resolver: "#{@userKeyResolver}"
                redis-rate-limiter.replenishRate: 10
                redis-rate-limiter.burstCapacity: 20
                redis-rate-limiter.requestedTokens: 1
```
##### Resilience4j (Service Level)
```java
@RateLimiter(name = "userService")
public String getUser() {
    return "user";
}
```

## Caching
- Caching is a technique to store frequently accessed data in a faster storage layer to avoid repeated expensive computations or database calls.
- Without Caching, we will need to constantly query the DB increasing its loads, reducing response and it scales poorly.
- With Cache, first call is made to the DB and subsequent calls are from the cache.
- TTL - The duration for which a cached entry remains valid before it is automatically expired.
    - It is configured in the yml file.
```java
@Cacheable("users")
public User getUser(Long id) {
    return userRepository.findById(id);
}
```
#### Types
- In-Memory Cache
    - Stored inside application
    - They are fast but not distributed
    - Examples:
        - ConcurrentHashMap
        - Caffeine
- Distributed Cache
    - Shared across instances
    - They are Scalable and Consistent
    - Examples:
        - Redis
        - Memcached
- CDN
    - Edge Caching
        - Cloudflare
        - AWS Cloudfront

#### Caching in Spring Boot
- Enable Caching by adding `@EnableCaching` that activates Spring AOP based caching
```java
@EnableCaching
@SpringBootApplication
public class App {
}
```
- Add cache dependency in pom.xml.
- Use `@Cacheable`, this will
    - Call method
    - Spring Proxy intercepts
    - Check cache
    - If present → return cached value
    - Else → call method → store result
```java
@Service
public class UserService {

    @Cacheable(value = "users", key = "#id")
    public User getUser(Long id) {
        simulateSlowCall();
        return userRepository.findById(id).orElseThrow();
    }
}
```
##### Other core annotations
- `@CacheEvict` - Remove stale data.
```java
@CacheEvict(value = "users", key = "#id")
public void deleteUser(Long id)
```
- `@CachePut` - Updates cache everytime.
```java
@CachePut(value = "users", key = "#user.id")
public User updateUser(User user)
```
- `@Caching` - For multiple cache annotations
```java
@Caching(evict = {
    @CacheEvict(value = "users", key = "#id"),
    @CacheEvict(value = "usersList", allEntries = true)
})
```

##### Caching strategy
- Cache Aside (Default) - Used by `@Cacheable`
```
App → Cache → DB → Cache
```
- Evict on Write - Most common pattern
```
Update DB → Evict cache → reload later
```
```java
@Service
public class ProductService {

    @Cacheable(value = "products", key = "#id")
    public Product getProduct(Long id) {
        return repo.findById(id);
    }

    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }
}
```

### Cache recovery
- If the cache is in-process, such as a local memory cache (ConcurrentHashMap, Caffeine), it is usually lost when the application crashes because it lives in JVM memory.
- If the cache is external, like Redis, the application can often continue using it after restart because the cache exists outside the app.
- Whether the cache survives a cache-server restart depends on whether the cache system has persistence enabled. In practice, cache recovery is often handled either through external caches, persistence, or rebuilding and warming the cache after restart.
- Cache is usually a performance optimization. It is often acceptable to lose it.
###### Cache warming
- Preloading frequently used data into the cache before real user traffic starts hitting the system.
## Circuit Breaker
- A Circuit Breaker is a design pattern used in distributed systems to prevent cascading failures by stopping calls to a failing service temporarily and return a fallback response instead.
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
- It is generally implemented in Spring Boot using Resilience4j (Netflix Hystrix - deprecated)
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
- Commonly used in -
    - External APIs
    - Microservices
    - Payment gateways
    - Database (rare but possible)
    - Messaging systems

### Flow
- When `pay()` is called:
- Proxy intercepts method
- Circuit breaker checks state
- If CLOSED → execute
- If failures exceed threshold → switch OPEN
- If OPEN → immediately call fallback
- After wait time → HALF-OPEN
- Trial calls determine next state

## Retry Pattern
- Retry is a fault-tolerance pattern where a failed operation is automatically attempted again after a failure, usually with some delay and limit on attempts.
- Failures are often temporary not permanent, they can occur due to Network glitch, Service warming up, Temporary overload, DB connection timeout.
- Retry Pattern assumes that failure is transient and wait for some time until it works.
- Retry Pattern is commonly implemented in Spring Boot using Resilience4j.
- Use this pattern only when the operation is idempotent.
```java
@Service
public class OrderService {

    @Retry(name = "orderService", fallbackMethod = "fallback")
    public String placeOrder() {

        // Simulate random failure
        if (new Random().nextBoolean()) {
            throw new RuntimeException("Temporary failure");
        }

        return "Order placed successfully";
    }

    // Fallback method
    public String fallback(Exception ex) {
        return "Fallback: Could not place order";
    }
}
```
```
resilience4j:
  retry:
    instances:
      orderService:
        maxAttempts: 3
        waitDuration: 1s
```
### Flow
- Wraps your method call
- Intercepts exceptions
- Decides:
    - Should retry?
    - How long to wait?
    - When to stop?

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

## `@Async`
- `@Async` is an annotation used to execute a method asynchronously in a separate thread, so the caller does not wait for the result.
```
Caller thread
   ↓
Calls @Async method
   ↓
Returns immediately
   ↓
Async thread executes method
```
- We have to add `@EnableAsync` in the main class before using `@Async`.
```java
@EnableAsync
@SpringBootApplication
public class App {
}

@Async
public void sendEmail() {
    // runs in background
}
```


## Reactive Framework
- A reactive framework is a programming model and set of libraries that enable asynchronous, non-blocking, event-driven data processing with support for backpressure.
- Traditional Frameworks work on a **thread per request model**, that routinely gets blocked and has limited scalability and high memory usage.
```
Request → Thread → Wait → Response
```
```
Request → Event → Non-blocking → Callback when ready
```
## WebFlux
- Spring WebFlux is a non-blocking, reactive web framework in Spring designed to handle large numbers of concurrent requests efficiently.
- Traditional Frameworks work on a **thread per request model**, that routinely gets blocked and has limited scalability and high memory usage.
- On the other hand WebFlux creates event loops that are non blocking and asynchronous.
```java
Mono<User> user = repo.findById(id);
return user;
```
- WebFlux is suitable for systems with -
  - high concurrency (1000+ users)
  - I/O heavy apps
  - streaming APIs
  - real-time systems

## Mono
- `Mono` is a type from Project Reactor that represents an asynchronous computation that emits either 0 or 1 item (or an error).
```java
Mono<User> user = repo.findById(id);
```
- This Reactive solution returns the result immediately eventhough the result comes later.
```
Call method → return Mono immediately (no waiting)
             ↓
        actual DB call happens later
             ↓
        result is emitted asynchronously
```
- `Mono` is similar to `Future` and `CompletableFuture`.
- It is suitable for a single object.

## Flux
- Flux is a type from Project Reactor that represents an asynchronous stream of 0 to N elements (or an error).
```java
Flux<User> users = repo.findAll();
```
- It is suitable for a list/stream of objects.

## Internationalization in Spring Boot
- Internationalization in Spring Boot means serving messages based on locale.
- The two most important concepts are:
    - `MessageSource`
    - `LocaleResolver`
- `MessageSource` resolves the message text.
- `LocaleResolver` decides which locale to use.
- Messages are usually stored in:
    - `messages.properties`
    - `messages_fr.properties`
    - `messages_en.properties`
- In REST APIs, `Accept-Language` is usually the cleanest approach.
- In real projects, localize:
    - validation messages
    - user-facing errors
    - UI text
    - Keep business logic language-neutral.
 
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
