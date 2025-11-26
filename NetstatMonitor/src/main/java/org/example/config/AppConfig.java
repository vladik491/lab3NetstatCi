package org.example.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * класс конфигурации приложения,
 * он отвечает за чтение настроек из файла app.properties
 */
public class AppConfig {
    private final int portToCheck;
    private final String outputMode;

    /**
     * загружает настройки из указанного файла свойств,
     * если параметр отсутствует в файле, используется значение по умолчанию
     *
     * @param configPath путь к файлу конфигурации (например, app.properties)
     * @throws IOException если файл не найден или произошла ошибка чтения
     */
    public AppConfig(String configPath) throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(configPath)) {
            props.load(fis);
        }
        // читаем порт, по умолчанию 8080
        this.portToCheck = Integer.parseInt(props.getProperty("monitor.port.check", "8080"));
        // читаем режим вывода, по умолчанию VERBOSE
        this.outputMode = props.getProperty("output.mode", "VERBOSE");
    }

    /**
     * получить порт, который необходимо проверить на занятость
     * @return номер порта
     */
    public int getPortToCheck() { return portToCheck; }

    /**
     * получить режим вывода информации (VERBOSE или SUMMARY)
     * @return строковое представление режима
     */
    public String getOutputMode() { return outputMode; }
}