package com.mycompany.chatapppart1;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    @Test
    void menuHonorsMessageLimitAndShowsSentMessages() {
        String marker = "Menu test message 2026";
        String input = String.join("\n",
                "1",                 // Send Messages
                "1",                 // One-message limit
                "+27718693002",      // Recipient
                marker,              // Message text
                "1",                 // Send
                "1",                 // Try to send again
                "2",                 // Show sent messages
                "3") + "\n";        // Quit

        ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;
        try (Scanner scanner = new Scanner(input)) {
            System.setOut(new PrintStream(capturedOutput));
            Main.runMenu(scanner);
        } finally {
            System.setOut(originalOutput);
        }

        String output = capturedOutput.toString();
        assertEquals(1, countOccurrences(output, "Preparing message 1"));
        assertTrue(output.contains("You have completed your message limit."));
        assertTrue(output.contains("Recently sent messages:"));
        assertTrue(output.contains(marker));
        assertTrue(output.contains("Thank you for using QuickChat. Goodbye!"));
    }

    private int countOccurrences(String text, String value) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(value, index)) >= 0) {
            count++;
            index += value.length();
        }
        return count;
    }
}
