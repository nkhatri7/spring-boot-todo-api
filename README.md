# Task Manager API

## Purpose
The purpose of this repository is to practice the basics of a Spring Boot application. This is my first Spring Boot API, so I want to get the project structure correct and understand the best practices for Spring Boot applications.

## Setup
The Spring Boot app itself doesn't need any setup, however you will need to create a PostgreSQL database called `spring_todo` (you can call it whatever you want, but you will need to change `spring.datasource.url` in `application.properties` to the new database name).

You will also have to create another properties file called `secrets.properties` in the same directory as `application.properties` in the `resources` folder and in that file you should have a variable called `jwt.secret-key` which should have the value of a generated secret key (I used [this website](https://generate-random.org/encryption-key-generator?count=1&bytes=512&cipher=aes-256-cbc&string=&password=) to generate my secret key).