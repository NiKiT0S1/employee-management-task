package com.task.service;

import com.task.dao.DepartmentDao;
import com.task.dao.EmployeeDao;
import com.task.model.Department;

import java.sql.SQLException;
import java.util.List;

public class DepartmentService {

    // Создание объектов DAO-классов для взаимодействия с БД
    private final DepartmentDao departmentDao =  new DepartmentDao();
    private final EmployeeDao employeeDao = new EmployeeDao();

    // Метод для возврата списка всех отделов
    public List<Department> findAll() throws SQLException {
        return departmentDao.findAll();
    }

    // Метод для возврата конкретного отдела по id
    public Department findById(int id) throws SQLException {
        return departmentDao.findById(id);
    }

    // Метод для сохранения нового отдела
    public void save(Department department) throws SQLException {
        // Перед добавлением нового отдела идет проверка полей (все ли поля, которые NOT NULL, заполнены)
        validateDepartment(department);

        // Если все окей, то сохраняем новое поле
        departmentDao.save(department);
    }

    // Метод для изменения данных существующего отдела
    public void update(Department department) throws SQLException {
        // Перед изменением данных отдела идет проверка полей
        validateDepartment(department);

        // Если все окей, то применяем изменения
        departmentDao.update(department);
    }


    // Метод для удаления конкретного отдела по id
    public void delete(int id) throws SQLException {
        // Переменная для подсчета количества сотрудников конкретного отдела
        int employeesCount = employeeDao.countByDepartmentId(id);

        // Если к отделу привязаны сотрудники, то операцию нельзя выполнить из-за текущего состояния данных
        if (employeesCount > 0) {
            throw new IllegalStateException("Нельзя удалить отдел, пока в нем есть сотрудники");
        }

        // Если отдел пуст, то производим удаление отдела
        departmentDao.delete(id);
    }

    // Метод для обработки ошибок, если переданы некорректные (неполные) данные
    private void validateDepartment(Department department) {
        if (department.getDepartmentName() == null || department.getDepartmentName().isBlank()) {
            throw new IllegalArgumentException("Название отдела не может быть пустым");
        }

        if (department.getDepartmentPhone()== null || department.getDepartmentPhone().isBlank()) {
            throw new IllegalArgumentException("Телефон отдела не может быть пустым");
        }

        if (department.getDepartmentEmail()== null || department.getDepartmentEmail().isBlank()) {
            throw new IllegalArgumentException("Email отдела не может быть пустым");
        }
    }
}
