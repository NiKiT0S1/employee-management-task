<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Отделы</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Отделы</h1>

        <a href="${pageContext.request.contextPath}/departments?action=new"
           class="btn btn-primary">
            Добавить отдел
        </a>
    </div>

    <a href="${pageContext.request.contextPath}/"
       class="btn btn-secondary mb-3">
        На главную
    </a>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">
                ${error}
        </div>
    </c:if>

    <div class="card shadow-sm">
        <div class="card-body">

            <div class="table-responsive">
                <table class="table table-bordered table-striped table-hover align-middle">
                    <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Название отдела</th>
                        <th>Телефон</th>
                        <th>Email</th>
                        <th>Начальник</th>
                        <th>Кол-во сотрудников</th>
                        <th>Действия</th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach var="department" items="${departments}">
                        <tr>
                            <td>${department.id}</td>
                            <td>${department.departmentName}</td>
                            <td>${department.departmentPhone}</td>
                            <td>${department.departmentEmail}</td>

                            <td>
                                <c:choose>
                                    <c:when test="${not empty department.headName}">
                                        ${department.headName}
                                    </c:when>
                                    <c:otherwise>
                                        Не назначен
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td>${department.employeesCount}</td>

                            <td>
                                <div class="d-flex gap-2">
                                    <a href="${pageContext.request.contextPath}/departments?action=edit&id=${department.id}"
                                       class="btn btn-warning btn-sm">
                                        Редактировать
                                    </a>

                                    <form action="${pageContext.request.contextPath}/departments?action=delete&id=${department.id}"
                                          method="post">
                                        <button type="submit"
                                                class="btn btn-danger btn-sm"
                                                onclick="return confirm('Вы уверены, что хотите удалить отдел?');">
                                            Удалить
                                        </button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

        </div>
    </div>

</div>

</body>
</html>