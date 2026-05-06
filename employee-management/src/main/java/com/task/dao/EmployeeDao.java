package com.task.dao;

import com.task.model.Employee;
import com.task.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {

    // Метод для поиска всех сотрудников
    public List<Employee> findAll() throws SQLException {
        String sql = """
                SELECT
                    e.id,
                    e.full_name,
                    e.department_id,
                    e.position_id,
                    e.is_department_head,
                    d.department_name,
                    p.position_name,
                    p.position_salary
                FROM employees e
                JOIN departments d ON e.department_id = d.id
                JOIN positions p ON e.position_id = p.id
                ORDER BY e.id
                """;
        // Создание пустого списка
        List<Employee> employees = new ArrayList<>();

        // Подключение к БД
        try (Connection connection = DatabaseConnection.getConnection();
             // Сохраняем sql-запрос
             PreparedStatement statement = connection.prepareStatement(sql);
             // Выполняем String sql в БД
             ResultSet resultSet = statement.executeQuery()) {

            // Пока есть строки в результате - каждую строку переводим в Employee
            while (resultSet.next()) {
                Employee employee = mapRow(resultSet);
                employees.add(employee);
            }
        }

        // Возвращаем список сотрудников
        return employees;
    }

    // Метод для поиска сотрудника по id
    public Employee findById(int id) throws SQLException {
        String sql = """
                SELECT
                    e.id,
                    e.full_name,
                    e.department_id,
                    e.position_id,
                    e.is_department_head,
                    d.department_name,
                    p.position_name,
                    p.position_salary
                FROM employees e
                JOIN departments d ON e.department_id = d.id
                JOIN positions p ON e.position_id = p.id
                WHERE e.id = ?
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
                    // Возвращаем данные сотрудника, если он найден
                    return mapRow(resultSet);
                }
            }
        }

        // Если сотрудник не найден - ничего не возвращаем
        return null;
    }

    // Метод для добавления нового сотрудника
    public void save(Employee employee) throws SQLException {
        String sql = """
                INSERT INTO employees (
                    full_name,
                    department_id,
                    position_id,
                    is_department_head
                )
                VALUES (?, ?, ?, ?)
                """;

        // Подключение к БД
        try (Connection connection = DatabaseConnection.getConnection();
             // Сохраняем sql-запрос
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Подставляем значения под ?
            statement.setString(1, employee.getFullName());
            statement.setInt(2, employee.getDepartmentId());
            statement.setInt(3, employee.getPositionId());
            statement.setInt(4, employee.isHeadOfDepartment() ? 1 : 0);

            // Вносим новую строку в таблицу employees
            statement.executeUpdate();
        }
    }

    // Метод для обновления данных сотрудника
    public void update(Employee employee) throws SQLException {
        String sql = """
                UPDATE employees
                SET
                    full_name = ?,
                    department_id = ?,
                    position_id = ?,
                    is_department_head = ?
                WHERE id = ?
                """;

        // Подключение к БД
        try (Connection connection = DatabaseConnection.getConnection();
             // Сохраняем sql-запрос
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Подставляем данные под ?
            statement.setString(1, employee.getFullName());
            statement.setInt(2, employee.getDepartmentId());
            statement.setInt(3, employee.getPositionId());
            statement.setInt(4, employee.isHeadOfDepartment() ? 1 : 0);
            statement.setInt(5, employee.getId());

            // Изменяем данные существующей строки в employees
            statement.executeUpdate();
        }
    }

    // Метод для удаления сотрудника
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";

        // Подключение к БД
        try (Connection connection = DatabaseConnection.getConnection();
             // Сохраняем sql-запрос
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Подставляем id под ?
            statement.setInt(1, id);

            // Удаляем сотрудника под соответствующим id
            statement.executeUpdate();
        }
    }

    // Метод для конвертации строк БД (ResultSet) в объект Employee
    private Employee mapRow(ResultSet resultSet) throws SQLException {
        // Создаем пустой объект
        Employee employee = new Employee();

        // Записываем поля объекта значениями из строки ResultSet
        employee.setId(resultSet.getInt("id"));
        employee.setFullName(resultSet.getString("full_name"));
        employee.setDepartmentId(resultSet.getInt("department_id"));
        employee.setPositionId(resultSet.getInt("position_id"));

        employee.setDepartmentName(resultSet.getString("department_name"));
        employee.setPositionName(resultSet.getString("position_name"));
        employee.setPositionSalary(resultSet.getBigDecimal("position_salary"));
        employee.setHeadOfDepartment(resultSet.getInt("is_department_head") == 1);

        // Возвращаем заполненный объект Employee - данные сотрудника
        return employee;
    }

    // Метод для подсчета количества сотрудников отдела
    public int countByDepartmentId(int departmentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM employees WHERE department_id = ?";

        // Подключение к БД
        try (Connection connection = DatabaseConnection.getConnection();
             // Сохраняем sql-запрос
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Подставляем id отдела под ?
            statement.setInt(1, departmentId);

            // Выполняем String sql в БД
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Возвращаем количество сотрудников
                    return resultSet.getInt(1);
                }
            }
        }

        // Если сотрудников в отделе нет - то возвращаем 0
        return 0;
    }

    // Метод для подсчета количества сотрудников, привязанных к должности
    public int countByPositionId(int positionId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM employees WHERE position_id = ?";

        // Подключение к БД
        try (Connection connection = DatabaseConnection.getConnection();
             // Сохраняем sql-запрос
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Подставляем id должности под ?
            statement.setInt(1, positionId);

            // Выполняем String sql в БД
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Возвращаем количество сотрудников
                    return resultSet.getInt(1);
                }
            }
        }

        // Если нет сотрудников, привязанных к должности - то возвращаем 0
        return 0;
    }

    // Метод для проверки, есть ли уже начальник у отдела
    public boolean existsHeadByDepartmentId(int departmentId) throws SQLException {
        String sql = """
                SELECT COUNT(*)
                FROM employees
                WHERE department_id = ?
                  AND is_department_head = 1
                """;

        // Подключение к БД
        try (Connection connection = DatabaseConnection.getConnection();
             // Сохраняем sql-запрос
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Подставляем id отдела под ?
            statement.setInt(1, departmentId);

            // Выполняем String sql в БД
            try (ResultSet resultSet = statement.executeQuery()) {
                // Возвращаем true, если у отдела есть Начальник
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }

        // Если Начальник не назначен - то возвращаем false
        return false;
    }

    // Метод для проверки, есть ли другой начальник у отдела, кроме текущего сотрудника (метод для редактирования)
    public boolean existsAnotherHeadByDepartmentId(int departmentId, int employeeId) throws SQLException {
        String sql = """
                SELECT COUNT(*)
                FROM employees
                WHERE department_id = ?
                  AND is_department_head = 1
                  AND id <> ?
                """;

        // Подключение к БД
        try (Connection connection = DatabaseConnection.getConnection();
             // Сохраняем sql-запрос
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Подставляем id под ?
            statement.setInt(1, departmentId);
            statement.setInt(2, employeeId);

            // Выполняем String sql в БД
            try (ResultSet resultSet = statement.executeQuery()) {
                // Возвращаем true, если у отдела есть Начальник
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }

        // Если Начальник не назначен - то возвращаем false
        return false;
    }
}