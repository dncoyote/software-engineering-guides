# **System Design**
## Table of Contents

 1. [Scaling](#Scaling)
    1. [Horizontal Scaling](#Horizontal)
    2. [Vertical Scaling](#Vertical)
 2. [Client-Server Architecture](#client-server-architecture)
 3. [Load Balancer](#load-balancer)
 4. [Caching](#caching)
   

# **Scaling**
Scaling is a fundamental concept in distributed systems. It helps systems handle increasing workloads efficiently. The two primary scaling strategies are horizontal scaling (scaling out) and vertical scaling (scaling up).
### Scalability 
## Horizontal
 - Involves adding more machines (nodes/servers) to a system to distribute the load.
 - Used in distributed architectures to handle more requests, storage, and computation power.
 - This scaling procedure involves adding more application servers, database replicas, or caching nodes.
 - It works by distributing requests across multiple machines using load balancers.
 - Each server runs a copy of the application, handling a portion of the traffic.
 - Data is often sharded or partitioned across multiple nodes in a database.
 - Horizontal scaling is ideal for distributed, fault-tolerant, and highly available systems.
 - Horizontal scaling is preferable when - 
    - When handling millions of users or high traffic (e.g., web applications, microservices).
    - When needing high availability and fault tolerance.
    - When using distributed databases (NoSQL) or cloud-based architectures.

## Advantages and Disadvantages
### Advantages
 - High Availability & Fault Tolerance(Resilient) – If one server fails, others can continue processing requests.
- Infinite Scalability – Can theoretically scale infinitely by adding more nodes.
- Improved Performance – Load is distributed, reducing bottlenecks.
- Better Cost Efficiency – Commodity hardware can be used instead of expensive high-performance machines.

### Disadvantages
- Increased Complexity – Requires load balancing, distributed data handling, and synchronization.
- Data Consistency Challenges – Databases may require replication, sharding, and eventual consistency mechanisms.
- More Networking Overhead – Communication between nodes introduces latency.

## Vertical
 - Involves increasing the capacity of a single machine (more CPU, RAM, storage).
 - The application continues running on a single, more powerful machine rather than multiple distributed servers.
 - It works by  upgrading the hardware of an existing server (e.g., add more RAM, CPUs, SSDs) and improving the software performance  (e.g., better caching, optimizing database queries).
 - Older monolithic architectures typically scale vertically.
 - Small businesses and startups(Single server applications) often use vertical scaling before moving to horizontal scaling.
 - Vertical scaling is preferable when - 
    - When dealing with low-to-medium traffic applications.
    - When keeping infrastructure simple (e.g., single-database apps).
    - When latency-sensitive workloads require low network overhead.

## Advantages and Disadvantages
### Advantages
- Simplicity – No need for load balancing, data partitioning, or distributed coordination.
- Lower Latency – No network overhead since everything is processed within a single machine.
- Easier Consistency Management – No need for complex synchronization mechanisms.

### Disadvantages
- Scalability Limits – There’s a hardware limit to CPU, RAM, and storage upgrades.
- Single Point of Failure – If the machine crashes, the entire system goes down.
- Expensive – High-end servers are costly, and upgrading becomes more expensive over time.

## Horizontal v/s Vertical
| Feature      | Horizontal      | Vertical      |
| :---  | :----:  | :----:           |
| **Method** | 	Add more machines (nodes) | Upgrade a single machine |
| **Scalability** | Virtually unlimited | Limited by hardware |
| **Cost** | 	Cheaper (commodity hardware) | Expensive (high-end servers) |
| **Complexity** | High (load balancing, sharding) | Low (simple upgrades) |
| **Performance** | 	Distributed workload | Faster for single tasks |
| **Failure Tolerance** | High (multiple nodes) | Low (single point of failure) |
| **Use Cases** | 	Cloud apps, big data, web apps | Databases, legacy systems |

## Practices
- Auto-Scaling – Use AWS Auto Scaling, Kubernetes HPA (Horizontal Pod Autoscaler), or GCP Compute Autoscaler for automatic scaling.
- Load Balancing – Use NGINX, HAProxy, AWS ELB, or Cloudflare Load Balancer for distributing traffic across servers.
- Database Partitioning & Replication – Use sharding (horizontal scaling) and read replicas (vertical scaling) for performance optimization.
- Microservices Over Monoliths – Design microservices architectures that scale independently.
- Hybrid Scaling – Combine horizontal and vertical scaling (e.g., scale up database servers but scale out application servers).

# **Client-Server Architecture**
Client-Server Architecture is a fundamental design pattern in distributed systems where clients request services, and servers provide them. The client and server communicate over a network using standard protocols like HTTP, WebSockets, or gRPC.
## Client
- A client is a program or device that initiates requests to the server for data or services like Web browsers, mobile apps, desktop applications.
-  They request data from the server, display processed data to users and maintain a session with the server (e.g., authentication tokens)
## Server
- A server is a program or system that processes client requests and sends back responses like  Web servers (NGINX, Apache), database servers (MySQL, PostgreSQL), API servers.
- They process client requests, manage business logic and, store and retrieve data from databases.
- Server is designed in a way, different types of clients can connect to it.
- Server has all the information and acts as the central management system

## Types of Client-Server Architecture
### Two-Tier Architecture
- Direct communication between client and server(A desktop application directly querying a database server).
- Simple but has scalability limitations

### Three-Tier Architecture
- This is the most common Client-Server architecture and is more scalable and secure.
- It has a client(React, Vue), application server(Backend API: Spring Boot, Express.js, Django) and database(MySQL, MongoDB).

### Multi-Tier (N-Tier) Architecture
- Large scale applications have more than three layers to fulfill caching, authentication, analytics etc.

## Advantages and Disadvantages
### Advantages
- Centralized Control - Easier to manage updates and security.
- Scalability - Can handle many clients by adding more servers.
- Data Integrity - Centralized database ensures consistency.
- Security - Servers enforce access control and authentication.

### Disadvantages
- Single Point of Failure – If the server crashes, all clients are affected.
- Network Dependency – Requires a reliable network for communication.
- Server Overload – High traffic can slow down server response times.

# **Load Balancer**
- A Load Balancer is a system component (hardware or software) that sits between clients and backend servers to distribute incoming traffic across multiple servers to improve performance, maximize availability, prevent overloading and enable fault tolerance.
- With a load balancer, traffic is evenly spread, it detects and skips unhealthy servers(Use `/actuator/health` as a reliable endpoint) and we can scale horizontally with ease. 
- When a server goes down, the load balancer identifies the clients waiting to be served by the faulty servers and assign them to healthy servers, which ensures that all clients are served.
- Simply put, it ensures that all the server's load is balanced.
## Types of Load Balancers
### Layer 4 Load Balancing
### Layer 7 Load Balancing

## Load Balancing Algorithms
### Round Robin
### Least Connections
### IP Hash
### Weighted Round Robin
### Health Check Based

## How to design a scalable backend for an e-commerce app with high user traffic?
“I’d use a Layer 7 Load Balancer like AWS ALB or Nginx to distribute incoming HTTP requests across multiple Spring Boot instances. The load balancer would perform health checks on each instance, support SSL termination, and handle path-based routing (e.g., /products to one service, /checkout to another). Behind the services, we’d use a database cluster and Redis cache, also fronted by their own load balancers where needed.”

## Tools
- Nginx - L4/L7 - Reverse proxy, L7 rules, SSL, caching
- HAProxy - L4/L7
- AWS ELB - L4/L7
- Traefik - L7 - Cloud Native, automatic Service Discovery
- Envoy Proxy - L7 - Used in Service Meshes

# **Caching**
Caching is the technique of storing frequently accessed data in a fast-access layer (like memory or SSDs) to reduce
 - Latency: Faster responses to the client.
 - Load: Lower pressure on backend servers or databases.
 - Cost: Reduced usage of I/O and compute resources.
## Types
### In-Memory Caching
- Stored directly in application memory (e.g., HashMap, Guava, Caffeine).
- Ultra-fast (nanoseconds).
- Limited to the local instance.
- Best for: small, frequently used, read-heavy data (e.g., config values, small lookups).
- Not shared across instances.
### Distributed Caching
- Shared cache across multiple app instances (e.g., Redis, Memcached).
- Supports high availability and replication.
- Best for: API results, DB query results, user sessions, auth tokens.
- Higher latency than in-memory cache but scales horizontally.
### CDN Cache
- Static content like images, videos, HTML/CSS/JS.
- Cached at edge locations (e.g., Cloudflare, AWS CloudFront).
- Offloads traffic from origin servers
- Not suitable for dynamic content
### Database Caching
- Caches query results at the DB level or app level.
- Speeds up heavy queries
- Often used with Materialized Views or Query Caching (PostgreSQL, MySQL)
## Caching Strategies
### Write-Through
- Data is written to cache and DB at the same time.
- Cache is always fresh.
- Good for strong consistency.
### Write-Behind
- Data is first written to the cache and later persisted to the DB asynchronously.
- Good for high write throughput, but risk of data loss.
### Read-Through
- App reads from the cache.
- If missing, cache retrieves from DB and stores it.
- Abstracts the cache from the application.
### Cache Aside (Lazy Loading)
- App first checks the cache.
- If miss: fetch from DB → store in cache → return to client.
- Most common strategy. Gives more control to the app.
## Eviction Policies
| Policy      | Description      |
| ------------- | ------------- |
| LRU (Least Recently Used) | Evicts the entry that hasn't been accessed recently |
| LFU (Least Frequently Used) | Removes least accessed items |
| TTL (Time To Live) | Auto-expires items after a set time |
| Manual Invalidation | App explicitly clears cache |

# **Databases**
A database is an organized collection of data that can be efficiently accessed, managed, and updated.

- In system design, databases:

    - Store persistent data
    - Support ACID (or BASE) guarantees
    - Act as the backbone of nearly all backend systems

## Types of Databases
### Relational Database (SQL)
- Data is stored in tables with rows and columns
- Enforce schemas and relationships
- Support ACID transactions (Atomicity, Consistency, Isolation, Durability)
- SQL databases are commonly use when you need structured data
- Strong consistency is critical (e.g., financial apps)
- Complex queries with JOINS
### Non-Relational Database (NoSQL)
- Document Stores
    - Store data as JSON-like documents
    - Flexible schema
    - Great for hierarchical/nested data
    - Example: MongoDB, Couchbase

- Key-Value Stores
    - Very fast — retrieve data via a key
    - Simple and low-latency
    - Example: Redis, DynamoDB, Riak

- Wide Column Stores
    - Data stored in column families
    - Designed for high write throughput and horizontal scalability
    - Example: Apache Cassandra, HBase

- Graph Databases
    - Store data in nodes and edges
    - Ideal for highly connected data
    - Example: Neo4j, Amazon Neptune

## Real-World Use Cases
| Use Case      | Recommended DB      |
| ------------- | ------------- |
| E-commerce Catalog | MongoDB or PostgreSQL |
| User Sessions | Redis |
| Messaging App | Cassandra (high write throughput) |
| Banking App | PostgreSQL or Oracle |
| Social Network | Neo4j |
| Configuration Storage | etcd, Consul |
| Banking App | Elastic Search |

## Design Patterns
### Read replicas
- Read-heavy systems use read replicas to scale reads.
- Primary node handles writes; secondaries replicate asynchronously.
- Tools: PostgreSQL replication, MySQL master-slave, AWS RDS read replicas
### Sharding
- Horizontal partitioning of data across multiple nodes.
- Reduces write and storage bottlenecks.
- Used In: MongoDB, Cassandra, Elasticsearch
### Write-Ahead Logs
- Every change is written to a log before being applied.
- Ensures durability and crash recovery.
- Used In: PostgreSQL, Kafka, MySQL
### Caching Layer in Front of DB
- Reduces DB load for frequent queries.
- Use Redis or Memcached.
### CQRS (Command Query Responsibility Segregation)
- Separate read and write models for complex systems.
- Optimizes performance and scaling.

## Indexing in Databases
- An index is a data structure (usually a B-tree or hash) that allows the database to quickly locate rows in a table without scanning every row.
