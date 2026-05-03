# Database
## SQL
- SQL (Structured Query Language) is a declarative language used to interact with relational databases to store, retrieve, manipulate, and manage data. 

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,        -- unique identifier
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE,
    age INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id),  -- foreign key
    amount DECIMAL(10,2),
    status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


INSERT INTO users (name, email, age)
VALUES ('John', 'john@email.com', 32);


SELECT name, age
FROM users
WHERE age > 25;


UPDATE users
SET age = 33
WHERE id = 1;


DELETE FROM users
WHERE id = 1;


SELECT u.name, o.amount
FROM users u
JOIN orders o ON u.id = o.user_id;
```

## Relational Database
- A Relational Database is a type of database that stores data in tables (relations) with rows and columns, and defines relationships between tables using keys and constraints.
- Data is stored in structured tables and connected using keys, ensuring consistency and integrity.
- Relational DBs are best for:
    - Banking systems
    - Payment systems
    - Inventory management
    - Order processing
    - Enterprise applications
#### Characteristics
- Table (Relation)
    - A structured collection of data.
- Row (Tuple)
    - A single record.
- Column (Attribute)
    - A property of the entity.
####  Advantages
- Strong consistency (ACID)
- Structured data
- Powerful querying (JOINs)
- Data integrity via constraints
- Mature ecosystem
#### Disadvantages
- Hard to scale horizontally
- Schema changes costly
- Joins can be expensive
- Not ideal for unstructured data


## NoSQL
- NoSQL refers to a class of databases designed to handle large-scale, distributed, flexible data models that do not strictly follow the relational (table-based) model of SQL databases.
- NoSQL databases are designed for scalability and flexibility, using non-relational data models like documents or key-value pairs. 
- They are optimized for high throughput and distributed systems, often trading strict consistency and normalization for performance and scalability.
- Examples
- NoSQL databases embeds related data together instead of splitting data across multiple tables and joining them.
- Core idea is to optimize for scale, flexibility and performance over strict structure.
- Common NoSQL Databases and uses 
    - MongoDB -  Product catalogs, CMS, User profiles, Analytics ingestion
    - Cassandra - Time-series data, Logging, IoT data
    - Redis - Caching, Session storage, Rate limiting
    - DynamoDB
### Types
- Document Databases - Data stored as JSON like documents.
    - Allows Nested data with flexible schema.
    - No joins required.
    - Example - MongoDB
- Key-Value stores
    - Extremely fast and simple look-up.
    - Used for caching.
    - Example - Redis
- Wide Column stores
    - Massive scale, high write throughput
    - Distributed by design
    - Example - Cassandra
- Graph Database
    - Relationship heavy queries.
    - Social networks, recommendations.
    - Example Neo4j

## When to use NoSQL 
- Use NoSQL when you need flexible schema, high scalability, high write throughput, or data models that don’t fit relational structures, and you can relax strict ACID guarantees or avoid complex joins.
###### Flexible/Evolving schema
- Rapid product iteration
- Startups

```json
//today
{
  "name": "John",
  "skills": ["Java", "Spring"],
  "social": {
    "twitter": "@john"
  }
}

//tomorrow
{
  "name": "John",
  "skills": ["Java"],
  "certifications": ["AWS"]
}
```
###### High Write Throughput (Write-Heavy Systems)
- Applicable for systems like logs, metrics, IoT events.
- Cassandra

###### Read Patterns Centered Around Single Entity
- There is no joins - like `user`
- Unlike `users → orders → order_items (JOINs)`
###### Horizontal Scaling Required
- Applicable when applications is designed for millions of users, global systems with high traffic.
- NoSQL is built for sharding.
###### Low Latency requirements
- When you want to fetch data in low latency.
- Caching and session storage
###### Semi-Structured/Unstructured Data 
- JSON Docs, Logs, Content systems.
###### Eventual Consistency is acceptable
- If a slight delay is acceptable.
- Social media

## When Not to use NoSQL 
- Use SQL when you need strict structure and consistency and involves scale and flexibility.
###### Strong Transactions required.
###### Complex Joins / Relationships
###### Strict Data Integrity Needed
###### Schema Is Stable and Well-Understood
###### Consistent Reads are mandated 

## ACID
- ACID is a set of four guarantees that ensure reliable and correct transaction processing in relational databases.
    - A → Atomicity
    - C → Consistency
    - I → Isolation
    - D → Durability
- Supported by systems like:
    - PostgreSQL
    - MySQL
### Atomicity
- A transaction is fully completed or fully rolled back.
### Consistency
- A transaction must bring the database from one valid state to another valid state.
### Isolation
- Transactions execute as if they are independent of each other.
### Durability
- Once a transaction is committed, it is permanently stored, even if system crashes.

## WHERE vs HAVING
- `WHERE`
    - Filter rows before grouping
- `HAVING`
    - Filter groups after grouping

```sql
SELECT user_id, SUM(amount) AS total_amount
FROM orders
GROUP BY user_id
HAVING SUM(amount) > 250;
```

## Primary key v/s Unique key v/s Foreign key v/s Composite key 
#### Primary key
- A primary key is a column or a set of columns in a database table that uniquely identifies each row (record) in the table.
- It ensures that each row in the table is distinct, and there are no duplicate entries for the primary key values.
- Enforces the uniqueness and non-null property of the primary key columns.
  Eg: id in StudentTable

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(255)
);
```
#### Unique key
- A unique key, often referred to as a unique constraint, is a set of one or more columns in a database table that must contain unique values within the table.
- Unique key does not necessarily need to be used to identify individual rows. It's primarily used to ensure that certain columns or combinations of columns have distinct values.
  eg: email in StudentTable

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE
);
```
#### Foreign key
- A foreign key is a column or a set of columns in a table that establishes a link between data in two tables.
- Foreign keys are used to create relationships between tables, allowing you to retrieve related data from different tables through JOIN operations.
- Foreign key in a child table typically references the primary key in the parent table, it helps maintain data consistency and prevent orphaned data.
  Eg: studentId in DepartmentTable

```sql
CREATE TABLE departments (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    department_id INT REFERENCES departments(id)
);
```
#### Composite key
 - Composite key is a combination of two or more columns that uniquely identifies each row in a database table. 
 - They are unlike a single-column primary key, which consists of only one column, a composite key involves multiple columns.
 Eg: studentId and DepartmentId in SchoolTable

```sql
CREATE TABLE enrollments (
    student_id INT,
    course_id INT,
    PRIMARY KEY (student_id, course_id)
);
```
## Indexing 
- Indexing is a database optimization technique that creates a data structure (usually a B-Tree) to make data retrieval faster.
- Instead of scanning the whole table (Full Table Scan), the database uses an index to locate rows efficiently.
- Imagine a table with 10 million records and we have this query - `SELECT * FROM users WHERE email = 'john@email.com';`
- Without index, Database scans all 10 million rows and the Time complexity ≈ O(n)
- With index on email, Database uses a sorted tree structure and the Time complexity ≈ O(log n)
- Default Index type is a Balanced Tree.
- Primary Key Automatically created Index

```sql
CREATE INDEX idx_users_email ON users(email);


```
- Always index Primary keys, Foreign keys, Frequently searched columns
- How Index look-up works for `SELECT * FROM users WHERE email = 'f@email';`
    - Go to root node - `a@email.com`
    - Compare key
        - if greater move right, else move left
    - Move down correct branch
    - Reach leaf node
    - Get row pointer
    - Fetch row
- Advantages
    - Faster `SELECT`, `WHERE`, `JOIN`, `ORDER BY`
- Disadvantage
    - Slower `UPDATE`, `INSERT` - as these operations also involves updating the existing index.
#### Composite Index
- A Composite Index (also called multi-column index) is an index created on multiple columns together.

```sql
CREATE INDEX idx_user_status_date
ON orders(user_id, status, created_at);
```
- In a composite index, index is sorted like - `user_id → then status → then created_at`
- So indexing only works for 

```sql
WHERE user_id = 1
WHERE user_id = 1 AND status = 'PAID'
WHERE user_id = 1 AND status = 'PAID' AND created_at > '2026-01-01'
```
- It does not work for
```sql
WHERE status = 'PAID'
WHERE created_at = '2026-01-01'
```
#### Unique Index
- A Unique Index ensures all values in a column (or combination of columns) are unique.

```sql
CREATE UNIQUE INDEX idx_email ON users(email);
```
#### Covering Index
- A Covering Index is an index that contains all the columns required to satisfy a query, so the database does NOT need to access the table.
- The results are returned directly from the index tree, this avoids costly table data lookups.

## Database Optimization Techniques 
### Query-Level Optimization
- Avoid `SELECT *` - Fetch only needed columns → less I/O, better index usage.
- Use proper joins.
- Replace subqueries when needed.
- Use `EXPLAIN`
`EXPLAIN SELECT * FROM orders WHERE user_id = 1;`
- Index the tables - use Basic Index, Composite Index, Partial Index and Covering Index. 
- Avoid N+1 Problems using JOIN / batch fetch or DTO Projections.
- Use Pagination - but avoid deep offsets.
### Schema Design
- Normalization
- Choose correct data types.
- Use Constraints to act as guardrails.
### Caching
- Cache frequently fetched data.
- Use Application Cache (Redis) and DB Cache.
- Use adequate Cache Strategy.
### Read/Write Separation
- Use Primary for writes and use Replicas for reads
### Database
- Partition the same DB.
- Shard the DB to multiple DB's.


## Partitioning
- Partitioning is process of splitting a large table into smaller physical pieces (partitions) while keeping it logically as one table.
- This is unlike sharding, here the pieces stays within the same database instance.
- Consider table with 100M rows, without partitioning this is a huge table that can result in slow retrieves. With partitioning the table becomes transformed to the following structure and the query will touch only the relevant partitions.

```
orders
 ├── orders_2024
 ├── orders_2025
 ├── orders_2026
```
### Types
- Range Partitioning

```sql
CREATE TABLE orders_2024
PARTITION OF orders
FOR VALUES FROM ('2024-01-01') TO ('2025-01-01');

CREATE TABLE orders_2025
PARTITION OF orders
FOR VALUES FROM ('2025-01-01') TO ('2026-01-01');
```
- List Partitioning

```sql
CREATE TABLE users_india
PARTITION OF users FOR VALUES IN ('India');

CREATE TABLE users_usa
PARTITION OF users FOR VALUES IN ('USA');
```
- Hash Partitioning
## Sharding
- Sharding is a technique of splitting a large dataset across multiple database nodes (shards) so that each node stores only a subset of the data.
- It enables horizontal scaling, allowing applications to handle increased data volume and high traffic
### Types
- Range based Sharding
- Hash based Sharding
- Directory based Sharding

## DDL (Data Definition Language)
- DDL is used to define or modify the database structure (schema).
- DDL is often auto-committed and it is harder to rollback (PostgreSQL supports transactional DDL)
- DDL locks entire table/schema
- CREATE
 
```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(255) UNIQUE
);
```
- ALTER - modifies structure.

```sql
ALTER TABLE users
ADD COLUMN age INT;
```
- DROP - Deletes entire table.

```sql
DROP TABLE users;
```
- TRUNCATE - Removes all data (fast, resets storage).

```sql
TRUNCATE TABLE users;
```
- RENAME 

```sql
ALTER TABLE users RENAME TO customers;
```

## DML (Data Manipulation Language) 
- DML is used to read and modify the actual data stored in tables
- Fully transactional.
- DML performs row level locks.
- INSERT 

```sql
INSERT INTO users (name, email)
VALUES ('John', 'john@email.com');
```
- SELECT

```sql
SELECT * FROM users WHERE age > 25;
```
- UPDATE

```sql
UPDATE users
SET age = 32
WHERE id = 1;
```
- DELETE

```sql
DELETE FROM users WHERE id = 1;
```

## DQL (Data Query Language) 
- DQL (Data Query Language) is the subset of SQL used to retrieve (query) data from the database.
- SELECT

```sql
SELECT id, name FROM users;
```
- WHERE - Filter rows before grouping.

```sql
SELECT * FROM users
WHERE age > 25;
```
- ORDER BY - sorting

```sql
SELECT name, age
FROM users
ORDER BY age DESC;
```

- LIMIT/OFFSET - pagination

```sql
SELECT *
FROM users
ORDER BY id
LIMIT 10 OFFSET 20;  -- page 3 (10 per page)
```
- DISTINCT

```sql
SELECT DISTINCT city FROM users;
```
- GROUP BY

```sql
SELECT department, COUNT(*) AS cnt
FROM employees
GROUP BY department
HAVING COUNT(*) > 5;
```
- JOINS
- SUBQUERIES

```sql
SELECT name
FROM users
WHERE id IN (
  SELECT user_id FROM orders WHERE amount > 100
);
```

## DCL (Data Control Language)
- DCL (Data Control Language) is the subset of SQL used to control access and permissions on database objects.
- Suppose we have a database `employees` and we have two sets of users.
    - `app_user` → application
    - `report_user` → analytics
    - `admin_user`
- GRANT 
```sql
GRANT SELECT ON employees TO report_user;
//`report_user` can only read data.

```

```sql
GRANT SELECT, INSERT, UPDATE ON employees TO app_user;
```

```sql
GRANT ALL PRIVILEGES ON employees TO admin_user;

```
- REVOKE

```sql
REVOKE SELECT ON employees FROM report_user;
```

```sql
REVOKE ALL PRIVILEGES ON employees FROM app_user;
```
- We can also create Role-Based Access Control (RBAC)

```sql
CREATE ROLE analyst;
```

```sql
GRANT SELECT ON employees TO analyst;
```

```sql
GRANT analyst TO report_user;
```

## TCL (Transaction Control Language) 
- TCL (Transaction Control Language) is used to manage transactions in a database.
- Without TCL:
    - Partial updates can corrupt data
    - Failures leave DB in inconsistent state
- TCL ensures Atomicity (all-or-nothing)
- COMMIT - Permanently saves all changes in the transaction.

```sql
BEGIN;

UPDATE accounts SET balance = balance - 100 WHERE id = 1;
UPDATE accounts SET balance = balance + 100 WHERE id = 2;

COMMIT;
```
- ROLLBACK - Reverts all changes since the last COMMIT (or transaction start).

```sql
BEGIN;

UPDATE accounts SET balance = balance - 100 WHERE id = 1;

ROLLBACK;
```
- SAVEPOINT - Creates a checkpoint inside a transaction.

```sql
BEGIN;

UPDATE accounts SET balance = balance - 100 WHERE id = 1;

SAVEPOINT before_second_update;

UPDATE accounts SET balance = balance + 100 WHERE id = 2;

ROLLBACK TO before_second_update;
// Only the second update is undone

```

```sql
BEGIN;

UPDATE users SET balance = balance - 500 WHERE id = 1;

SAVEPOINT step1;

UPDATE users SET balance = balance + 500 WHERE id = 2;

-- error occurs
ROLLBACK TO step1;

COMMIT;
```

## Constraints 
- Constraints are rules enforced by the database to ensure data integrity, validity, and consistency.
- They act as guardrails: invalid data never enters the system.
- NOT NULL - Column cannot contain NULL values

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);
```

```sql
//INVALID
INSERT INTO users (name) VALUES (NULL);
```

- UNIQUE - All values must be distinct

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE
);
```

```sql
//INVALID
INSERT INTO users (email) VALUES ('a@email.com');
INSERT INTO users (email) VALUES ('a@email.com'); -- fails
```
- PRIMARY KEY
    - Uniquely identifies row
    - NOT NULL + UNIQUE
    - Only one per table
    - We can keep two or more fields as PRIMARY KEY `PRIMARY KEY (student_id, course_id)`- Same student cannot enroll twice in same course (This is a Composite Primary Key).
```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY
);
```
- FOREIGN KEY - Ensures referential integrity between tables.

```sql
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id)
);
```

```sql
//INVALID
INSERT INTO orders (user_id) VALUES (999); -- if user doesn't exist
```
- CHECK - Validates condition

```sql
CREATE TABLE users (
    age INT CHECK (age >= 18)
);
```

```sql
//INVALID
INSERT INTO users (age) VALUES (15);
```
- DEFAULT - Sets default value if none provided.

```sql
CREATE TABLE users (
    status VARCHAR(20) DEFAULT 'ACTIVE'
);
```

```sql
INSERT INTO users DEFAULT VALUES;
```

- COMPOSITE CONSTRAINT - A rule applied on multiple columns together, not individually.
    - Suppose if we want a user to order one product only once.

```sql
CREATE TABLE orders (
    user_id INT,
    product_id INT,
    UNIQUE (user_id, product_id)
);
```


## Find average of salary in each department

```sql
SELECT department, AVG(salary) AS avg_salary
FROM employees
GROUP BY department;


SELECT department, AVG(salary) AS avg_salary
FROM employees
GROUP BY department
HAVING AVG(salary) > 80000;


SELECT d.name, AVG(e.salary) AS avg_salary
FROM employees e
JOIN departments d ON e.department_id = d.id
GROUP BY d.name;
```

## JOIN Queries
- Consider we have two tables `Customers` and `Orders`.

| customer_id| name|
| --- | --- |
| 1| Alice|
| 2| Bob|
| 3| Charlie|



| order_id| customer_id|
| --- | --- |
| 101| 1|
| 102| 1|
| 103| 2|
| 104| 4|
#### INNER JOIN
- Returns records that have matching values in both tables.
- We can use `INNER JOIN` or `JOIN`.

```sql
SELECT c.name, o.order_id
FROM Customers c
INNER JOIN Orders o
ON c.customer_id = o.customer_id;
```

| name| order_id|
| --- | --- |
| Alice| 101|
| Alice| 102|
| Bob| 103|
#### LEFT JOIN (or LEFT OUTER JOIN)
- Returns all records from the left table (table1) and the matched records from the right table (table2). The result is NULL from the right side if there is no match.

```sql
SELECT c.name, o.order_id
FROM Customers c
LEFT JOIN Orders o
ON c.customer_id = o.customer_id;
```


| name| order_id|
| --- | --- |
| Alice| 101|
| Alice| 102|
| Bob| 103|
| Charlie| NULL|
#### RIGHT JOIN (or RIGHT OUTER JOIN)
- Returns all records from the right table (table2) and the matched records from the left table (table1). The result is NULL from the left side if there is no match.

```sql
SELECT c.name, o.order_id
FROM Customers c
RIGHT JOIN Orders o
ON c.customer_id = o.customer_id;
```


| name| order_id|
| --- | --- |
| Alice| 101|
| Alice| 102|
| Bob| 103|
| NULL| 104|
#### FULL JOIN (or FULL OUTER JOIN)
- Returns all records when there is a match in either left (table1) or right (table2) table.

```sql
SELECT c.name, o.order_id
FROM Customers c
FULL JOIN Orders o
ON c.customer_id = o.customer_id;
```


| name| order_id|
| --- | --- |
| Alice| 101|
| Alice| 102|
| Bob| 103|
| Charlie| NULL|
| NULL| 104|
#### CROSS JOIN
- Returns all combinations (Cartesian product)
- It does NOT care about matching 
- It ignores relationships completely
- It just produces combinations
```sql
SELECT c.name, o.order_id
FROM Customers c
CROSS JOIN Orders o;
```


| name| order_id|
| --- | --- |
| Alice| 101|
| Alice| 102|
| Alice| 103|
| Alice| 104|
| Bob| 101|
| Bob| 102|
| Bob| 103|
| Bob| 104|
| Charlie| 101|
| Charlie| 102|
| Charlie| 103|
| Charlie| 104|
#### SELF JOIN
- A table joined with itself.
- Consider this table `Employees`

| id| name| manager_id|
| --- | --- | --- |
| 1| Alice| NULL|
| 2| Bob| 1|
| 3| Charlie| 1|

```sql
SELECT e.name AS employee, m.name AS manager
FROM employees e
LEFT JOIN employees m
ON e.manager_id = m.id;
```


| employee| manager|
| --- | --- |
| Alice| NULL|
| Bob| Alice|
| Charlie| Alice|

## UNION, UNION ALL 
- Union - Combines results and removes duplicate rows
- Union All - Combines results and keeps all rows (including duplicates)
- Consider this two tables


| name |
| --- |
| Alice |
| Bob|
| Charlie |



| name |
| --- |
| Bob|
| David |
| Alice |


```sql
SELECT name FROM A
UNION
SELECT name FROM B;
```


| name |
| --- |
| Alice |
| Bob|
| Charlie |
| David |


```sql
SELECT name FROM A
UNION ALL
SELECT name FROM B;
```


| name |
| --- |
| Alice |
| Bob|
| Charlie |
| Bob |
| David |
| Alice |


## ORDER BY 
- `ORDER BY` is used to sort the result set of a query based on one or more columns.

```sql
SELECT name, age
FROM users
ORDER BY age;

SELECT name, age
FROM users
ORDER BY age DESC;
```
- Multiple column sorting
    - Sort by `department`, if same department → sort by `salary DESC`

```sql
SELECT name, department, salary
FROM employees
ORDER BY department ASC, salary DESC;
```
- Control NULL order

```sql
SELECT *
FROM users
ORDER BY age NULLS FIRST;
```

## GROUP BY
- `GROUP BY` clause is used to group rows that have the same values in specified columns into summary rows.

```sql
SELECT department, AVG(salary) AS avg_salary
FROM employees
GROUP BY department;

SELECT department, AVG(salary)
FROM employees
WHERE salary > 60000
GROUP BY department;
```
- Multiple columns - Each unique combination becomes a group

```sql
SELECT department, name, COUNT(*)
FROM employees
GROUP BY department, name;
```
- Data Aggregation

```sql
SELECT product_category, SUM(sales_amount) as total_sales
FROM sales
GROUP BY product_category;
```

- Data Summarization

```sql
SELECT DATE(order_date), SUM(order_total) as daily_total
FROM orders
GROUP BY DATE(order_date);
```

- Data Cleansing

```sql
SELECT email, COUNT(*) as count
FROM customers
GROUP BY email
HAVING count > 1;
```

## Window Functions
- Window functions perform calculations across a set of rows (a “window”) related to the current row, without collapsing rows.

## SQL query to fetch second highest value

```sql
SELECT MAX(column_name) AS second_highest
FROM table_name
WHERE column_name < (SELECT MAX(column_name) FROM table_name);
```
```sql
SELECT column_name AS second_highest
FROM table_name
ORDER BY column_name DESC
LIMIT 1 OFFSET 1;
```


## SQL query to fetch third highest value

```sql
SELECT DISTINCT salary
FROM employees
ORDER BY salary DESC
LIMIT 1 OFFSET 2;
```

```sql
SELECT salary
FROM (
    SELECT salary,
           DENSE_RANK() OVER (ORDER BY salary DESC) AS rnk
    FROM employees
) t
WHERE rnk = 3;
```

## View
- View is a virtual table derived from one or more tables or other views. 
- It represents a set of rows and columns, just like a real table, but its contents are dynamically generated based on the definition of the view. 
- Views do not store data themselves; instead, they retrieve data from the underlying tables or views whenever they are queried.
- Views are a powerful tool for organizing and presenting data in SQL databases, offering flexibility, security, and abstraction capabilities.

```sql
CREATE VIEW EmployeeView AS
SELECT EmployeeID, FirstName, LastName, Department
FROM Employees
WHERE Department = 'IT';

SELECT * FROM EmployeeView;
```
## Procedure
- Procedure is a group of SQL statements that perform a specific task or set of tasks. 
- Procedures are stored in the database and can be executed by invoking their name. 
- They are often used to encapsulate frequently executed sequences of SQL statements, implement business logic, or perform data manipulation tasks.

```sql
CREATE PROCEDURE GetEmployeeInfo(IN emp_id INT)
BEGIN
    SELECT * FROM employees WHERE employee_id = emp_id;
END;
```

## Triggers
- Triggers in SQL are special types of stored procedures that automatically execute in response to specific events or actions performed on a database table. 
- These events can include INSERT, UPDATE, or DELETE operations on the table. 
- Triggers are useful for enforcing data integrity constraints, auditing changes, or automating certain tasks based on database events.

```sql
CREATE TRIGGER update_timestamp
AFTER UPDATE ON Employees
FOR EACH ROW
BEGIN
    SET NEW.last_updated = CURRENT_TIMESTAMP;
END;
```

## Functions
- Functions are named, reusable code blocks that perform a specific task or computation and return a single value. 
- Functions can accept parameters, perform calculations or manipulations on data, and return a result. 
- They are commonly used to encapsulate logic that needs to be executed repeatedly within SQL queries or statements.
```sql
SELECT SQUARE(5);
```

## Partition
- A Partition is a situation where a distributed system is split into two or more groups of nodes that cannot communicate with each other due to a network failure.

## CAP Theorem 
- CAP Theorem states that in a distributed system, you can only guarantee two out of the following three at the same time:
    - C → Consistency - Every read gets the latest write.
    - A → Availability - Every request gets a response, even if data is stale.
    - P → Partition Tolerance - System continues to operate despite network failures.
- In the presence of a network partition, the system must choose between consistency and availability. 
- Most modern systems understands that network failures can happen so Partition Tolerance cannot be guaranteed so they choose either CP (MongoDB) or AP (Cassandra, DynamoDB) based on business requirements.


## Database Design
- Database Design involving modeling the data, defining their relationship and constraints.
- This enables the system to perform read and writes at scale.
### Principles
- Identify Functional requirements
    - What entities exist?
    - What operations?
        - Create / Read / Update / Delete
        - Queries?
- Identify Non-Functional requirements
    - Read-heavy or write-heavy?
        - Read-heavy → Denormalize + cache
        - Write-heavy → Normalize + batch writes
    - Latency requirements?
        - Sub-ms → caching (Redis)
        - Acceptable delay → DB only
    - Scale (10K vs 100M users)?
    - Consistency requirements? 
        - Strong consistency → SQL
        - Eventual consistency → NoSQL
- Identify core entities and define the relationships
```
User
Product
Order
OrderItem
Payment

User → Orders (1:N)
Order → OrderItems (1:N)
Product → OrderItems (1:N)
```
- Choose Database type (SQL or NoSQL)
- Normalize data to avoid redundancy
- Define keys and constraints
```sql
PRIMARY KEY (id)
FOREIGN KEY (user_id)
UNIQUE (email)
```
- Design the Indexing strategy based on the queries

```sql
SELECT * FROM orders WHERE user_id = ?

CREATE INDEX idx_orders_user ON orders(user_id);
```
- Plan for scale
    - Partitioning / Sharding
    - Replication
    - Caching (Redis)

## N+1 Problem 
- The N+1 problem occurs when an application executes one query to fetch a list of entities and then executes additional queries for each entity to fetch related data. 
- This leads to performance issues due to excessive database calls. 
- It can be solved using techniques like JOIN queries, batch fetching, eager loading, or DTO projections to reduce the number of queries.

## Normalization
- Normalization is the process of structuring a relational database to minimize redundancy and dependency by organizing data into multiple related tables.
- Eliminate duplication and organizing data to avoid redundancy.
- One piece of data should be available only once.

#### Unnormalized Table 

| order_id | user_name | product | price |
| --- | --- | --- | --- |
| 101 | John | Laptop | 100000 |
| 102 | John | Mouse | 500 |
- Here `user_name` is repeated.
- If name changes, multiple updates is required to make table consistent.
- After Normalization, `users`, `orders`, `order_items` tables are created. 


| user_id | name |
| --- | --- |
| 1 | John |


| order_id | user_id |
| --- | --- |
| 101 | 1 |
| 102 | 1 |


| order_id | product | price |
| --- | --- | --- |
| 101 | Laptop | 100000 |
| 102 | Mouse | 500 |


## Normal forms
- Normal forms are used to eliminate redundancy and dependency issues in database design. 1NF ensures atomic values, 2NF removes partial dependencies in composite keys, 3NF eliminates transitive dependencies, and BCNF further ensures that every determinant is a candidate key. In practice, most systems are designed up to 3NF and selectively denormalized for performance.
- Consider this unnormalized table.

| order_id | product_id | product_name | customer_id | customer_name | city |
| --- | --- | --- | --- | --- | --- |
| 101 | 1 | Laptop | 10 | John | New York |
| 101 | 2 | Mouse | 10 | John | New York |
| 102 | 1 | Laptop | 11 | Alice | Austin |

### 1 NF 
- No repeating groups
- Atomic values (no arrays, no nested data)
- Violation

| order_id | products |
| --- | --- |
| 101 | Laptop, Mouse|

- Fix

| order_id | products |
| --- | --- |
| 101 | Laptop|
| 101 | Mouse|

### 2 NF
- Must be in 1NF
- No partial dependency on composite key
- In the unnormalized table, there is two primary keys (composite keys) `order_id` and `product_id`, but `product_name` only depends on `product_id`.
- Fix

| product_id | product_name |
| --- | --- |
| 1 |  Laptop|
| 2| Mouse |


| order_id | product_id |
| --- | --- |
| |  |

### 3 NF
- Must be in 2NF
- No transitive dependency, `A → B → C` A indirectly determines C, this is not allowed.
- In the unnormalized table, `customer_id → customer_name` and `customer_name → city`, so indirectly `customer_id → city`.
- Fix, split tables to `customers` and `cities`.

| customer_id | customer_name | city_id |
| --- | --- | --- |
| | | |


| city_id| city_name |
| --- | --- |
| | |

### BCNF (Boyce-Codd Normal Form) 
- Every determinant must be a candidate key.
- Consider this table

| teacher | subject | room |
| --- | --- | --- |
| A | Math | 101|
| B | Math | 101|
- In the above table, 
    - Each subject has one room
    - Each teacher teaches one subject
    - But the problem is `subject → room`, but `subject` is not a key.
- Fix, split tables to `subjects` and `teachers`


| subject | room |
| --- | --- |
| | |




| teacher | subject |
| --- | --- |
| | |


## JPA
- JPA (Java Persistence API) is a Java specification (standard) that defines how Java objects should be mapped to relational databases.
- It is a set of interfaces + annotations + rules.
- Earlier every ORM had its own API, so code was tightly coupled to specific implementation (Switching ORM = rewriting code). 
- JPA was created to solve this.
- It provides interfaces like `EntityManager` and standard annotations such as `@Entity` and `@Id`. It does not perform persistence itself but is implemented by ORM frameworks like Hibernate.

## Hibernate
- Hibernate is an ORM (Object-Relational Mapping) framework for Java that maps Java objects to relational database tables and handles database operations automatically.
- It lets you work with Java objects instead of SQL queries, while it handles SQL behind the scenes.
- Hibernate implements the JPA specification and handles SQL generation, caching, and object lifecycle management internally, allowing developers to work with objects instead of writing SQL.
- Without Hibernate we will need to add a lot of boilerplate code and use manual SQL making it harder to maintain.

| Component | Role |
| --- | --- |
| JPA |  Standard (interfaces) |
| Hibernate | Implentation |
| Spring Data JPA | Convenience layer |

- Hibernate defines an Object ↔ Table Mapping
    - Hibernate maps Class → Table and Fields → Columns

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
}
```
- Hibernate maintains a Session (Persistence Context)
    - It tracks objects in memory
    - Ensures consistency
    - Avoids duplicate queries
- Hibernate generates SQL dynamically

```java
userRepository.save(new User("John"));
```

```sql
//Hibernate checks entity metadata and generates and executes SQL
INSERT INTO users (name) VALUES ('John');
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
- Spring Data JPA supports `PagingAndSortingRepository`
```java
findAll(Pageable pageable)
findAll(Sort sort)
```
- Spring Data JPA supports Query Methods (Derived Queries) that Spring generates automatically.
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
#### Spring Data JPA Solution
- By using pure JPA we need to write repetitive code 

```java
@PersistenceContext
private EntityManager em;

public User findById(Long id) {
    return em.find(User.class, id);
}
```
- With Spring Data JPA, CRUD methods are auto-implemented after defining this

```java
public interface UserRepository extends JpaRepository<User, Long> {
}
```

## JPA vs Hibernate vs Spring Data JPA 
- JPA is a specification that defines how Java objects should be persisted to relational databases. Hibernate is an implementation of JPA that actually performs ORM operations like SQL generation, caching, and entity lifecycle management. Spring Data JPA is a higher-level abstraction built on top of JPA that simplifies data access by providing repository-based APIs and reducing boilerplate code. In a typical Spring Boot application, we use Spring Data JPA, which internally uses JPA APIs implemented by Hibernate.

| Aspect | JPA | Hibernate | Spring Data JPA |
| --- | --- | --- | --- |
| Type | Specification | Implementation | Abstraction |
| Purpose | Define ORM standards | ORM | Simplify usage |
| SQL Execution | No | Yes | Yes |
| Dependency | None | Implements JPA | Depends on JPA |


```
Spring Data JPA
   ↓
EntityManager.persist()   (JPA)
   ↓
Hibernate
   ↓
Database
```

- Hibernate is rarely used directly, but if we want to use directly then we create and use `EntityManager` and `Session`.

```java
Session session = sessionFactory.openSession();
session.save(user);

entityManager.persist(user);
```
- This can be avoided if we use Spring Data JPA

```java
userRepository.save(user);
```

## Persistence Context
- Persistence Context is a memory space (cache) where JPA/Hibernate manages entity objects during a transaction.
- It is a first-level cache managed by JPA where entities are stored and tracked during a transaction. 
- It ensures that each entity is uniquely represented, tracks changes automatically through dirty checking, and synchronizes updates with the database during flush or commit. It improves performance by reducing redundant database calls and maintains consistency within a transaction. 

## EntityManager 
- EntityManager is a JPA interface used to interact with the persistence context and perform database operations on entities.
- It is the core API of JPA.
- It provides methods to perform CRUD operations, manage entity lifecycle, and execute queries. It acts as a bridge between the application and the persistence context, and internally delegates work to the JPA implementation like Hibernate.

```java
entityManager.persist(user);   // CREATE
entityManager.find(User.class, 1L); // READ
entityManager.merge(user);     // UPDATE
entityManager.remove(user);    // DELETE
```

## Session 
- Session is the core Hibernate interface used to manage persistence operations, entity lifecycle, and the first-level cache. It is the underlying implementation behind JPA’s EntityManager.
- While EntityManager provides a standard API, Session offers additional Hibernate-specific features and fine-grained control over persistence operations.
- You typically don’t use Session directly. Instead we use Spring Data JPA that automatically uses EntityManager and creates a session.

```java
Session session = sessionFactory.openSession();
Transaction tx = session.beginTransaction();

User user = new User("John");
session.save(user);

tx.commit();
session.close();
```
- It provides all the CRUD Operations

```java
Session session = sessionFactory.openSession();

session.save(user);       // CREATE
session.get(User.class, 1L); // READ
session.update(user);     // UPDATE
session.delete(user);     // DELETE
```

## Entity
- An Entity is a Java class annotated with `@Entity` that represents a table in a relational database. Each instance of the class corresponds to a row in the table, and fields are mapped to columns. JPA uses these entities to manage persistence, and Hibernate uses the metadata to generate SQL and perform database operations.
- It is a Java object that represents a row in a database table. It is a POJO (Plain Old Java Object) annotated so that JPA/Hibernate can map it to a table.

```java
import jakarta.persistence.*;

@Entity // Marks this class as an entity
@Table(name = "users") // Optional: maps to table name
public class User {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name") // Optional mapping
    private String name;

    // Default constructor (required)
    public User() {}

    public User(String name) {
        this.name = name;
    }

    // Getters and setters
}
```

## `@Table`

## `@Access`

## `@Id` 

## `@GeneratedValue`

## `@EmbeddedId` 

## `@Column` 

## `@OneToOne` 

## `@Cascade` 

## `@PrimaryKeyJoinColumn` 

## One-to-One Relationship
- A One-to-One relationship means one row in table A is related to exactly one row in table B 
- Consider this example, `users` table stores login/account data, `user_profiles` table stores profile-specific info
- Each user has one profile and each profile belongs to one user.

| id | email | password_hash |
| --- | --- | --- |
| 1 | john@email.com | xxxx |
| 2 | alice@email.com | yyyy |



| id | user_id | full_name | phone |
| --- | --- | --- | --- |
| 101 | 1 | John | 99999 |
| 102 | 2 | Alice | 88888 |


```java
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    // Inverse side of the relationship
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile profile;

    public User() {}

    public User(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
        if (profile != null) {
            profile.setUser(this); // keep both sides consistent
        }
    }

    // getters and setters
}
```

```java
import jakarta.persistence.*;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    private String phone;

    // Owning side: this side holds the foreign key column
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public UserProfile() {}

    public UserProfile(String fullName, String phone) {
        this.fullName = fullName;
        this.phone = phone;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // getters and setters
}
```

## One-to-Many Relationship 
- A One-to-Many relationship means one parent row is related to many child rows
- Consider this example, `Order` and `OrderItem` where one order can have many order items and each order item belongs to one order 

| id | customer_id | status |
| --- | --- | --- |
| 1001 | 1 | PLACED |



| id  | order_id | product_id | quantity | unit_price |
| --- | --- | --- | --- | --- |
| 5001 | 1001 | 11 | 2 | 799|
| 5002 | 1001 | 15 | 1 | 1499 |


```java
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private String status;

    // Inverse side
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Order() {}

    public Order(Long customerId, String status) {
        this.customerId = customerId;
        this.status = status;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }

    // getters and setters
}
```

```java
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    // Owning side
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public OrderItem() {}

    public OrderItem(Long productId, Integer quantity, BigDecimal unitPrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    // getters and setters
}
```
## Many-to-Many Relationship
- A Many-to-Many relationship means many rows in table A can relate to many rows in table B
- Consider this example, `User` and `Role`, one user can have many roles and one role can be assigned to many users 

| id | email |
| --- | --- |
| 1 | john@email.com |
| 2 | alice@email.com |


| id | name |
| --- | --- |
| 10 |  ADMIN |
| 20 |  SUPPORT |

- In this case we need to create a join table `user_roles`

```sql
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role_id) REFERENCES roles(id)
);
```

| user_id | role_id |
| --- | --- |
| 1 | 10|
| 1 | 20|
| 2 | 20|


```java
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User() {}

    public User(String email) {
        this.email = email;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    // getters and setters
}
```

```java
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    // getters and setters
}
```
- We need not create a separate entity for `user_roles` in a basic `@ManyToMany` but you should create one when the join table has business meaning or extra columns.

## First level caching

## Second level caching

## Query cache 

## Lazy Loading vs Eager Loading
- Lazy loading fetches related data only when it is accessed, reducing initial query cost but potentially causing N+1 query problems. 
```java
@OneToMany(fetch = FetchType.LAZY)
```
- Eager loading fetches related data immediately, avoiding multiple queries but possibly leading to over-fetching and performance issues. 
```java
@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
```
- In practice, lazy loading is preferred by default, with explicit fetching strategies like JOIN FETCH or entity graphs used to optimize queries.
- Use Lazy Loading When
    - Large collections (orders, logs)
    - Data not always needed
    - Performance-sensitive APIs
- Use Eager Loading When
    - Small, always-needed data
    - One-to-one relationships 

## Cascade Type
- Cascade Type defines how operations performed on a parent entity are automatically propagated to its related child entities.
- Consider tables with parent child relationship, cascade types controls the behavior on what happens to related entities when action is performed on parent entity.

#### Types 
- PERSIST - When parent is saved → child is saved

```java
cascade = CascadeType.PERSIST
```
- MERGE - When parent is updated → child is updated

```java
cascade = CascadeType.MERGE
```
- REMOVE - When parent is deleted → child is deleted

```java
cascade = CascadeType.REMOVE
```
- REFRESH - Reload parent + child from DB 

```java
cascade = CascadeType.REFRESH
```
- DETACH - Detach parent + child from persistence context

```java
cascade = CascadeType.DETACH
```
- ALL - PERSIST + MERGE + REMOVE + REFRESH + DETACH

```java
cascade = CascadeType.ALL
```

## orphanRemoval 

## save() vs saveAndFlush() 

## persist() 

## `getCurrentSession()` 

## `openSession()` 

## `get()`

## `load()` 

## Optimistic Locking and Pessimistic Locking 
- Optimistic locking allows concurrent access, but detect conflicts at update time.
- It allows concurrent access and detects conflicts at the time of update using mechanisms like versioning, making it suitable for systems with low contention.
- Optimistic locking scales well and gives better performance as there are no locks.
- Pessimistic locking locks the data before modifying, preventing others from accessing it.
- It locks row/table before access to prevent conflicts, ensuring correctness but reducing concurrency due to blocking and potential deadlocks.
- Pessimistic Locking helps to conflicts early, but it is blocking and can cause potential Deadlocks.

## JPQL

## Criteria API

## Native Query 

## MongoDB
- MongoDB is a document-oriented NoSQL database that stores data as JSON-like documents (Binary JSON - BSON) instead of rows and columns.

```
Collections → Documents → Fields → Embedded Data
```
- MongoDB can handle both read-heavy and write-heavy workloads, but it is often preferred for write-heavy and high-ingest systems
