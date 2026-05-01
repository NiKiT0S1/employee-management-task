<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Учет сотрудников предприятия</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h1 class="mb-4">Учет сотрудников предприятия</h1>

    <p class="lead">
        Система для управления отделами, должностями и сотрудниками предприятия.
    </p>

    <div class="list-group mt-4">
        <a href="${pageContext.request.contextPath}/departments" class="list-group-item list-group-item-action">
            Отделы
        </a>
        <a href="${pageContext.request.contextPath}/positions" class="list-group-item list-group-item-action">
            Должности
        </a>
        <a href="${pageContext.request.contextPath}/employees" class="list-group-item list-group-item-action">
            Сотрудники
        </a>
    </div>
</div>
</body>
</html>