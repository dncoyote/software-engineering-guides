
## REST APIs
- REST (Representational State Transfer) is an architectural style for designing networked applications, typically over HTTP.

```
/users        (POST → create)
/users/123    (GET → read)
/users/123    (DELETE → delete)
```

### GET
- Retrieve data from server
- Idempotent
- Use case
    - Fetch user profile
    - Get list of orders
    - Search/filter data

```
GET /users/123

{
  "id": 123,
  "name": "Bilal"
}
```
### POST 
- Create a new resource
- Not Idempotent
- Use case 
    - Create user
    - Submit form
    - Trigger non-idempotent action (payment)
- There is size limit for POST data but it not defined by HTTP spec and it depends on infrastructure. `413 Payload Too Large`

```
POST /users
Content-Type: application/json

{
  "name": "John"
}
```
### PUT
- Replace entire resource (Full update / Replace)
- Idempotent
- Missing fields will be overwritten

```
PUT /users/123
Content-Type: application/json

{
  "name": "John",
  "email": "john@example.com"
}
```
### PATCH
- Update only specific fields (Partial update)
- Not strictly idempotent

```
PATCH /users/123
Content-Type: application/json

{
  "email": "new@email.com"
}
```
### DELETE 
- Delete a resource
- Idempotent

```
DELETE /users/123
```

## PUT vs PATCH

|  Aspect | PUT | PATCH |
| --- | --- | --- |
| type| Full replace | partial replace |
| idempotent| always | not guaranteed |
| payload | Full object | partial fields |
| risk | overwrites missing field | safer |
| use case | replace resource | modify some fields |

## Idempotency
- An operation is idempotent if repeating it multiple times produces the same result as applying it once.

## API best practises
- Design around resources, not actions - Use nouns in URLs and let the HTTP method express the actions

```
GET /users/123
POST /users
PATCH /users/123
DELETE /users/123
```
- Use the correct HTTP method - HTTP method semantics are standardized, and clients and infrastructure rely on them
- Return the right status codes

```
200 OK for successful reads/updates
201 Created when a resource is created
204 No Content for delete or update without response body
400 Bad Request for invalid input
401 Unauthorized when authentication is missing or invalid
403 Forbidden when authenticated but not allowed
404 Not Found when resource does not exist
409 Conflict for state conflicts
429 Too Many Requests for rate limiting; Retry-After may be included
```
- Keep request and response shapes consistent

```json
//request
{
  "data": {
    "id": 123,
    "name": "Bilal"
  },
  "meta": {
    "requestId": "a1b2c3"
  }
}
```

```json
{
  "error": {
    "code": "VALIDATION_FAILED",
    "message": "Email is invalid",
    "details": [
      {
        "field": "email",
        "issue": "must be a valid email address"
      }
    ]
  }
}
```
- Validate input strictly at the boundary

```java
public record CreateUserRequest(
    @NotBlank String name,
    @Email String email,
    @Size(min = 8, max = 100) String password
) {}
```

- Use pagination, filtering, and sorting - Never return unbounded lists in production APIs

```
GET /users?page=0&size=20&sort=createdAt,desc
GET /orders?status=PAID&customerId=123
```
- Version API

```
/api/v1/users
/api/v2/users
```
- Make idempotency explicit for retry-prone operations using idempotency key
- Document the API well using OpenAPI/Swagger
    - endpoint purpose
    - request schema
    - response schema
    - auth requirements
    - error codes
    - examples
    - rate limits
- Keep business logic out of controllers

## API Security Best practices
- Use HTTPS everywhere
- Strong authentication using OAuth2/OIDC, JWT or session auth should be implemented properly
- Apply least privilege
- Rate limit and protect resources
    - login
    - OTP endpoints
    - password reset
    - search endpoints
    - expensive reports
    - public APIs
- Prevent excessive data exposure using DTO's
- Validate and sanitize all inputs, always use parameterized SQL.
- Log everything

## API Versioning
- API versioning is the practice of managing changes to an API in a way that allows existing clients to continue working while newer clients adopt updated behavior or contracts.
- You usually need a new version for breaking changes
- URI / path versioning

```
/api/v1/users
/api/v2/users
```
- Query parameter versioning

```
GET /users/123?version=2
```
- Header versioning

## HTTP
- HTTP (HyperText Transfer Protocol) is a stateless, application-layer protocol used for communication between clients (e.g., browser, mobile app) and servers over a network.
- It follows a request → response model
- Built on top of TCP/IP
- Designed to transfer resources (HTML, JSON, images, etc.)

```
<HTTP METHOD> <URL> <VERSION>
Headers
Body (optional)
```
```
POST /users HTTP/1.1
Host: example.com
Content-Type: application/json
Authorization: Bearer token123

{
"name": "John",
"email": "john@example.com"
}
```
### Statelessness
- HTTP is stateless.
- Each request is independent — server does NOT remember previous requests.
- State is handled Cookies, Sessions, JWT tokens.
### Components
#### Methods
| Method | Meaning | Example |
| ------ | -------------- | ---------------- |
| GET | Fetch data | Get user |
| POST | Create | Create user |
| PUT | Replace | Update full user |
| PATCH | Partial update | Update email |
| DELETE | Remove | Delete user |
#### URL
```
https://api.example.com/users/123
```
- `https` → protocol
- `api.example.com` → host
- `/users/123` → resource

#### Headers
- Metadata about request
```
Content-Type: application/json
Authorization: Bearer token
Accept: application/json
```

#### Body
- Optional, used for `POST`, `PUT`, `PATCH`.
- Usually JSON in modern APIs.

## HTTP vs HTTPS
- HTTP (HyperText Transfer Protocol)
- A plain-text protocol for client-server communication
- Data is sent without encryption
```
Client → Request → Server → Response → Client
```
- HTTPS
- HTTP + TLS (Transport Layer Security)
- HTTPS is not a different protocol — it’s HTTP wrapped inside a secure tunnel (TLS)
- Data is encrypted in transit
```
Client → TLS Handshake → Secure Connection → HTTP over TLS
```
- TLS Handshake
- Client connects to server
- Server sends SSL certificate
- Client verifies certificate
- Both agree on Encryption algorithm and Session key
- Secure Communication
- All data encrypted using symmetric encryption
- Only client & server can decrypt

### TLS (Transport Layer Security)
- TLS is a cryptographic protocol that provides
- Encryption (privacy)
- Integrity (no tampering)
- Authentication (server identity)
- It is the protocol that powers HTTPS
- TLS 1.2 and TLS 1.3 are the Modern, faster and widely used protocols.
- TLS handled by Nginx, AWS ALB, Cloudflare

## GraphQL
- GraphQL is a query language + runtime for APIs where the client specifies exactly what data it needs, and the server returns precisely that—no more, no less.
- REST can over-fetch or under fetch, GraphQL lets the client define exactly what it wants.
## Authentication vs Authorization
- Authentication - Verifies the identity of a user.
- Authorization - Determines permissions/access after identity is known.
```
//AuthN
POST /login
{
  "email": "john@example.com",
  "password": "123456"
}
```
- The Server validates credentials and returns token (JWT).
- Happens during Login
```
//AuthZ
GET /admin/dashboard
Authorization: Bearer <token>
```
- The Server verifies token (Authentication) and checks role/permission (Authorization)
- Happens when accessing API's.
- In microservices:
  - Authentication handled by:
    - API Gateway
    - Auth service
  - Authorization handled by:
    - Each service
    - Or centralized policy engine
- Common AuthN methods
  - Username + Password
  - OTP / MFA
  - OAuth / SSO
  - JWT-based Authentication
- Common AuthZ models
  - Role-Based Access Control (RBAC)
  - Permission-Based (Fine-grained)
  - Attribute-Based Access Control (ABAC) - Based on User Attributes - User can access resource only if they own it.

## JWT
- JWT (JSON Web Token) is a compact, self-contained token used for authentication and information exchange between client and server.
- It is stateless, Digitally signed and used in Authorization headers.
- Traditional systems stores user session in memory/DB, this is not scalable for distributed systems.
- JWT solves this by storing user identity inside the token, hence server doesn’t need to remember anything.
- Use JWT for Microservices architecture, Mobile apps, APIs consumed by multiple clients
#### Structure
- JWT has 3 parts
  - HEADER
  - PAYLOAD
  - SIGNATURE
### Working
- During AuthN - Login `POST /login`, Server validates credentials, Generates JWT, Returns token.
- This Token is stored by the client in LocalStorage / Memory / Cookie.
- During API Request, this token is passed along with request as Bearer Token.
- Server validates the token, verifies the signature and checks expiry.

## Session
- Session-based authentication is a mechanism where the server stores user state, and the client holds only a session identifier (session ID).
- This is a stateful mechanism as server stores the session data and is harder to scale.
- Use sessions for Monolithic apps, Server-rendered apps, Admin dashboards.
#### Working
- During Login `POST /login`, Server verifies credentials and creates session.
- This created Session ID is stored in the server (Memory / Redis / DB) and then send to the Client where it can be stored in cookie.
- During subsequent requests, server looks up session and retrieves user info from the cookie.

## Distributed Transaction
- Distributed Transaction is a transaction that spans multiple systems or services, ensuring all operations either commit together or rollback together.
- Distributed Transaction is harder to manage in microservices as there are Multiple services, Multiple databases and Network calls and we can be hit with Network failures or Partial success scenarios.
- It can be solved using
  - 2-Phase Commit (2PC) - Traditional approach
  - Saga Pattern - A Saga is a sequence of local transactions where each step has a compensating action if something fails.
- Distributed Transaction ensures eventual consistency over strict consistency.

## Difference between PUT vs PATCH
## Idempotency?
## Authentication (JWT, OAuth)
## Authentication works over HTTP
## SQL Injection
## SSRF
## OIDC
## Cookies vs Sessions vs JWT
## API security best practices
## API Versioning 
## Status codes
## REST API design
## API Gateway & Load Balancer & Reverse Proxy
## Caching strategies
## Rate Limiting
