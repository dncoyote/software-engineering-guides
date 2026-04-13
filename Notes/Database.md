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
    - Slower `UPDATE`, `INSERT`
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

## Sharding
- Sharding is a technique of splitting a large dataset across multiple database nodes (shards) so that each node stores only a subset of the data.
- It enables horizontal scaling, allowing applications to handle increased data volume and high traffic
### Types
- Range based Sharding
- Hash based Sharding
- Directory based Sharding

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
## GROUP BY

- `GROUP BY` clause is used to group rows that have the same values in specified columns into summary rows.
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

## One-Many Relationship
- In a one-to-many relationship, one record in the first table (parent table) can be associated with multiple records in the second table (child table), but each record in the second table is associated with only one record in the first table.

```sql
CREATE TABLE department (
    department_id INT PRIMARY KEY,
    department_name VARCHAR(255)
);

CREATE TABLE employee (
    employee_id INT PRIMARY KEY,
    employee_name VARCHAR(255),
    department_id INT,
    FOREIGN KEY (department_id) REFERENCES department(department_id)
);
```
- The department table stores information about departments.
- The employee table stores information about employees.
- The employee table has a foreign key (department_id) that references the primary key of the department table.
- An employee belongs to one department, but a department can have multiple employees.

## Many-to-One Relationship
- A many-to-one relationship is essentially the reverse of a one-to-many relationship. In a many-to-one relationship, many records in the first table can be associated with one record in the second table.

```sql
CREATE TABLE department (
    department_id INT PRIMARY KEY,
    department_name VARCHAR(255)
);

CREATE TABLE employee (
    employee_id INT PRIMARY KEY,
    employee_name VARCHAR(255),
    department_id INT,
    FOREIGN KEY (department_id) REFERENCES department(department_id)
);
```
- The department table stores information about departments.
- The employee table stores information about employees.
- The employee table has a foreign key (department_id) that references the primary key of the department table.
- Many employees can belong to the same department, but each employee belongs to only one department.

## Many-Many Relationship

In a relational database, the relationship between the "author" and "book" tables is typically modeled using a foreign key. There are different types of relationships, and in this case, it sounds like a many-to-many relationship because one author can write many books, and one book can have multiple authors.

To represent a many-to-many relationship between the "author" and "book" tables, you need a third table, often called a junction or linking table. This table serves to link authors to books, indicating which authors are associated with which books. Here's a basic schema for such a setup:

```sql
CREATE TABLE author (
    author_id INT PRIMARY KEY,
    author_name VARCHAR(255)
);

CREATE TABLE book (
    book_id INT PRIMARY KEY,
    book_title VARCHAR(255)
);

CREATE TABLE author_book (
    author_id INT,
    book_id INT,
    PRIMARY KEY (author_id, book_id),
    FOREIGN KEY (author_id) REFERENCES author(author_id),
    FOREIGN KEY (book_id) REFERENCES book(book_id)
);
```
- The author table stores information about authors.
- The book table stores information about books.
- The author_book table is the junction table that establishes the many-to-many relationship. It contains foreign keys referencing the primary keys of the author and book tables. The combination of author_id and book_id forms a composite primary key for this table.
- One author can be associated with multiple books.
- One book can have multiple authors.
- The author_book table keeps track of these associations.

```sql
-- Insert authors
INSERT INTO author (author_id, author_name) VALUES
(1, 'Author A'),
(2, 'Author B'),
(3, 'Author C');

-- Insert books
INSERT INTO book (book_id, book_title) VALUES
(101, 'Book X'),
(102, 'Book Y'),
(103, 'Book Z');

-- Associate authors with books
INSERT INTO author_book (author_id, book_id) VALUES
(1, 101),  -- Author A wrote Book X
(1, 102),  -- Author A also wrote Book Y
(2, 101),  -- Author B wrote Book X as well
(3, 103);  -- Author C wrote Book Z
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


## Normalization
- Organizing data to avoid redundancy.

## MongoDB
- MongoDB is a document-oriented NoSQL database that stores data as JSON-like documents (Binary JSON - BSON) instead of rows and columns.

```
Collections → Documents → Fields → Embedded Data
```
- MongoDB can handle both read-heavy and write-heavy workloads, but it is often preferred for write-heavy and high-ingest systems
