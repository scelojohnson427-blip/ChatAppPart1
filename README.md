# QuickChat Part 2

**Student:** Scelo Johnson  
**Student number:** ST10486748  
**Package:** `com.mycompany.chatapppart1`  
**Git branch:** `KhanbanTasks`

## Project overview

QuickChat is a Java console application for registering and logging in users and working with messages. This Part 2 project is organized as a Maven project for use in Apache NetBeans. The implementation is in the `com.mycompany.chatapppart1` package.

## Features

- User registration and login through `Login.java`.
- Message creation and message ID and hash handling through `Message.java`.
- Recipient validation and message-length validation.
- Sending, disregarding, and storing messages.
- A console entry point and application menu in `Main.java`.
- JUnit test classes: `LoginTest.java` and `MessageTest.java`.
- A GitHub Actions workflow at `.github/workflows/maven.yml` for automated Maven checks.

## Project files

```text
ChatAppPart1/
├── .github/workflows/maven.yml
├── src/main/java/com/mycompany/chatapppart1/
│   ├── Main.java
│   ├── Login.java
│   └── Message.java
├── src/test/java/com/mycompany/chatapppart1/
│   ├── LoginTest.java
│   └── MessageTest.java
└── pom.xml
```

## Requirements

- Apache NetBeans with Maven support.
- A JDK version supported by the project configuration in `pom.xml` and the GitHub Actions workflow. Check both settings before choosing a JDK; the exact required version should match those files.
- Git, if you want to commit and push changes from your computer.

## Run the application in NetBeans

1. Clone or download the `ChatAppPart1` repository and check out the `KhanbanTasks` branch.
2. In NetBeans, choose **File → Open Project** and select the folder containing `pom.xml`.
3. Wait for Maven to load the project and dependencies.
4. Open `Main.java` and run the project using **Run Project** (or right-click the project and choose **Run**).
5. Follow the prompts shown in the console.

The exact prompts depend on the current `Main.java` implementation. Confirm that its startup flow and menu labels match the instructions for your assignment.

## Run the tests

In NetBeans, right-click the project and choose **Test**. Alternatively, open a terminal in the project folder and run:

```bash
mvn test
```

The test classes are `LoginTest.java` and `MessageTest.java`. The test command reports whether they pass in the current checkout; this README does not imply a particular test result.

## GitHub Actions

The workflow in `.github/workflows/maven.yml` runs Maven checks when its configured trigger occurs. Open the repository's **Actions** tab to see workflow runs and their results. Confirm the trigger, JDK version, and Maven goals in the workflow file before submission.

## Example menu flow

The following is an illustrative flow, not a guarantee of the exact prompts or option numbers in the current source:

```text
Register or log in
Open the QuickChat menu
Choose a message action: send, disregard, or store
Exit
```

Update this example to show the exact menu text and options from `Main.java` if your assignment requires a sample run.

## Assignment checks to confirm

Before submission, compare the source and output with the Part 2 assignment rubric. In particular, confirm:

- Which Java version and Maven commands are required by `pom.xml` and the workflow.
- Whether the user specifies the number of messages at startup, and whether the application prevents processing more than that number after returning to the menu.
- The exact menu options and any required message-count, message-ID, hash, recipient, or length rules.
- Whether stored messages are persisted between application runs or only kept during one run.
- The required GitHub Actions triggers and checks, and that the workflow succeeds on `KhanbanTasks`.

## References

Use the references required by your assignment's referencing style. The official documentation below is relevant to the tools named in this README; it does not replace sources required by the assignment:

- Apache Maven, *Maven Getting Started Guide*: <https://maven.apache.org/guides/getting-started/>
- JUnit 5 User Guide: <https://junit.org/junit5/docs/current/user-guide/>
- Apache NetBeans: <https://netbeans.apache.org/>

Add any course notes, prescribed readings, or other sources you actually used, in the format required by your institution. Do not list a source you did not consult.
