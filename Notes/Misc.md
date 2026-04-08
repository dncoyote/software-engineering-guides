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

## Authentication vs Authorization
- Authentication - Verifies the identity of a user.
- Authorization - Determines permissions/access after identity is known.
```
//AuthN
POST /login
{
  "email": "bilal@example.com",
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
## Cookies vs Sessions vs JWT
## API security best practices
## Status codes
## REST API design
## API Gateway & Load Balancer & Reverse Proxy
## Caching strategies
## Rate Limiting
