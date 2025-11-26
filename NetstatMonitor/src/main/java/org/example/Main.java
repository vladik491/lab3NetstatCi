package org.example;

import org.example.config.AppConfig;
import org.example.model.ConnectionRecord;
import org.example.service.NetstatParser;
import java.util.List;

/**
 * главный класс приложения и точка входа в программу
 */
public class Main {
    /**
     * метод запуска приложения,
     * он считывает конфигурацию, получает данные от netstat и выводит отчет в консоль
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        try {
            // чтение конфигурации
            AppConfig config = new AppConfig("app.properties");
            // получение данных
            NetstatParser parser = new NetstatParser();
            List<ConnectionRecord> records = parser.getConnections();

            System.out.println("Mode: " + config.getOutputMode());

            // ЗАД-1: слушающие порты
            System.out.println("\nListening Ports:");
            for (ConnectionRecord rec : records) {
                if (rec.getState().equalsIgnoreCase("LISTEN") || rec.getState().equalsIgnoreCase("LISTENING")) {
                    System.out.println(rec);
                }
            }

            // ЗАД-2: внешние соединения
            System.out.println("\nExternal Connections:");
            for (ConnectionRecord rec : records) {
                if (rec.getState().equalsIgnoreCase("ESTABLISHED")) {
                    System.out.println("Foreign: " + rec.getForeignAddress() + " -> " + rec.getPidAndProgram());
                }
            }

            // ЗАД-3: проверка порта
            System.out.println("\nCheck Port " + config.getPortToCheck() + ":");
            boolean found = records.stream().anyMatch(r -> r.getLocalAddress().endsWith(":" + config.getPortToCheck()));
            System.out.println(found ? "Port is BUSY." : "Port is FREE.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}