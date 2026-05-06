package com.task.dao;

import com.task.model.Department;
import com.task.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDao {

    // Метод для поиска всех отделов
    public List<Department> findAll() throws SQLException {
        String sql = """
                SELECT
                    d.id,
                    d.department_name,
                    d.department_phone,
                    d.department_email,
                    MAX(CASE WHEN e.is_department_head = 1 THEN e.full_name ELSE NULL END) AS head_name,
                    COUNT(e.id) AS employees_count
                FROM departments d
                LEFT JOIN employees e ON e.department_id = d.id
                GROUP BY
                    d.id,
                    d.department_name,
                    d.department_phone,
                    d.department_email
                ORDER BY d.id
                """;

        // Создание пустого списка
        List<Department> departments = new ArrayList<>();

        // Подключение к БД
        try (Connection connection = DatabaseConnection.getConnection();
             // Сохраняем sql-запрос
             PreparedStatement statement = connection.prepareStatement(sql);
             // Выполняем String sql в БД
             ResultSet resultSet = statement.executeQuery()) {

            // Пока есть строки в результате - каждую строку переводим в Department
            while (resultSet.next()) {
                Department department = mapRow(resultSet);
                departments.add(department);
            }
        }

        // Возвращаем список отделов
        return departments;
    }

    // Метод для поиска отдела по id
    public Department findById(int id) throws SQLException {
        String sql = """
                SELECT
                    d.id,
                    d.department_name,
                    d.department_phone,
                    d.department_email,
                    MAX(CASE WHEN e.is_department_head = 1 THEN e.full_name ELSE NULL END) AS head_name,
                    COUNT(e.id) AS employees_count
                FROM departments d
                LEFT JOIN employees e ON e.department_id = d.id
                WHERE d.id = ?
                GROUP BY
                    d.id,
                    d.department_name,
                    d.department_phone,
                    d.department_email
                """;

        // Подключение к БД
        try (Connection connection = DatabaseConnection.getConnection();
             // Сохраняем sql-запрос
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Подставляем значение под ?, начиная с 1 (для безопасности и избежания ошибок)
            statement.setInt(1, id);

            // Выполняем String sql в БД
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Возвращаем данные отдела, если он найден
                    return mapRow(resultSet);
                }
            }
        }

        // Если отдел не найден - ничего не возвращаем
        return null;
    }

    // Метод для добавления нового отдела
    public void save(Department department) throws SQLException {
        String sql = """
                INSERT INTO departments (
                    department_name,
                    department_phone,
                    department_email,
                )
                VALUES (?, ?, ?)
                """;

        // Подключение к БД
        try (Connection connection = DatabaseConnection.getConnection();
             // Сохраняем sql-запрос
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Подставляем значения под ?
            statement.setString(1, department.getDepartmentName());
            statement.setString(2, department.getDepartmentPhone());
            statement.setString(3, department.getDepartmentEmail());

            // Вносим новую строку в таблицу departments
            statement.executeUpdate();
        }
    }

    // Метод для обновления данных отдела
    public void update(Department department) throws SQLException {
        String sql = """
                UPDATE departments
                SET
                    department_name = ?,
                    department_phone = ?,
                    department_email = ?,
                WHERE id = ?
                """;

        // Подключение к БД
        try (Connection connection = DatabaseConnection.getConnection();
             // Сохраняем sql-запрос
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Подставляем данные под ?
            statement.setString(1, department.getDepartmentName());
            statement.setString(2, department.getDepartmentPhone());
            statement.setString(3, department.getDepartmentEmail());

            // Подставляем id отдела, который нужно обновить
            statement.setInt(4, department.getId());

            // Изменяем данные существующей строки в departments
            statement.executeUpdate();
        }
    }

    // Метод для удаления отдела
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM departments WHERE id = ?";

        // Подключение к БД
        try (Connection connection = DatabaseConnection.getConnection();
             // Сохраняем sql-запрос
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Подставляем id под ?
            statement.setInt(1, id);

            // Удаляем отдел под соответствующим id
            statement.executeUpdate();
        }
    }

    // Метод для конвертации строк БД (ResultSet) в объект Department
    private Department mapRow(ResultSet resultSet) throws SQLException {
        // Создаем пустой объект
        Department department = new Department();

        // Записываем поля объекта значениями из строки ResultSet
        department.setId(resultSet.getInt("id"));
        department.setDepartmentName(resultSet.getString("department_name"));
        department.setDepartmentPhone(resultSet.getString("department_phone"));
        department.setDepartmentEmail(resultSet.getString("department_email"));

        department.setHeadName(resultSet.getString("head_name"));
        department.setEmployeesCount(resultSet.getInt("employees_count"));

        // Возвращаем заполненный объект Department - данные отдела
        return department;
    }
}
