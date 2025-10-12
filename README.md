# MyTaskManager

MyTaskManager is a Spring Boot project for managing tasks with role-based authorization and basic authentication.  
Admins (managers) and regular users (employees) can securely access the system according to their roles.

---

## Features

### Role-Based Access

**Admin (Manager) can:**
- Create tasks and assign them to any user.
- View the list of all users and delete users if needed.
- View all tasks and edit or delete any task.
- Mark tasks as completed or uncompleted.

**Common User (Employee) can:**
- Create tasks only for themselves.
- View the list of all users (read-only, no actions allowed).
- View all tasks but can edit or delete only the tasks they are responsible for.
- Mark their own tasks as completed or uncompleted.

**Every authorized user can:**
- View their own profile.

---

## Tech Stack

- Java 17  
- Spring Boot 3  
- Spring Data JPA  
- MySQL  
- Maven  
- Basic Authentication  

---

## Setup Instructions

1. Clone the repository

```bash
git clone <repository-url>
cd MyTaskManager
```



2. Configure MySQL Database

Create a database, e.g., mytaskmanager.

Update application.properties:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mytaskmanager
spring.datasource.username=<your-db-username>
spring.datasource.password=<your-db-password>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

3. **Run the Application**

```bash
mvn spring-boot:run
```

The app runs at: `http://localhost:8080`

## Authentication

* **Admin:** `admin:adminpassword`
* **User:** `user1:userpassword`

Use `Authorization: Basic <base64-encoded-credentials>` for protected endpoints.

## API Endpoints

### Users

   
Endpoint 	              Method	         Auth	              Description
--------------------- | -----------------|  --------- | ------------------------ |

`/api/users`              POST	         Admin	      Create a new user
`/api/users/register`	POST	         None         Public user registration
`/api/users`              GET	             Admin	      Get all users
`/api/users/{id}`	    GET	             Admin	      Get user by ID
`/api/users/{id}`	    DELETE	         Admin	      Delete user


### Tasks

   
Endpoint 	                        Method	         Auth	              Description
---------------------         | -----------------|  --------- | ------------------------ |

`/api/tasks/{userId}`             POST           Admin        Create a task for a user     
`/api/tasks`                        GET            User         Get all tasks                
`/api/tasks/{id}`                  GET            User         Get task by ID               
`/api/tasks/{id}`                  PUT            User         Update task                  
`/api/tasks/{id}`                DELETE           User         Delete task                  
`/api/tasks/user/{userId}`       GET            User         Get tasks assigned to a user 




##Future Enhancements

JWT-based authentication for better security.

Task priorities, deadlines, and status tracking.

Email notifications for tasks and reminders.


## License

This project is licensed under the MIT License.

