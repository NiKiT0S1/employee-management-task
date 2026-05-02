package com.task.service;

import com.task.dao.EmployeeDao;
import com.task.dao.PositionDao;
import com.task.model.Position;

import java.sql.SQLException;
import java.util.List;

public class PositionService {

    // Создание объектов DAO-классов для взаимодействия с БД
    private final PositionDao positionDao = new PositionDao();
    private final EmployeeDao employeeDao = new EmployeeDao();

    // Метод для возврата списка всех должностей
    public List<Position> findAll() throws SQLException {
        return positionDao.findAll();
    }

    // Метод для возврата конкретной должности по id
    public Position findById(int id) throws SQLException {
        return positionDao.findById(id);
    }

    // Метод для сохранения новой должности
    public void save(Position position) throws SQLException {
        // Перед добавлением новой должности идет проверка полей
        validatePosition(position);

        // Если все окей, то сохраняем новое поле
        positionDao.save(position);
    }

    // Метод для изменения данных существующей должности
    public void update(Position position) throws SQLException {
        // Перед изменением данных должности идет проверка полей
        validatePosition(position);

        // Если все окей, то изменяем поле
        positionDao.update(position);
    }

    // Метод для удаления конкретной должности по id
    public void delete(int id) throws SQLException {
        // Переменная для подсчета количества сотрудников конкретной должности
        int employeeCount = employeeDao.countByPositionId(id);

        // Если к должности привязаны сотрудники, то операцию нельзя выполнить из-за текущего состояния данных
        if (employeeCount > 0) {
            throw new IllegalStateException("Нельзя удалить должность, пока она назначена сотрудникам");
        }

        // Если к должности никто не привязан, то производим ее удаление
        positionDao.delete(id);
    }

    // Метод для обработки ошибок, если переданы некорректные (неполные) данные
    public void validatePosition(Position position) {
        if (position.getPositionName() == null || position.getPositionName().isBlank()) {
            throw new IllegalArgumentException("Название должности не может быть пустым");
        }

        if (position.getPositionSalary() == null) {
            throw new IllegalArgumentException("Зарплата не может быть пустой");
        }

        if (position.getPositionSalary().signum() <= 0) {
            throw new IllegalArgumentException("Зарплата должна быть больше 0");
        }
    }
}
