<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Сотрудники</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">
</head>
<body>
<div class="container mt-5">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Сотрудники</h1>

        <a href="${pageContext.request.contextPath}/employees?action=new"
           class="btn btn-primary">
            Добавить сотрудника
        </a>
    </div>

    <a href="${pageContext.request.contextPath}/" class="btn btn-secondary mb-3">
        На главную
    </a>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">
                ${error}
        </div>
    </c:if>

    <table class="table table-bordered table-striped align-middle">
        <thead class="table-dark">
        <tr>
            <th>ID</th>
            <th>ФИО</th>
            <th>Отдел</th>
            <th>Должность</th>
            <th>Зарплата</th>
            <th>Статус</th>
            <th>Действия</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="employee" items="${employees}">
            <tr>
                <td>${employee.id}</td>
                <td>${employee.fullName}</td>
                <td>${employee.departmentName}</td>
                <td>${employee.positionName}</td>
                <td>${employee.positionSalary}</td>

                <td>
                    <c:choose>
                        <c:when test="${employee.headOfDepartment}">
                            <span class="badge bg-success">Начальник отдела</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-secondary">Сотрудник</span>
                        </c:otherwise>
                    </c:choose>
                </td>

                <td>
                    <a href="${pageContext.request.contextPath}/employees?action=edit&id=${employee.id}"
                       class="btn btn-warning btn-sm">
                        Редактировать
                    </a>

                    <form action="${pageContext.request.contextPath}/employees?action=delete&id=${employee.id}"
                          method="post"
                          style="display:inline;">
                        <button type="submit"
                                class="btn btn-danger btn-sm"
                                onclick="return confirm('Вы уверены, что хотите удалить сотрудника?');">
                            Удалить
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

</div>
</body>
</html>