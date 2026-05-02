package com.task.dao;

import com.task.model.Position;
import com.task.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PositionDao {

    // Метод для поиска всех должностей
    public List<Position> findAll() throws SQLException {
        String sql = """
                SELECT
                    id,
                    position_name,
                    position_salary
                FROM positions
                ORDER BY id
                """;

        // Создание пустого списка
        List<Position> positions = new ArrayList<>();

        // Подключение к БД
        try (Connection connection = DatabaseConnection.getConnection();
             // Сохраняем sql-запрос
             PreparedStatement statement = connection.prepareStatement(sql);
             // Выполняем String sql в БД
             ResultSet resultSet = statement.executeQuery()) {

            // Пока есть строки в результате - каждую строку переводим в Position
            while (resultSet.next()) {
                Position position = mapRow(resultSet);
                positions.add(position);
            }
        }

        // Возвращаем список должностей
        return positions;
    }

    // Метод для поиска должности по id
    public Position findById(int id) throws SQLException {
        String sql = """
                SELECT
                    id,
                    position_name,
                    position_salary
                FROM positions
                WHERE id = ?
                """;

        // Подключение к БД
        try (Connection connection = DatabaseConnection.getConnection();
             // Сохраняем sql-запрос
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Подставляем значение под ?
            statement.setInt(1, id);

            // Выполняем String sql в БД
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Возвращаем данные должности, если она найдена
                    return mapRow(resultSet);
                }
            }
        }

        // Если должность не найдена - ничего не возвращаем
        return null;
    }

    // Метод для добавления новой должности
    public void save(Position position) throws SQLException {
        String sql = """
                INSERT INTO positions (
                    position_name,
                    position_salary
                )
                VALUES (?, ?)
                """;

        // Подключение к БД
        try (Connection connection = DatabaseConnection.getConnection();
             // Сохраняем sql-запрос
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Подставляем значения под ?
            statement.setString(1, position.getPositionName());
            statement.setBigDecimal(2, position.getPositionSalary());

            // Вносим новую строку в таблицу positions
            statement.executeUpdate();
        }
    }

    // Метод для обновления данных должности
    public void update(Position position) throws SQLException {
        String sql = """
                UPDATE positions
                SET
                    position_name = ?,
                    position_salary = ?
                WHERE id = ?
                """;

        // Подключение к БД
        try (Connection connection = DatabaseConnection.getConnection();
             // Сохраняем sql-запрос
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Подставляем данные под ?
            statement.setString(1, position.getPositionName());
            statement.setBigDecimal(2, position.getPositionSalary());
            statement.setInt(3, position.getId());

            // Изменяем данные существующей строки в positions
            statement.executeUpdate();
        }
    }

    // Метод для удаления должности
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM positions WHERE id = ?";

        // Подключение к БД
        try (Connection connection = DatabaseConnection.getConnection();
             // Сохраняем sql-запрос
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Подставляем id под ?
            statement.setInt(1, id);

            // Удаляем должность под соответствующим id
            statement.executeUpdate();
        }
    }

    // Метод для конвертации строк БД (ResultSet) в объект Position
    private Position mapRow(ResultSet resultSet) throws SQLException {
        // Создаем пустой объект
        Position position = new Position();

        // Записываем поля объекта значениями из строки ResultSet
        position.setId(resultSet.getInt("id"));
        position.setPositionName(resultSet.getString("position_name"));
        position.setPositionSalary(resultSet.getBigDecimal("position_salary"));

        // Возвращаем заполненный объект Position - данные должности
        return position;
    }
}
