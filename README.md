# QuickChatApp Part 3 PoE

## Project Overview

QuickChatApp is a Java-based console messaging application developed for the Programming 1A (PROG5121) Portfolio of Evidence (PoE).

The application allows users to log in, send messages, store messages, disregard messages and manage message information. Part 3 builds on the previous parts by introducing additional message management features, arrays and JSON file storage.

## Technologies Used

- Java
- Apache Maven
- JUnit 5
- Gson
- NetBeans IDE

## Main Features

### 1. User Login

- Allows users to log in.
- Validates user credentials.
- Provides login status feedback.

### 2. Sending Messages

- Allows users to create and send messages.
- Supports message recipients and message content.
- Generates a unique message ID and message hash.
- Allows users to send, store or disregard messages.

### 3. Message Management Using Arrays

The application uses dynamically populated arrays to manage message information:

- `sentMessages` – stores all sent messages.
- `disregardedMessages` – stores messages that have been disregarded.
- `storedMessages` – stores messages saved for later.
- `messageHashes` – stores message hashes.
- `messageIDs` – stores message IDs.

### 4. Stored Messages Menu

The application includes a fourth main menu option called **Stored Messages**.

This option provides the following features:

1. Display stored messages, including sender and recipient details.
2. Identify and display the longest stored message.
3. Search for a stored message using its message ID.
4. Search for messages associated with a recipient, including sent and stored messages.
5. Delete a stored message using its message hash.
6. Display a full report of stored messages.

### 5. JSON File Storage

- Stored messages are saved in a JSON file.
- The application can load stored messages from the JSON file.
- Message information is retained for later use.

## Testing

JUnit 5 is used to test the application's functionality.

The tests cover login validation, message handling, stored message features, searching, deleting messages and displaying reports.

The latest local test run completed successfully:

- Tests run: 30
- Failures: 0
- Errors: 0
- Skipped: 0
- Build result: BUILD SUCCESS

## How to Run the Project

1. Open the project in NetBeans IDE.
2. Make sure Java and Maven are configured.
3. Build and run the project.
4. Follow the console menu instructions.

To run the automated tests using Maven:

```bash
mvn clean test
```

## GitHub Repository

The project is maintained in the `KhanbanTasks` branch of this repository.

## Conclusion

QuickChatApp Part 3 extends the messaging application with improved message management, dynamically populated arrays, JSON storage, search functionality, message deletion and reporting features.

## References

Use the references required by your assignment's referencing style. The official documentation below is relevant to the tools named in this README; it does not replace sources required by the assignment:

- Apache Maven, *Maven Getting Started Guide*: <https://maven.apache.org/guides/getting-started/>
- JUnit 5 User Guide: <https://junit.org/junit5/docs/current/user-guide/>
- Apache NetBeans: <https://netbeans.apache.org/>


