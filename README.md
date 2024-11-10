# Learning-Management-System
The Learning Management System (LMS) is a backend web application which provides a solution of making education more accessible through online learning by designing and building a comprehensive platform that simplifies and enhances educational processes for institutions, instructors, and students. This system will focus on core aspects such as course and instructor management, student enrollment, grading, and reporting on student progress.

## Features
- User management (admin, students and instructors)
- Course and Instructor Management
- Student Enrollment and Grading
- Reporting on Student Progress
- Logging for Actions and Error Handling
- Unit and Integration Tests using JUnit
  
## Clone the repository
```shell
https://github.com/daisy-amrita/Learning-Management-System.git
```
## Run in your System
1. **Open Project in Eclipse**
2. **Create the Database**:
   - Open MySQL and create a new database:
   ```sql
   CREATE DATABASE yourdatabasename;
3. **Configure the Database**:
   - Open the `application.properties` file located at `src/main/resources/application.properties`.
   - Update the file with your database credentials by modifying the following properties:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/yourdatabasename
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
4. **Build the Project**:
   - Use Maven to clean and build the project. Open your terminal and run the following command:
   ```bash
   mvn clean install
5. **Run the Project**:
   - Right-click on the main class: `Run As > Spring Boot App`


## Technologies Used
- **Spring Boot**: For developing the back-end services and business logic.
- **Eclipse IDE**: Integrated Development Environment used for developing and running the project.
- **Maven**: Dependency management and build automation tool.
- **MySQL**: For Database.
- **JUnit and mockito**: For unit and integration testing.
- **Postman**: For Manual API Testing.
- **SLF4J/Logback**: For Logging and Monitoring.

## Database
The LMS uses a relational database to store and manage data related to users (students and instructors), courses, enrollments, grades and assignments. The schema includes key tables such as:
The project uses a relational database with the following tables:
- `User`:  Represents both students and teachers.
- `Course`: Stores course information.
- `Enrollment`: Tracks which students are enrolled in which courses.
- `Assignment`: Stores information about assignments within courses and manage it.
- `Grade`: Stores student grades for assignments.

### Prerequisites
To run this project, you need:
- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Maven](https://maven.apache.org/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Eclipse IDE](https://www.eclipse.org/downloads/)

