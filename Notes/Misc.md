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

## Difference between PUT vs PATCH
## Idempotency?
## Authentication (JWT, OAuth)
## Authentication works over HTTP
## Cookies vs Sessions vs JWT
## Authentication & Authorization
## API security best practices
## Status codes
## REST API design
## API Gateway & Load Balancer & Reverse Proxy
## Caching strategies
## Rate Limiting
## Concurrency and Parallelism
