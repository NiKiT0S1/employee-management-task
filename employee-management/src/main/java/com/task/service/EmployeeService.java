package com.task.service;

import com.task.dao.EmployeeDao;
import com.task.dao.PositionDao;
import com.task.model.Employee;
import com.task.model.Position;

import java.sql.SQLException;
import java.util.List;

public class EmployeeService {

    // Создание объектов DAO-классов для взаимодействия с БД
    private final EmployeeDao employeeDao = new EmployeeDao();
    private final PositionDao positionDao = new PositionDao();

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

        // Перед добавлением нового сотрудника проверяем правила его назначения (а именно случай, когда новый сотрудник - Начальник отдела)
        validateHeadOfDepartment(employee, false);

        // Если все окей, то сохраняем новое поле
        employeeDao.save(employee);
    }

    // Метод для изменения данных существующего сотрудника
    public void update(Employee employee) throws SQLException {
        // Перед изменением данных сотрудника идет проверка полей
        validateEmployee(employee);

        // Перед изменением данных сотрудника проверяем правила его назначения (а именно случай, когда сотрудник - Начальник отдела)
        validateHeadOfDepartment(employee, true);

        // Если все окей, то изменяем поле
        employeeDao.update(employee);
    }

    // Метод для удаления конкретного сотрудника по id
    public void delete(int id) throws SQLException {
        // Ищем сотрудника по id
        Employee employee = employeeDao.findById(id);

        // Если сотрудник не найден - выбрасываем сообщение об ошибке
        if (employee == null) {
            throw new IllegalArgumentException("Сотрудник не найден");
        }

        // Если сотрудник является Начальником отдела - запрещаем изменение его состояния и выбрасываем соответствующее сообщение
        if (employee.isHeadOfDepartment()) {
            throw new IllegalStateException(
                    "Нельзя удалить сотрудника, если он назначен начальником отдела"
            );
        }

        // Если сотрудник не является Начальником, то производится его удаление
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

    // Метод для проверки правил назначения начальника отдела
    private void validateHeadOfDepartment(Employee employee, boolean updateMode) throws SQLException {
        // Если сотрудник является Начальником, то берем текущие данные о его должности по id
        Position position = positionDao.findById(employee.getPositionId());

        // Если должность не назначена - выбрасываем ошибку
        if (position == null) {
            throw new IllegalArgumentException("Выбранная должность не найдена");
        }

        // Проверяем, выбрана ли у сотрудника должность "Начальник отдела"
        boolean positionIsHead = "Начальник отдела".equalsIgnoreCase(position.getPositionName());

        // Проверяем, отмечен ли checkbox "Назначить начальником отдела"
        boolean employeeIsHead = employee.isHeadOfDepartment();

        // Если выбрана только должность Начальника отдела, но не отмечен checkbox - возвращаем сообщение об ошибке
        if (positionIsHead && !employeeIsHead) {
            throw new IllegalArgumentException(
                    "Если выбрана должность 'Начальник отдела', необходимо отметить статус начальника отдела"
            );
        }

        // Если отмечен только checkbox, без выбора соответствующей должности - возвращаем сообщение об ошибке
        if (!positionIsHead && employeeIsHead) {
            throw new IllegalArgumentException(
                    "Начальником отдела можно назначить только сотрудника с должностью 'Начальник отдела'"
            );
        }

        // Если сотрудник не является Начальником отдела - пропускаем проверку через validateHeadOfDepartment
        if (!employeeIsHead) {
            return;
        }

        // Переменная для обозначения существующего Начальника отдела
        boolean headExists;

        // Если активировано редактирования данных, проверяем есть ли другой Начальник отдела помимо этого сотрудника
        if (updateMode) {
            headExists = employeeDao.existsAnotherHeadByDepartmentId(
                    employee.getDepartmentId(),
                    employee.getId()
            );
        }
        // Если это режим добавления нового сотрудника (метод save) - проверяем есть ли вообще Начальник отдела
        else {
            headExists = employeeDao.existsHeadByDepartmentId(employee.getDepartmentId());
        }

        // Если Начальник отдела есть - возвращаем запрет об изменении состояния сотрудника
        if (headExists) {
            throw new IllegalStateException(
                    "В выбранном отделе уже назначен начальник отдела"
            );
        }
    }
}
