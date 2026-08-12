# RoadWatch 🚦

**RoadWatch** is a backend-focused, microservices-based platform designed to help users discover and share **real-time local road conditions**.

The core idea is simple:

> **People already on the road can report what's happening around them, and nearby users can quickly discover relevant updates.**

Examples include:

* 🚗 Traffic jams
* 🚧 Road construction
* 🕳️ Potholes
* 🚨 Accidents
* 🌧️ Waterlogging
* 🛣️ Road closures
* ⚠️ Other local road conditions

The project is being built with a strong focus on **production-grade backend engineering, distributed systems, scalability, reliability, security, and edge-case handling**.

---

## 🎯 Core Idea

A user can create a road-related post containing:

* Description
* Category
* Location (latitude/longitude)
* Images/videos

Other users can then request posts near their current location.

For example:

```text
User Location
     ↓
Latitude + Longitude
     ↓
Search nearby posts
     ↓
Posts within ~5 km
     ↓
Return relevant road updates
```

The long-term goal is to make RoadWatch useful for **fast, location-aware road information** rather than relying only on centralized traffic information.

---

# 🏗️ Architecture

RoadWatch is being developed as a collection of independently deployable services.

```text
                         ┌─────────────────┐
                         │     Client      │
                         └────────┬────────┘
                                  │
                                  ▼
                         ┌─────────────────┐
                         │   API Gateway   │
                         └────────┬────────┘
                                  │
                    ┌─────────────┴─────────────┐
                    │                           │
                    ▼                           ▼
             ┌─────────────┐             ┌─────────────┐
             │ User Service│             │ Post Service│
             └──────┬──────┘             └──────┬──────┘
                    │                           │
             ┌──────┴──────┐              ┌─────┴──────┐
             ▼             ▼              ▼            ▼
          MySQL         Redis          MySQL       Cloudinary
             │
             ▼
           Kafka
             │
             ▼
    ┌────────────────────┐
    │ Notification       │
    │ Service            │
    └─────────┬──────────┘
              │
              ▼
           Email
```

The architecture will evolve as more services and infrastructure are introduced.

---

# 🔐 User Service

The User Service manages authentication, registration and user identity.

### Registration Flow

RoadWatch does not immediately create a permanent user account when registration begins.

```text
Client
  ↓
Signup
  ↓
Validate input
  ↓
Check existing user
  ↓
Create Pending Registration
  ↓
Hash Password
  ↓
Generate OTP
  ↓
Store OTP in Redis
  ↓
Publish Kafka Event
  ↓
Notification Service
  ↓
Send Verification Email
  ↓
User verifies OTP
  ↓
Create permanent User
  ↓
Generate JWT
```

### Current authentication features

* User registration
* Email validation
* Password hashing using BCrypt
* Pending registration state
* OTP generation using `SecureRandom`
* Redis-based OTP storage
* 15-minute OTP expiration using Redis TTL
* OTP verification
* JWT authentication
* Login
* Global exception handling
* Bean validation
* Database uniqueness constraints

### OTP Design

OTP is treated as temporary state.

```text
Redis

otp:{registrationId}
        │
        └── 4-digit OTP
        └── TTL: 15 minutes
```

Redis automatically removes the OTP after expiration.

The PostgreSQL/MySQL pending-registration record can be retained separately for lifecycle management and cleanup.

---

# 📨 Event-Driven Communication

RoadWatch uses **Apache Kafka** for asynchronous communication between services.

For example:

```text
User Service
     │
     │ OTP Verification Event
     ▼
   Kafka
     │
     ▼
Notification Service
     │
     ▼
   Email
```

After successful registration:

```text
User Service
     │
     │ UserRegisteredEvent
     ▼
   Kafka
     │
     ▼
Notification Service
```

This keeps the User Service decoupled from email delivery.

---

# 📍 Post Service

The Post Service handles road-condition reports.

A post can contain:

```text
User
 │
 ├── Description
 ├── Category
 ├── Latitude
 ├── Longitude
 └── Media
       ├── Image
       └── Video
```

### Location-Based Retrieval

Users can request nearby road updates using their coordinates.

```text
User
 │
 │ latitude + longitude
 ▼
Post Service
 │
 │ distance calculation
 ▼
Nearby Posts
 │
 └── approximately 5 km radius
```

The system calculates geographical distance rather than simply filtering latitude/longitude values.

---

# 🖼️ Media Uploads

RoadWatch supports images and videos attached to posts.

The current architecture separates media storage from the relational database.

```text
Client
  ↓
Post Service
  ↓
Validate Media
  ↓
Cloudinary
  ↓
Media URL
  ↓
Database
```

The database stores the media reference/URL rather than the actual binary file.

The upload flow also considers failure scenarios such as:

* Invalid file type
* File size limits
* Multiple file uploads
* Partial upload failures
* Cleanup of successfully uploaded files when database persistence fails

---

# ⏳ Time-Aware Road Updates

One planned feature is **category-based post lifetime**.

Not every road report remains useful for the same amount of time.

For example:

```text
Traffic Jam
    ↓
Useful for a few hours

Accident
    ↓
Potentially useful for several hours

Road Construction
    ↓
Potentially useful for days/weeks

Permanent Road Issue
    ↓
Long-term relevance
```

Instead of treating every post equally, RoadWatch will explore **time-based relevance and expiration policies depending on the category**.

This can eventually allow the system to automatically reduce or remove stale information.

---

# ⚙️ Engineering Goals

This project is being developed with more than functionality in mind.

Key engineering areas being explored include:

### Distributed Systems

* Service-to-service communication
* Event-driven architecture
* Asynchronous processing
* Eventual consistency
* Failure handling
* Retry strategies
* Idempotency
* Race conditions

### Caching & Temporary State

* Redis
* TTL
* Temporary verification state
* Cache invalidation
* Distributed-state considerations

### Messaging

* Apache Kafka
* Producers
* Consumers
* Topics
* Consumer failures
* Retry mechanisms
* Dead-letter topics
* Message delivery semantics

### Security

* Spring Security
* JWT
* Password hashing
* Authentication
* Authorization
* Input validation
* Secure OTP generation
* Database constraints

### Database Engineering

* Spring Data JPA
* Transactions
* Unique constraints
* Pagination
* Location-based queries
* Data lifecycle management

### Reliability

The system is being designed with edge cases in mind, including:

* Duplicate registration attempts
* Expired OTPs
* Invalid OTPs
* Multiple verification requests
* Kafka delivery failures
* Email delivery failures
* Partial media uploads
* Database failures
* Stale location reports
* Concurrent requests

---

# 🛠️ Technology Stack

### Backend

* Java
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate

### Databases & Storage

* MySQL
* Redis
* Cloudinary

### Messaging

* Apache Kafka

### Infrastructure

* Docker
* Docker Compose

### Communication

* REST APIs
* Kafka events

### Authentication

* JWT
* BCrypt

### Notifications

* Spring Mail

---

# 🚧 Current Development

RoadWatch is actively being developed.

Current focus:

* [x] User registration
* [x] Password hashing
* [x] Pending registration flow
* [x] Redis OTP storage
* [x] OTP expiration
* [x] Kafka OTP event
* [x] Notification Service
* [x] Email verification
* [x] JWT generation
* [x] User login
* [x] Post creation
* [x] Image/video uploads
* [x] Location-based post retrieval
* [ ] Complete JWT authorization across services
* [ ] Post ownership authorization
* [ ] OTP resend flow
* [ ] Expired registration cleanup
* [ ] Kafka retry/DLQ strategy
* [ ] Idempotent event processing
* [ ] API Gateway
* [ ] Observability and distributed tracing
* [ ] Time-based post relevance
* [ ] Additional road-condition features

---

# 🚀 Vision

RoadWatch is being built as an evolving backend engineering project rather than a simple CRUD application.

The long-term goal is to explore how a real-world platform can handle:

```text
High request volume
       +
Location-based data
       +
Real-time updates
       +
Asynchronous communication
       +
Temporary state
       +
Media processing
       +
Authentication
       +
Distributed failures
```

The project will continue evolving as new backend and distributed-system concepts are explored.

---

## 📌 Repository

This repository contains the backend implementation of **RoadWatch**.

More services, infrastructure components, and features will be added as development continues.

**Built to learn. Designed to scale. 🚀**
