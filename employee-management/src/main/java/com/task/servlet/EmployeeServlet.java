package com.task.servlet;

import com.task.model.Employee;
import com.task.service.DepartmentService;
import com.task.service.EmployeeService;
import com.task.service.PositionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

// Обработка HTTP-запросов по endpoint'у "/employees"
@WebServlet("/employees")
public class EmployeeServlet extends HttpServlet {

    // Создание объектов Service-классов для работы с логикой взаимодействия над сотрудниками
    private final EmployeeService employeeService = new EmployeeService();
    private final DepartmentService departmentService = new DepartmentService();
    private final PositionService positionService = new PositionService();

    // Метод для обработки GET-запросов
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Переменная с параметром действия
        String action = request.getParameter("action");

        try {
            // Отображение списка сотрудников, до тех пор, пока параметр действия не передан
            if (action == null || action.isBlank()) {
                showList(request, response);
                return;
            }

            switch (action) {
                // Открыть форму добавления сотрудника
                case "new":
                    showCreateForm(request, response);
                    break;
                // Открыть форму редактирования сотрудника
                case "edit":
                    showEditForm(request, response);
                    break;
                // В ином случае (если невалидный action) вывести список сотрудников
                default:
                    showList(request, response);
                    break;
            }
        }
        // Ловим ошибки при работе с БД и оборачиваем для корректной обработки
        catch (SQLException e) {
            throw new ServletException("Ошибка работы с базой данных", e);
        }
    }

    // Метод для обработки POST-запросов
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Строка для корректной обработки данных форм (в частности для обработки кириллицы)
        request.setCharacterEncoding("UTF-8");

        // Переменная с параметром действия
        String action = request.getParameter("action");

        try {
            switch (action) {
                // Сохранить нового сотрудника
                case "save":
                    saveEmployee(request, response);
                    break;
                // Обновить существующего сотрудника
                case "update":
                    updateEmployee(request, response);
                    break;
                // Удалить существующего сотрудника
                case "delete":
                    deleteEmployee(request, response);
                    break;
                // В ином случае перенаправление пользователя на страницу со списком сотрудников
                default:
                    response.sendRedirect(request.getContextPath() + "/employees");
                    break;
            }
        }
        // Ловим и обрабатываем ошибки (если пользователь некорректно или не полностью ввел данные; если текущую операцию невозможно выполнить)
        catch (IllegalArgumentException | IllegalStateException e) {
            // Передача текста об ошибку на страницу
            request.setAttribute("error", e.getMessage());

            // В случае ошибки - перенаправление пользователя на страницу со списком сотрудников
            try {
                showList(request, response);
            }
            // Если с получением списка произошла ошибка - корректно ее обрабатываем
            catch (SQLException sqlException) {
                throw new ServletException("Ошибка работы с базой данных", sqlException);
            }
        }
        catch (SQLException e) {
            throw new ServletException("Ошибка работы с базой данных", e);
        }
    }

    // Метод для отображения списка сотрудников
    private void showList(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        // Получаем список сотрудников через Service (тот вызовет DAO -> DatabaseConnection -> данные из БД), и кладем в request
        request.setAttribute("employees", employeeService.findAll());

        // Выбор JSP-страницы, куда должен быть передан запрос со списком сотрудников
        request.getRequestDispatcher("/WEB-INF/views/employees/list.jsp")
                .forward(request, response);
    }

    // Метод для отображения формы добавления нового сотрудника
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        // Создание пустого объекта Employee для работы с добавлением сотрудника
        request.setAttribute("employee", new Employee());

        // Получаем список отделов для выбора отдела сотрудника
        request.setAttribute("departments", departmentService.findAll());

        // Получаем список должностей для выбора должности сотрудника
        request.setAttribute("positions", positionService.findAll());

        // Сохраняем действие JSP-формы - добавляем нового сотрудника
        request.setAttribute("formAction", "save");

        // Передаем запрос на JSP-страницу с формой сотрудника
        request.getRequestDispatcher("/WEB-INF/views/employees/form.jsp")
                .forward(request, response);
    }

    // Метод для отображения формы редактирования сотрудника
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        // Получаем id сотрудника из параметра запроса
        int id = parseId(request);

        // Ищем id сотрудника через Service-слой
        Employee employee = employeeService.findById(id);

        // Если сотрудник с таким id не найден - показываем список сотрудников со всплывающей ошибкой
        if (employee == null) {
            request.setAttribute("error", "Сотрудник не найден");
            showList(request, response);
            return;
        }

        // Если сотрудник найден - передаем на JSP-страницу формы для редактирования (используем все тот же объект employee, выводим список отделов и должностей, обновляем существующего сотрудника)
        request.setAttribute("employee", employee);
        request.setAttribute("departments", departmentService.findAll());
        request.setAttribute("positions", positionService.findAll());
        request.setAttribute("formAction", "update");

        // Передаем запрос на JSP-страницу формы сотрудника
        request.getRequestDispatcher("/WEB-INF/views/employees/form.jsp")
                .forward(request, response);
    }

    // Метод для сохранения сотрудника
    private void saveEmployee(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        // Создаем объект класса Employee на основе данных, пришедших из формы для создания сотрудника
        Employee employee = buildEmployeeFromRequest(request);

        // Передаем объект в Service-слой -- сохраняем нового сотрудника
        employeeService.save(employee);

        // Перенаправляем пользователя на страницу со списком сотрудников при успешном добавлении нового сотрудника
        response.sendRedirect(request.getContextPath() + "/employees");
    }

    // Метод для обновления сотрудника
    private void updateEmployee(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        // Создаем объект класса Employee на основе данных, пришедших из формы для обновления сотрудника
        Employee employee = buildEmployeeFromRequest(request);

        // Получаем id сотрудника, которого нужно изменить
        employee.setId(parseId(request));

        // Передаем объект в Service-слой -- обновляем сотрудника
        employeeService.update(employee);

        // Перенаправляем пользователя на страницу со списком сотрудников при успешном обновлении сотрудника
        response.sendRedirect(request.getContextPath() + "/employees");
    }

    // Метод для удаления сотрудника
    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        // Получаем id сотрудника из параметра запроса
        int id = parseId(request);

        // Передаем id сотрудника в Service-слой -- удаляем конкретного сотрудника
        employeeService.delete(id);

        // Перенаправляем пользователя на страницу со списком сотрудников при успешном удалении сотрудника
        response.sendRedirect(request.getContextPath() + "/employees");
    }

    // Метод для создания объекта класса Employee из параметров HTTP-запроса
    private Employee buildEmployeeFromRequest(HttpServletRequest request) {
        // Создаем пустой объект
        Employee employee = new Employee();

        // Считываем значения из той или иной формы (добавление/редактирование) и записываем в объект
        employee.setFullName(request.getParameter("fullName"));

        // departmentId приходит из формы в виде String, поэтому преобразуем его в int
        employee.setDepartmentId(Integer.parseInt(request.getParameter("departmentId")));

        // positionId приходит из формы в виде String, поэтому преобразуем его в int
        employee.setPositionId(Integer.parseInt(request.getParameter("positionId")));

        // Возвращаем объект
        return employee;
    }

    // Метод для преобразования id сотрудника, полученного из HTTP-запроса формы из String в int
    private int parseId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("id"));
    }
}