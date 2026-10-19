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
- JUnit test classes: `MainTest.java`, `LoginTest.java`, and `MessageTest.java`.
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
│   ├── MainTest.java
│   ├── LoginTest.java
│   └── MessageTest.java
└── pom.xml
```

## Screenshots
<img width="996" height="450" alt="BACB7AD5-EEF1-4793-8549-2E0263F548C0" src="https://github.com/user-attachments/assets/1fd52a80-7720-49de-9d9f-7bfe2678f1b6" />  


<img width="922" height="452" alt="601832BB-404D-497F-ACA9-0D33B0994EE9" src="https://github.com/user-attachments/assets/70008100-9d23-446c-b6b6-cf155cbe15ec" /> 


<img width="964" height="298" alt="8B72FDE6-18E8-48BA-9C0E-BC954AFB9E09_4_5005_c" src="https://github.com/user-attachments/assets/4f6bd661-353d-483d-bcbb-53080df6e1f0" /> 


<img width="700" height="334" alt="6A264371-EDFC-4FA1-8C8E-62A4C87F9F74_4_5005_c" src="https://github.com/user-attachments/assets/44e0d694-b86c-4e1e-9aff-acb91caa330d" /> 


<img width="684" height="420" alt="C6030762-C8F5-49F1-909B-C8C66BA0C527" src="https://github.com/user-attachments/assets/dd9801d1-ed2f-49b6-802e-832844e09e10" /> 


<img width="1480" height="520" alt="3B7E2672-5576-4362-AF06-DCD23557A9D9" src="https://github.com/user-attachments/assets/e446933b-83ef-4663-a91c-3096f9232275" /> 




## Requirements

- Apache NetBeans with Maven support.
- JDK 17 (the Maven compiler configuration and GitHub Actions workflow both use Java 17).
- Git, if you want to commit and push changes from your computer.

## Run the application in NetBeans

1. Clone or download the `ChatAppPart1` repository and check out the `KhanbanTasks` branch.
2. In NetBeans, choose **File → Open Project** and select the folder containing `pom.xml`.
3. Wait for Maven to load the project and dependencies.
4. Open `Main.java` and run the project using **Run Project** (or right-click the project and choose **Run**).
5. Follow the prompts shown in the console.

After registration and login, the application displays a menu to send messages, show recently sent messages, or quit. For each message it asks for a recipient, message text, and whether to send, disregard, or store the message.

## Run the tests

In NetBeans, right-click the project and choose **Test**. Alternatively, open a terminal in the project folder and run:

```bash
mvn test
```

The test classes are `MainTest.java`, `LoginTest.java`, and `MessageTest.java`. Together they cover menu behavior, the message limit, message handling, and registration and login checks.

## GitHub Actions

The workflow in `.github/workflows/maven.yml` runs Maven checks when its configured trigger occurs. Open the repository's **Actions** tab to see workflow runs and their results. Confirm the trigger, JDK version, and Maven goals in the workflow file before submission.

## Example menu flow

The application menu is:

```text
1. Send Messages
2. Show recently sent messages
3. Quit
```

Each message has its own action menu: send, disregard, or store. The application asks once for the message limit and does not accept additional messages after that limit is reached.

## Assignment checks to confirm

Before submission, compare the source and output with the Part 2 assignment rubric. In particular, confirm:

- The required message ID and hash algorithms match the assignment specification.
- The `stored_messages.json` file is included only if your submission instructions require a sample data file; the application creates or updates it when a message is stored.
- The GitHub Actions workflow succeeds on `KhanbanTasks`.

## References

Use the references required by your assignment's referencing style. The official documentation below is relevant to the tools named in this README; it does not replace sources required by the assignment:

- Apache Maven, *Maven Getting Started Guide*: <https://maven.apache.org/guides/getting-started/>
- JUnit 5 User Guide: <https://junit.org/junit5/docs/current/user-guide/>
- Apache NetBeans: <https://netbeans.apache.org/>


