package com.task.servlet;

import com.task.model.Position;
import com.task.service.PositionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

// Обработка HTTP-запросов по endpoint'у "/positions"
@WebServlet("/positions")
public class PositionServlet extends HttpServlet {

    // Создание объекта Service-класса для работы с логикой взаимодействия над должностями
    private final PositionService positionService = new PositionService();

    // Метод для обработки GET-запросов
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Переменная с параметром действия
        String action = request.getParameter("action");

        try {
            // Отображение списка должностей, до тех пор, пока параметр действия не передан
            if (action == null || action.isBlank()) {
                showList(request, response);
                return;
            }

            switch (action) {
                // Открыть форму добавления должности
                case "new":
                    showCreateForm(request, response);
                    break;
                // Открыть форму редактирования должности
                case "edit":
                    showEditForm(request, response);
                    break;
                // В ином случае (если невалидный action) вывести список должностей
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
                // Сохранить новую должность
                case "save":
                    savePosition(request, response);
                    break;
                // Обновить существующую должность
                case "update":
                    updatePosition(request, response);
                    break;
                // Удалить существующую должность
                case "delete":
                    deletePosition(request, response);
                    break;
                // В ином случае перенаправление пользователя на страницу со списком должностей
                default:
                    response.sendRedirect(request.getContextPath() + "/positions");
                    break;
            }
        }
        // Ловим и обрабатываем ошибки (если пользователь некорректно или не полностью ввел данные; если текущую операцию невозможно выполнить)
        catch (IllegalArgumentException | IllegalStateException e) {
            // Передача текста об ошибку на страницу
            request.setAttribute("error", e.getMessage());

            // В случае ошибки - перенаправление пользователя на страницу со списком должностей
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

    // Метод для отображения списка должностей
    private void showList(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        // Получаем список должностей через Service (тот вызовет DAO -> DatabaseConnection -> данные из БД), и кладем в request
        request.setAttribute("positions", positionService.findAll());

        // Выбор JSP-страницы, куда должен быть передан запрос со списком должностей
        request.getRequestDispatcher("/WEB-INF/views/positions/list.jsp")
                .forward(request, response);
    }

    // Метод для отображения формы добавления новой должности
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Создание пустого объекта Position для работы с добавлением должности
        request.setAttribute("position", new Position());

        // Сохраняем действие JSP-формы - добавляем новую должность
        request.setAttribute("formAction", "save");

        // Передаем запрос на JSP-страницу с формой должности
        request.getRequestDispatcher("/WEB-INF/views/positions/form.jsp")
                .forward(request, response);
    }

    // Метод для отображения формы редактирования должности
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        // Получаем id должности из параметра запроса
        int id = parseId(request);

        // Ищем id должности через Service-слой
        Position position = positionService.findById(id);

        // Если должность с таким id не найдена - показываем список должностей со всплывающей ошибкой
        if (position == null) {
            request.setAttribute("error", "Должность не найдена");
            showList(request, response);
            return;
        }

        // Если должность найдена - передаем на JSP-страницу формы для редактирования (используем все тот же объект position, обновляем существующую должность)
        request.setAttribute("position", position);
        request.setAttribute("formAction", "update");

        // Передаем запрос на JSP-страницу формы должности
        request.getRequestDispatcher("/WEB-INF/views/positions/form.jsp")
                .forward(request, response);
    }

    // Метод для сохранения должности
    private void savePosition(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        // Создаем объект класса Position на основе данных, пришедших из формы для создания должности
        Position position = buildPositionFromRequest(request);

        // Передаем объект в Service-слой -- сохраняем новую должность
        positionService.save(position);

        // Перенаправляем пользователя на страницу со списком должностей при успешном добавлении новой должности
        response.sendRedirect(request.getContextPath() + "/positions");
    }

    // Метод для обновления должности
    private void updatePosition(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        // Создаем объект класса Position на основе данных, пришедших из формы для обновления должности
        Position position = buildPositionFromRequest(request);

        // Получаем id должности, которую нужно изменить
        position.setId(parseId(request));

        // Передаем объект в Service-слой -- обновляем должность
        positionService.update(position);

        // Перенаправляем пользователя на страницу со списком должностей при успешном обновлении должности
        response.sendRedirect(request.getContextPath() + "/positions");
    }

    // Метод для удаления должности
    private void deletePosition(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        // Получаем id должности из параметра запроса
        int id = parseId(request);

        // Передаем id должности в Service-слой -- удаляем конкретную должность
        positionService.delete(id);

        // Перенаправляем пользователя на страницу со списком должностей при успешном удалении должности
        response.sendRedirect(request.getContextPath() + "/positions");
    }

    // Метод для создания объекта класса Position из параметров HTTP-запроса
    private Position buildPositionFromRequest(HttpServletRequest request) {
        // Создаем пустой объект
        Position position = new Position();

        // Считываем значения из той или иной формы (добавление/редактирование) и записываем в объект
        position.setPositionName(request.getParameter("positionName"));

        // Зарплата приходит из формы в виде String, поэтому преобразуем ее в BigDecimal
        position.setPositionSalary(new BigDecimal(request.getParameter("positionSalary")));

        // Возвращаем объект
        return position;
    }

    // Метод для преобразования id должности, полученного из HTTP-запроса формы из String в int
    private int parseId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("id"));
    }
}