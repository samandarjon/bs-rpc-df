# Kafka Avro Serialization Demo

This project demonstrates how to use Avro for serializing and deserializing Kafka messages, including schema evolution.

## Project Structure

- **kafka-producer**: Spring Boot application that produces Kafka messages serialized with Avro
- **kafka-consumer**: Spring Boot application that consumes Kafka messages deserialized with Avro

## Prerequisites

- Java 17
- Docker and Docker Compose

## Setup

### 1. Start Kafka and Schema Registry

Create a `docker-compose.yml` file in the project root:

```yaml
version: '3'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.5.1
    container_name: zookeeper
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000

  kafka:
    image: confluentinc/cp-kafka:7.5.1
    container_name: kafka
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:29092,PLAINTEXT_HOST://localhost:9092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1

  schema-registry:
    image: confluentinc/cp-schema-registry:7.5.1
    container_name: schema-registry
    depends_on:
      - kafka
    ports:
      - "8082:8082"
    environment:
      SCHEMA_REGISTRY_HOST_NAME: schema-registry
      SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS: kafka:29092
      SCHEMA_REGISTRY_LISTENERS: http://0.0.0.0:8082
```

Start the containers:

```bash
docker-compose up -d
```

### 2. Create Kafka Topic

Create the Kafka topic:

```bash
docker exec kafka kafka-topics --create --topic avro-messages --bootstrap-server kafka:29092 --partitions 1 --replication-factor 1
```

## Building and Running

### 1. Build the Projects

```bash
# Build the producer
cd kafka-producer
./gradlew build

# Build the consumer
cd ../kafka-consumer
./gradlew build
```

### 2. Run the Consumer

```bash
cd kafka-consumer
./gradlew bootRun
```

### 3. Run the Producer

In a new terminal:

```bash
cd kafka-producer
./gradlew bootRun
```

## Testing

### 1. Send a Message with Schema v1

```bash
curl -X POST "http://localhost:8081/api/messages/v1?content=Hello%20Avro"
```

### 2. Send a Message with Schema v2

```bash
curl -X POST "http://localhost:8081/api/messages/v2?content=Hello%20Avro%20v2&priority=HIGH"
```

## Observing Schema Evolution

1. Check the Schema Registry to see the registered schemas:

```bash
curl -X GET http://localhost:8082/subjects
curl -X GET http://localhost:8082/subjects/avro-messages-value/versions
curl -X GET http://localhost:8082/subjects/avro-messages-value/versions/1
curl -X GET http://localhost:8082/subjects/avro-messages-value/versions/2
```

2. Observe in the consumer logs how messages with different schema versions are handled.

## Cleanup

Stop and remove the Docker containers:

```bash
docker-compose down
```