package org.example;

import org.example.model.ConnectionRecord;
import org.example.service.NetstatParser;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NetstatParserTest {

    @Test
    void testParse() {
        NetstatParser parser = new NetstatParser();
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

        System.out.println("Running test on OS: " + System.getProperty("os.name"));

        String inputLine;

        // Формируем строку жестко, чтобы избежать проблем с копированием пробелов
        if (isWindows) {
            // Имитация вывода Windows: TCP [Local] [Foreign] [State] [PID]
            inputLine = "TCP 127.0.0.1:8080 0.0.0.0:0 LISTENING 1234";
        } else {
            // Имитация вывода Linux: tcp [recv] [send] [Local] [Foreign] [State] [PID/Name]
            inputLine = "tcp 0 0 127.0.0.1:8080 0.0.0.0:* LISTEN 1234/java";
        }

        System.out.println("Testing input line: " + inputLine);

        // Парсим
        List<ConnectionRecord> result = parser.parseOutput(Collections.singletonList(inputLine));

        // Выводим результат, если он есть
        if (result.isEmpty()) {
            System.out.println("Result list is EMPTY! Parser logic failed.");
        } else {
            System.out.println("Parsed record: " + result.get(0).toString());
        }

        // Проверки (Assertions)
        // 1. Список не должен быть пустым
        assertFalse(result.isEmpty(), "Parser returned empty list for valid input string");

        ConnectionRecord rec = result.get(0);

        // 2. Проверяем адрес
        assertTrue(rec.getLocalAddress().contains("8080"), "Port check failed");

        // 3. Проверяем PID (или программу)
        assertTrue(rec.getPidAndProgram().contains("1234"), "PID check failed");
    }
}