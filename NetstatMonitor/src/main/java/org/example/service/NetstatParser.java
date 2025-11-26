package org.example.service;

import org.example.model.ConnectionRecord;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * сервис для взаимодействия с системной утилитой netstat,
 * он твечает за запуск процесса и разбор (парсинг) текстового вывода
 */
public class NetstatParser {

    /**
     * выполняет команду netstat и возвращает список объектов соединений
     *
     * @return список найденных соединений
     * @throws IOException если произошла ошибка ввода-вывода при запуске процесса
     */
    public List<ConnectionRecord> getConnections() throws IOException {
        return parseOutput(runCommand());
    }

    /**
     * определяет ОС и запускает соответствующую команду netstat (-ano для Windows, -anp для Linux)
     *
     * @return список строк, полученных из stdout процесса
     * @throws IOException ошибка запуска процесса
     */
    private List<String> runCommand() throws IOException {
        List<String> output = new ArrayList<>();
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

        ProcessBuilder builder = isWindows ?
                new ProcessBuilder("netstat", "-ano") :
                new ProcessBuilder("netstat", "-anp");

        builder.redirectErrorStream(true);
        Process process = builder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }
        }
        return output;
    }

    /**
     * разбирает сырые строки вывода netstat в объекты модели
     *
     * @param lines список строк из консольного вывода
     * @return список объектов ConnectionRecord
     */
    public List<ConnectionRecord> parseOutput(List<String> lines) {
        List<ConnectionRecord> records = new ArrayList<>();
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("Active") || line.startsWith("Proto")) continue;

            String[] parts = line.split("\\s+");

            if (isWindows) {
                if (parts.length >= 5 && parts[0].equalsIgnoreCase("TCP")) {
                    records.add(new ConnectionRecord(parts[0], parts[1], parts[2], parts[3], parts[4]));
                }
            } else {
                if (parts.length >= 7 && parts[0].toLowerCase().startsWith("tcp")) {
                    records.add(new ConnectionRecord(parts[0], parts[3], parts[4], parts[5], parts[6]));
                }
            }
        }
        return records;
    }
}