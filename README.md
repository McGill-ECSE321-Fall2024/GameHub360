# Welcome to team 9's Game Shop Management System! 🎮

The goal of this project is to create a software system that could be used by customers, employees, and the manager to service an Online Game Shop.
Our Game Shop Management System offers a seamless way to manage inventory, customer orders, and reviews for an independent game shop 🕹️

## Table of Contents

1. [Our Team](#our-team)
2. [Getting Started](#getting-started)
3. [Deliverable 2](#deliverable-2)
4. [Deliverable 1](#deliverable-1)

## Our Team

#### Ahmed Mossa

I am a third-year Software Engineering student with expertise across various areas of software engineering. I thrive in collaborative environments, such as this project, where I can sharpen both my technical and interpersonal skills through diverse roles 👨🏻‍💻

#### Amgad Elgamal

I am a third-year Software Engineering student with a keen interest in software development and problem-solving. I enjoy working on projects that challenge me to think critically and collaborate with others to build efficient, impactful solutions.

#### Hamza Alfarrash

I am a third-year Computer Engineering student with a strong passion for software development. I enjoy tackling complex problems and continuously seek opportunities to enhance my technical skills while collaborating with others in dynamic environments like this project 👨🏻‍💻💻

#### Ismail Sentissi

I am a third-year Software Engineering student with a strong passion for software development and machine learning. I thrive on solving intricate challenges and constantly look for ways to sharpen my technical expertise.

#### Nicolas Bolouri

I am a U3 Software Engineering student with a strong passion for web development, artificial intelligence, and robotics. I have experience in developing scalable applications and a keen interest in exploring innovative solutions at the intersection of AI and engineering.

#### Mohamed-Amine Benzaid

I am a third-year Computer Engineering student passionate about software design and development. I love exploring new technologies and working in team environments to create innovative solutions that drive progress in the field.

## Getting Started

### Backend

Navigate to the backend directory:

```bash
cd GameShop-Backend
```

#### Setting Up the Database Locally

1. Create a new PostgreSQL database named `game_shop`:
   ```sql
   CREATE DATABASE game_shop;
   ```
2. Ensure the following properties are set in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/game_shop
   spring.datasource.username=postgres
   spring.datasource.password=321Group9
   spring.jpa.hibernate.ddl-auto=update
   ```

#### Building

Build the project using Gradle:

```bash
./gradlew build
```

#### Running

Start the Spring Boot application:

```bash
./gradlew bootRun
```

#### Testing

1. Ensure the database is correctly set up and accessible.
2. Run tests:
   ```bash
   ./gradlew test
   ```

#### Integration Testing

1. Ensure the database is correctly set up and accessible.
2. Run integration tests:
   ```bash
   ./gradlew integrationTest
   ```

## Deliverable 2

### Links
- [Wiki Home Page](https://github.com/McGill-ECSE321-Fall2024/project-group-9/wiki)
- [Project Report D2](https://github.com/McGill-ECSE321-Fall2024/project-group-9/wiki/Project-Report-D2)
- [API Documentation](https://github.com/McGill-ECSE321-Fall2024/project-group-9/wiki/API-Documentation)
- [QA Plan](https://github.com/McGill-ECSE321-Fall2024/project-group-9/wiki/QA-Plan)
- [QA Report](https://github.com/McGill-ECSE321-Fall2024/project-group-9/wiki/QA-Report)

### Effort Table (D2)
| Team Member           | Time Spent (h) | Responsibilities                                                                                                                         |
|-----------------------|----------------|---------------------------------------------------------------------------------------------------------------------------------------|
| Ismail Sentissi       | 27             | API design for Categories, Project Management and Report, Integration tests, cleaned up backlog, QA Plan                             |
| Ahmed Mossa           | 37             | API implementation for Employees, created QA report, unit & integration tests, assisted colleagues with debugging                     |
| Nicolas Bolouri       | 35             | API design for Manager and Store, project minutes organizer and scribe, GitHub setup, created API documentation                       |
| Hamza Alfarrash       | 23             | API implementation for Reviews and Orders, wiki documentation, integration tests                                                      |
| Amgad Elgamal         | 32             | API implementation for Games, cleaned up backlog, domain model rationale, testing                                                     |
| Mohamed-Amine Benzaid | 31             | API implementation for Customers, QA plan, unit tests, integration tests                                                              |

## Deliverable 1

### Links

- [Wiki Home Page](https://github.com/McGill-ECSE321-Fall2024/project-group-9/wiki)
- [Requirements](https://github.com/McGill-ECSE321-Fall2024/project-group-9/wiki/Requirements)
- [Use Case Diagrams and Specifications](https://github.com/McGill-ECSE321-Fall2024/project-group-9/wiki/Use-Cases-and-Specifications)
- [Domain Model & Rationale](https://github.com/McGill-ECSE321-Fall2024/project-group-9/wiki/Domain-Model-and-Rationale)
- [Project Report](https://github.com/McGill-ECSE321-Fall2024/project-group-9/wiki/Project-Report)

### Effort Table (D1)

| Team Member           | Time Spent (h) | Responsibilities                                                                                                                       |
| --------------------- | -------------- | -------------------------------------------------------------------------------------------------------------------------------------- |
| Ismail Sentissi       | 17             | Requirements, testing, use-case scenarios & report                                                                                     |
| Ahmed Mossa           | 18             | Writing Umple code for the domain model and generating/editing Java code, Spring Boot project setup, requirements & use-case scenarios |
| Nicolas Bolouri       | 22             | Minutes organizer and scribe, GitHub setup initialization & Creating/editing domain model                                              |
| Hamza Alfarrash       | 18             | Use-case diagram 1, requirements, wiki documentation & testing                                                                         |
| Amgad Elgamal         | 18             | Domain Model Rationale, testing, requirements & backlog                                                                                |
| Mohamed-Amine Benzaid | 18             | Use-case diagram 1, testing, use-case scenarios & requirements                                                                         |
