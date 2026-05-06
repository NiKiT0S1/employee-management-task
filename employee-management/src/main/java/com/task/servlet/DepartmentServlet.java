package com.task.servlet;

import com.task.model.Department;
import com.task.service.DepartmentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

// Обработка HTTP-запросов по endpoint'у "/departments"
@WebServlet("/departments")
public class DepartmentServlet extends HttpServlet {

    // Создание объекта Service-класса для работы с логикой взаимодействия над отделами
    private final DepartmentService departmentService = new DepartmentService();

    // Метод для обработки GET-запросов
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Переменная с параметром действия
        String action = request.getParameter("action");

        try {
            // Отображение списка отделов, до тех пор, пока параметр действия не передан
            if (action == null || action.isBlank()) {
                showList(request, response);
                return;
            }

            switch (action) {
                // Открыть форму добавления отдела
                case "new":
                    showCreateForm(request, response);
                    break;
                // Открыть форму редактирования отдела
                case "edit":
                    showEditForm(request, response);
                    break;
                // В ином случае (если невалидный action) вывести список отделов
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
                // Сохранить новый отдел
                case "save":
                    saveDepartment(request, response);
                    break;
                // Обновить существующий отдел
                case "update":
                    updateDepartment(request, response);
                    break;
                // Удалить существующий отдел
                case "delete":
                    deleteDepartment(request, response);
                    break;
                // В ином случае перенаправление пользователя на страницу со списком отделов
                default:
                    response.sendRedirect(request.getContextPath() + "/departments");
                    break;
            }
        }
        // Ловим и обрабатываем ошибки (если пользователь некорректно или не полностью ввел данные; если текущую операцию невозможно выполнить)
        catch (IllegalArgumentException | IllegalStateException e) {
            // Передача текста об ошибку на страницу
            request.setAttribute("error", e.getMessage());

            // В случае ошибки - перенаправление пользователя на страницу со списком отделов
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

    // Метод для отображения списка отделов
    private void showList(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        // Получаем список отделов через Service (тот вызовет DAO -> DatabaseConnection -> данные из БД), и кладем в request
        request.setAttribute("departments", departmentService.findAll());

        // Выбор JSP-страницы, куда должен быть передан запрос со списком отделов
        request.getRequestDispatcher("/WEB-INF/views/departments/list.jsp")
                .forward(request, response);
    }

    // Метод для отображения формы добавления нового отдела
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        // Создание пустого объекта Department для работы с добавлением отдела
        request.setAttribute("department", new Department());
        // Сохраняем действие JSP-формы - добавляем новый отдел
        request.setAttribute("formAction", "save");

        // Передаем запрос на JSP-страницу с формой отдела
        request.getRequestDispatcher("/WEB-INF/views/departments/form.jsp")
                .forward(request, response);
    }

    // Метод для отображения формы редактирования отдела
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        // Получаем id отдела из параметра запроса
        int id = parseId(request);
        // Ищем id отдела через Service-слой
        Department department = departmentService.findById(id);

        // Если отдела с таким id не найден - показываем список отделов со всплывающей ошибкой
        if (department == null) {
            request.setAttribute("error", "Отдел не найден");
            showList(request, response);
            return;
        }

        // Если отдел найден - передаем на JSP-страницу формы для редактирования (используем все тот же объект department, выводим список сотрудников, обновляем существующий отдел)
        request.setAttribute("department", department);
        request.setAttribute("formAction", "update");

        // Передаем запрос на JSP-страницу формы отдела
        request.getRequestDispatcher("/WEB-INF/views/departments/form.jsp")
                .forward(request, response);
    }

    // Метод для сохранения отдела
    private void saveDepartment(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        // Создаем объект класса Department на основе данных, пришедших из формы для создания отдела
        Department department = buildDepartmentFromRequest(request);

        // Передаем объект в Service-слой -- сохраняем новый отдел
        departmentService.save(department);

        // Перенаправляем пользователя на страницу со списком отделов при успешном добавлении нового отдела
        response.sendRedirect(request.getContextPath() + "/departments");
    }

    // Метод для обновления отдела
    private void updateDepartment(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        // Создаем объект класса Department на основе данных, пришедших из формы для обновления отдела
        Department department = buildDepartmentFromRequest(request);

        // Получаем id отдела, который нужно изменить
        department.setId(parseId(request));

        // Передаем объект в Service-слой -- обновляем отдел
        departmentService.update(department);

        // Перенаправляем пользователя на страницу со списком отделов при успешном обновлении отдела
        response.sendRedirect(request.getContextPath() + "/departments");
    }

    // Метод для удаления отдела
    private void deleteDepartment(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        // Получаем id отдела из параметра запроса
        int id = parseId(request);

        // Передаем id отдела в Service-слой -- удаляем конкретный отдел
        departmentService.delete(id);

        // Перенаправляем пользователя на страницу со списком отделов при успешном удалении отдела
        response.sendRedirect(request.getContextPath() + "/departments");
    }

    // Метод для создания объекта класса Department из параметров HTTP-запроса
    private Department buildDepartmentFromRequest(HttpServletRequest request) {
        // Создаем пустой объект
        Department department = new Department();

        // Считываем значения из той или иной формы (добавление/редактирование) и записываем в объект
        department.setDepartmentName(request.getParameter("departmentName"));
        department.setDepartmentPhone(request.getParameter("departmentPhone"));
        department.setDepartmentEmail(request.getParameter("departmentEmail"));

        // Возвращаем объект
        return department;
    }

    // Метод для преобразования id отдела, полученного из HTTP-запроса формы из String в int
    private int parseId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("id"));
    }
}
