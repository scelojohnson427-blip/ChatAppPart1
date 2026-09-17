# Chat App - Part 1

## Student Information

**Student Name:** Scelo Johnson  
**Student Number:** ST10486748  
**Project:** Chat App Part 1 - Registration and Login

---

## Project Description

This project is a console-based Java application developed for Part 1 of the Chat App PoE.

The application allows a user to register an account and then log in using their registered username and password.

The project demonstrates basic Java programming and object-oriented programming concepts.

---

## Features

The application includes:

- User registration
- Username validation
- Password complexity validation
- South African cellphone number validation
- User login
- Successful login message
- Failed login message
- JUnit unit testing
- Maven build management
- GitHub version control
- GitHub Actions automated testing

---

## Username Requirements

The username must:

- Contain an underscore (_)
- Be no more than five characters long

Example of a valid username:

`kyl_1`

Example of an invalid username:

`kyle!!!!!!!`

---

## Password Requirements

The password must:

- Contain at least eight characters
- Contain a capital letter
- Contain a number
- Contain a special character

Example of a valid password:

`Ch&&sec@ke99`

Example of an invalid password:

`password`

---

## Cellphone Requirements

The cellphone number must contain the South African international country code.

Example:

`+27838968976`

The application uses a regular expression to validate the cellphone number.

---

## Testing

JUnit 5 is used to test the Login class.

The tests check:

- Correct username
- Incorrect username
- Correct password
- Incorrect password
- Correct cellphone number
- Incorrect cellphone number
- Successful login
- Failed login
- Successful login message
- Failed login message

The tests can be run using Maven:

```bash
mvn test
