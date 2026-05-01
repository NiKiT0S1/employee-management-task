package com.task.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    // Константа с названием файла настроек (если имя файла меняется, то меняется только эта строка)
    private static final String PROPERTIES_FILE = "db.properties";

    // static-блок выполняется один раз - загружает JDBC-драйвер
    static {
        try {
            // Явная загрузка JDBC-драйвера
            Class.forName("org.firebirdsql.jdbc.FBDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("JDBC-драйвер Firebird/Jaybird не найден", e);
        }
    }

    // Метод для подключения к БД
    public static Connection getConnection() throws SQLException {
        // Загрузка настроек db.properties в объект класса Properties
        Properties properties = loadProperties();

        // Чтение значений из db.properties
        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        // Подключение к БД
        return DriverManager.getConnection(url, username, password);
    }

    // Метод для чтения файла db.properties
    private static Properties loadProperties() {
        // Инициализация объекта, где будут храниться значения файла
        Properties properties = new Properties();

        // Вызов метода getPropertiesInputStream(), который ищет сам файл db.properties
        try (InputStream inputStream = getPropertiesInputStream()) {

            if (inputStream == null) {
                throw new RuntimeException(
                        "Файл db.properties не найден. Проверь, что он находится в src/main/resources " +
                                "и после сборки попадает в WEB-INF/classes/db.properties"
                );
            }

            // Чтение файла и загрузка в объект properties
            properties.load(inputStream);
            return properties;

        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла db.properties", e);
        }
    }

    // Метод для поиска файла db.properties
    private static InputStream getPropertiesInputStream() {
        // Поиск через classloader текущего потока HTTP-запроса
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        if (contextClassLoader != null) {
            InputStream inputStream = contextClassLoader.getResourceAsStream(PROPERTIES_FILE);

            if (inputStream != null) {
                return inputStream;
            }
        }

        // Поиск через classloader класса DatabaseConnection
        ClassLoader classLoader = DatabaseConnection.class.getClassLoader();

        if (classLoader != null) {
            InputStream inputStream = classLoader.getResourceAsStream(PROPERTIES_FILE);

            if (inputStream != null) {
                return inputStream;
            }
        }

        // Поиск напрямую через класс DatabaseConnection
        return DatabaseConnection.class.getResourceAsStream("/" + PROPERTIES_FILE);
    }
}