🚀 SecurityApp
· Spring Boot 4 
· Spring Security 6 
· JWT 
· PostgreSQL 
· Role-Based Authentication


**_📌 Overview**_

SecurityApp is a fully working JWT Authentication + Authorization system using:

Spring Boot 4

Spring Security 6

PostgreSQL

JWT Token-Based Authentication

Role-Based Access Control (RBAC): USER, ADMIN

RESTful layered architecture

It follows production-quality practices and is perfect for:

✔ Portfolio projects
✔ Interview preparation
✔ Real-world microservices
✔ Scalable authentication modules

### **_📁 Project Structure_**

src/main/java/com/example/securityapp
│
├── SecurityappApplication.java
│
├── config/
│   ├── SecurityConfig.java
│   ├── JwtUtil.java
│   ├── JwtAuthenticationFilter.java
│   └── JwtAuthenticationEntryPoint.java
│
├── controller/
│   ├── AuthController.java
│   ├── UserController.java
│   └── AdminController.java
│
├── dto/
│   ├── AuthRequest.java
│   ├── AuthResponse.java
│   ├── RegisterRequest.java
│   ├── UpdateUserRequest.java
│   └── AdminUpdateUserRequest.java
│
├── entity/
│   └── UserEntity.java
│
├── repository/
│   └── UserRepository.java
│
└── service/
├── AuthService.java
├── UserService.java
└── impl/
├── AuthServiceImpl.java
└── UserServiceImpl.java

### 🎯 Features

🔐 Authentication

Register user

Login (JWT issuance)

Stateless authentication (no sessions)

JWT validation middleware

##### 🛡 Authorization

Role-based access (USER, ADMIN)

Protect admin APIs

Restrict /auth/register-admin to admins only

### **_👤 User Features_**

View profile

Update own profile

##### 🏛 Admin Features

Get all users

Update user role

Delete user

Create new admins

### ⚙️ **_Tech Stack_**

Technology	Purpose
Spring Boot 4	REST API backend
Spring Security 6	Authentication & RBAC
JWT	Stateless security
PostgreSQL	Database
Hibernate/JPA	ORM
Maven	Build tool
Lombok	Reduce boilerplate
🚀 Getting Started
1️⃣ Clone Repository
git clone https://github.com/your-username/securityapp.git
cd securityapp

#### 2️⃣ Setup PostgreSQL Database

CREATE DATABASE securitydb;

#### 3️⃣ Configure application.properties

spring.datasource.url=jdbc:postgresql://localhost:5432/securitydb
spring.datasource.username=postgres
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

#### 4️⃣ Run the Application

mvn spring-boot:run

#### 🔥 API Endpoints

🧑‍💻 Auth APIs
Method	Endpoint	Description	Auth
POST	/auth/register	Register a normal user	Public
POST	/auth/login	Login & get JWT token	Public
POST	/auth/register-admin	Create admin	Admin only
👤 User APIs
Method	Endpoint	Description	Auth
GET	/user/profile	Get logged-in user profile	User/Admin
PUT	/user/update	Update logged-in profile	User/Admin
🏛 Admin APIs
Method	Endpoint	Description	Auth
GET	/admin/users	Get all users	Admin
PUT	/admin/users/{id}	Update user + role	Admin
DELETE	/admin/users/{id}	Delete user	Admin

### 🔑 Authentication Flow

User registers

User logs in

Backend returns JWT

Client includes token in header:

Authorization: Bearer <token>


JwtAuthenticationFilter validates token

SecurityContextHolder stores authenticated user

Spring Security checks permissions based on role

#### 🧪 cURL Examples

Register User
curl -X POST http://localhost:8080/auth/register \
-H "Content-Type: application/json" \
-d '{"email":"user@gmail.com","password":"12345"}'

##### Login

curl -X POST http://localhost:8080/auth/login \
-H "Content-Type: application/json" \
-d '{"email":"user@gmail.com","password":"12345"}'

##### Register Admin (requires token)

curl -X POST http://localhost:8080/auth/register-admin \
-H "Authorization: Bearer <ADMIN_TOKEN>" \
-H "Content-Type: application/json" \
-d '{"email":"admin2@gmail.com","password":"secret"}'

### **_📘 Learning Outcomes_**

By building this project you understand:

✔ Spring Security internals (filters, entry points, contexts)
✔ JWT generation, validation & claims
✔ Role-based authorization (hasRole, hasAuthority)
✔ User management CRUD
✔ Service-repository pattern
✔ Secure API design
✔ Stateless authentication architecture

Great for interviews & production-ready microservices.

##### **_📄 License_**

This project is licensed under the MIT License.