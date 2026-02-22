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
 
