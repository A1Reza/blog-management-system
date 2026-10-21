# 📝 Blog Management System

A full-stack blog management application developed with **Java 21, Jakarta Servlets, Hibernate, JPA, PostgreSQL, HTML, CSS, and JavaScript**. The application enables users to create accounts, authenticate securely, publish blog posts, and manage their own content through a REST-style API.

## ✨ Features

- **User Registration:** Create an account using a username, email, and password.
- **User Authentication:** Login and logout with session-based authentication.
- **Password Security:** Password hashing with BCrypt.
- **Browse Blog Posts:** Retrieve and explore all available posts.
- **Post Details:** View the full content and author information of individual posts.
- **Create Posts:** Publish text-based blog posts as an authenticated user.
- **Edit Posts:** Update posts created by the current user.
- **Delete Posts:** Remove posts owned by the current user.
- **Access Control:** Prevent users from editing or deleting other users' posts.
- **Input Validation:** Validate user credentials and post content.
- **Responsive Frontend:** A clean web interface built with HTML, CSS, and JavaScript.
- **API Integration:** Asynchronous communication between the frontend and backend using Fetch API.
- **API Testing:** HTTP request tests using the IntelliJ IDEA HTTP Client.

## 🛠️ Technologies

| Technology | Purpose |
|---|---|
| Java 21 | Backend development |
| Jakarta Servlets | HTTP request handling |
| Apache Tomcat 11 | Application deployment |
| Hibernate ORM | Object-relational mapping |
| Jakarta Persistence (JPA) | Database persistence |
| PostgreSQL | Relational database |
| Maven | Build and dependency management |
| Jackson | JSON processing |
| jBCrypt | Password hashing |
| HTML5 | Web page structure |
| CSS3 | Styling and responsive design |
| JavaScript | Frontend functionality |
| Fetch API | Backend communication |
| IntelliJ IDEA | Development environment |

## 🏗️ Architecture

The application uses a layered architecture to separate responsibilities and improve maintainability.

- **Servlet Layer:** Handles HTTP requests and responses.
- **Filter Layer:** Applies authentication checks to protected endpoints.
- **Service Layer:** Contains business logic, validation, and authorization rules.
- **Repository Layer:** Performs database operations.
- **Entity Layer:** Represents persistent application data.
- **DTO Layer:** Defines request and response data structures.
- **Command Layer:** Converts entities into response DTOs.
- **Configuration Layer:** Manages persistence and application configuration.

## 📂 Project Structure

```text
blog-management-system/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/blogmanagement/
│       │       ├── command/
│       │       ├── config/
│       │       ├── dto/
│       │       │   ├── request/
│       │       │   └── response/
│       │       ├── entity/
│       │       ├── exception/
│       │       ├── filter/
│       │       ├── repository/
│       │       ├── service/
│       │       └── servlet/
│       ├── resources/
│       │   └── META-INF/
│       │       └── persistence.xml
│       └── webapp/
│           ├── css/
│           │   └── style.css
│           ├── js/
│           │   └── app.js
│           ├── pages/
│           ├── WEB-INF/
│           └── index.html
├── .idea/
│   └── httpRequests/
│       └── blog-management-system.http
├── pom.xml
└── README.md
```

## 🗄️ Database Setup

The application uses PostgreSQL for persistent data storage.

Create the database:

```sql
CREATE DATABASE blog_management_system;
```

Configure the database connection in `src/main/resources/META-INF/persistence.xml` using your local PostgreSQL settings.

Make sure your database credentials are not committed to the public repository.

## ⚙️ Prerequisites

Install the following software before running the application:

- JDK 21
- Apache Maven
- Apache Tomcat 11
- PostgreSQL
- IntelliJ IDEA or another Java IDE

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/A1Reza/blog-management-system.git
cd blog-management-system
```

### 2. Configure the Database

Create the `blog_management_system` database and update the PostgreSQL connection settings in `persistence.xml`.

### 3. Load Maven Dependencies

Open the project in IntelliJ IDEA and allow Maven to download the required dependencies.

Alternatively, run:

```bash
mvn clean install
```

### 4. Build the Application

```bash
mvn clean package
```

### 5. Configure Apache Tomcat

Configure Tomcat 11 in your IDE and deploy the generated WAR artifact.

### 6. Run the Application

Start the server and open the application using the context path configured in Tomcat.

Example:

```text
http://localhost:8080/blog_management_system_war_exploded/
```

The actual URL depends on your deployment configuration.

## 🔌 API Endpoints

The following endpoints are available under the application's context path.

### Authentication

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Authenticate a user |
| POST | `/api/auth/logout` | End the current session |

### Blog Posts

| Method | Endpoint | Description | Authentication |
|---|---|---|---|
| GET | `/api/posts` | Retrieve all posts | Not required |
| GET | `/api/posts/{id}` | Retrieve a post by ID | Not required |
| POST | `/api/posts` | Create a post | Required |
| PUT | `/api/posts/{id}` | Update an owned post | Required |
| DELETE | `/api/posts/{id}` | Delete an owned post | Required |

## 📬 Example Requests

### Register a User

```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "reza",
  "email": "reza@example.com",
  "password": "your-password"
}
```

### Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "reza",
  "password": "your-password"
}
```

### Create a Blog Post

```http
POST /api/posts
Content-Type: application/json

{
  "title": "Getting Started with Java",
  "content": "Java is a widely used programming language."
}
```

## 🧪 Testing

The API can be tested using the built-in HTTP Client in IntelliJ IDEA.

Test requests are maintained in:

```text
.idea/httpRequests/blog-management-system.http
```

The test scenarios cover:

- User registration and authentication
- Duplicate username and email validation
- Invalid login attempts
- Retrieving all posts and individual posts
- Creating, updating, and deleting posts
- Invalid input and nonexistent resources
- Requests without authentication
- Ownership-based authorization
- Session logout behavior

Run the requests in the intended order because some scenarios depend on users, posts, and sessions created by earlier requests.

## 🔐 Security

The application includes several security measures:

- BCrypt password hashing.
- Session-based authentication.
- Authentication checks for protected endpoints.
- Server-side ownership validation for post updates and deletions.
- Request validation to prevent invalid post data.
- Response DTOs that avoid exposing password fields.

Authorization is enforced on the backend rather than relying solely on frontend controls.

## 🔮 Potential Improvements

- Add pagination and post search.
- Improve form validation and user feedback.
- Add automated unit and integration tests.
- Introduce centralized exception handling.
- Improve deployment configuration and environment-specific settings.

## 👨‍💻 Author

**Reza Asadipour**

- GitHub: [@A1Reza](https://github.com/A1Reza)
- Repository: [Blog Management System](https://github.com/A1Reza/blog-management-system)

## 📄 License

No license has been specified. All rights are reserved by the author unless a license is added to this repository.
