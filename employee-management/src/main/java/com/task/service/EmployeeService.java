package com.task.service;

import com.task.dao.DepartmentDao;
import com.task.dao.EmployeeDao;
import com.task.model.Employee;

import java.sql.SQLException;
import java.util.List;

public class EmployeeService {

    // Создание объектов DAO-классов для взаимодействия с БД
    private final EmployeeDao employeeDao = new EmployeeDao();
    private final DepartmentDao departmentDao = new DepartmentDao();

    // Метод для возврата списка всех сотрудников
    public List<Employee> findAll() throws SQLException {
        return employeeDao.findAll();
    }

    // Метод для возврата конкретного сотрудника по id
    public Employee findById(int id) throws SQLException {
        return employeeDao.findById(id);
    }

    // Метод для сохранения нового сотрудника
    public void save(Employee employee) throws SQLException {
        // Перед добавлением нового сотрудника идет проверка полей
        validateEmployee(employee);

        // Если все окей, то сохраняем новое поле
        employeeDao.save(employee);
    }

    // Метод для изменения данных существующего сотрудника
    public void update(Employee employee) throws SQLException {
        // Перед изменением данных сотрудника идет проверка полей
        validateEmployee(employee);

        // Если все окей, то изменяем поле
        employeeDao.update(employee);
    }

    // Метод для удаления конкретного сотрудника по id
    public void delete(int id) throws SQLException {
        // Переменная, содержащее булево значение относительно сотрудника (Начальник он, или нет)
        boolean isHeadOfDepartment = departmentDao.existsByHeadId(id);

        // Если сотрудник является Начальником отдела, то операцию нельзя выполнить из-за текущего состояния данных
        if (isHeadOfDepartment) {
            throw new IllegalStateException("Нельзя удалить сотрудника, потому что он назначен начальником отдела");
        }

        // Если сотрудник не является начальником, то производится его удаление
        employeeDao.delete(id);
    }

    // Метод для обработки ошибок, если переданы некорректные (неполные) данные
    private void validateEmployee(Employee employee) {
        if (employee.getFullName() == null || employee.getFullName().isBlank()) {
            throw new IllegalArgumentException("ФИО сотрудника не может быть пустым");
        }

        if (employee.getDepartmentId() <= 0) {
            throw new IllegalArgumentException("Необходимо выбрать отдел");
        }

        if (employee.getPositionId() <= 0) {
            throw new IllegalArgumentException("Необходимо выбрать должность");
        }
    }
}
