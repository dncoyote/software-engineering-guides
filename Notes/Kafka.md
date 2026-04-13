# Kafka
## Kafka
- Kafka is a distributed event streaming platform that acts as a durable, scalable, partitioned commit log. It allows producers to publish events and consumers to read them independently with configurable delivery guarantees.
#### Broker
- A Kafka server instance.
#### Cluster
- A group of brokers working together.
#### Topic
- A logical category of events.
#### Partition
- A topic is divided into partitions.
#### Offset
- Every message in a partition has a sequential number.
#### Producer
- Writes messages to Kafka.
- Producer decides:
    - Topic
    - Partition (optional)
    - Key
    - Acknowledgment strategy
#### Consumer
- Reads messages from Kafka.
- Consumers:
    - Pull data
    - Track offsets
    - Can replay old data
#### Consumer group
- A consumer group is a group of consumers sharing the same group ID
- They distributes partitions among members
- One partition → one consumer in a group
- Multiple groups → same message processed independently
#### Retention
- Kafka does NOT delete after consumption.
- Data retention can be based on:
    - Time (e.g., 7 days)
    - Size (e.g., 100GB)
## Producer
- A Kafka Producer is a client application that publishes (writes) records to a Kafka topic.
- They appends records to a partition of a topic with configurable durability, ordering, and retry semantics.
- Kafka Producer must
    - Serialize data
    - Decide which partition to send to
    - Batch records efficiently
    - Handle retries
    - Maintain ordering (if configured)
    - Ensure durability (based on acks)
    - Optionally guarantee idempotence
## Consumer
- A Kafka Consumer is a client application that reads records from Kafka topics and processes them while tracking its progress using offsets.
- They pulls records from assigned partitions and commits offsets to maintain processing state.
- Kafka Consumer 
    - is a log reader
    - can remembers where it left off
    - can rewind if needed
- Kafka does NOT delete messages after consumption.
- Consumers track their own position. That position is called offset.
- Kafka Consumer must
    - Join a consumer group
    - Get partition assignments
    - Poll messages
    - Process them
    - Commit offsets
    - Handle rebalancing
## Topic
- Kafka Topic is a named, logical stream of records to which producers write and from which consumers read.
- It is not a queue.
- A topic consists of partitions.
## Consumer Group
- A Consumer Group is a set of consumers sharing the same group.id that collaboratively consume records from a topic by dividing partitions among themselves.
- They improve scalability by increasing consumers and improve fault-tolerance.
#### Rules
- A partition can be assigned to only one consumer inside a group at a time.
- Across groups: Same message can be consumed multiple times.
- Maximum parallelism = Number of partitions
## Partition vs consumer group relationship
- When a consumer group subscribes:
    - All consumers in the group register with Kafka.
    - Kafka assigns partitions to consumers.
    - Each partition is assigned to exactly one consumer in that group.
    - This creates a 1-to-1 mapping:
- One partition → one consumer.
    - Ordering is guaranteed per partition.
    - Offset tracking is per partition per group.
- If two consumers read same partition concurrently:
    - Offset commits race.
    - Order breaks.
## Event streaming
- Event streaming is the practice of treating events as an immutable, ordered sequence that systems can publish to and subscribe from independently.
- This ensures that systems are Decoupled, Scalable and Asynchronous.
## Kafka Configurations
#### Broker / Cluster configurations

## Apache Kafka vs Rabbit MQ

| Property               | Kafka                                                  | RabbitMQ                                               |
|------------------------|--------------------------------------------------------|--------------------------------------------------------|
| Definition             | Distributed event streaming platform (log-based)       | Traditional message broker (queue-based)               |
| Message Retention      | Stores messages after consumption (configurable)       | Messages removed once consumed and acknowledged        |
| Replay                 | Consumers can re-read messages using offsets           | Not supported by default (requires re-queuing/DLQ)     |
| Throughput vs Latency  | Optimized for high throughput                          | Optimized for low latency                              |
| Use case               | Event streaming, analytics, log aggregation            | Task queues, background jobs, request-response         |
