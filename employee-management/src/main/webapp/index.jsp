<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Учет сотрудников предприятия</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">

    <div class="card shadow-sm">
        <div class="card-body p-5">

            <h1 class="mb-3">Учет сотрудников предприятия</h1>

            <p class="lead mb-4">
                Клиент-серверное приложение для управления отделами, должностями
                и сотрудниками предприятия.
            </p>

            <div class="row g-3">

                <div class="col-md-4">
                    <div class="card h-100 border-primary">
                        <div class="card-body">
                            <h5 class="card-title">Отделы</h5>
                            <p class="card-text">
                                Просмотр, добавление, редактирование и удаление отделов.
                            </p>
                            <a href="${pageContext.request.contextPath}/departments"
                               class="btn btn-primary">
                                Открыть отделы
                            </a>
                        </div>
                    </div>
                </div>

                <div class="col-md-4">
                    <div class="card h-100 border-success">
                        <div class="card-body">
                            <h5 class="card-title">Должности</h5>
                            <p class="card-text">
                                Управление должностями сотрудников и их зарплатами.
                            </p>
                            <a href="${pageContext.request.contextPath}/positions"
                               class="btn btn-success">
                                Открыть должности
                            </a>
                        </div>
                    </div>
                </div>

                <div class="col-md-4">
                    <div class="card h-100 border-dark">
                        <div class="card-body">
                            <h5 class="card-title">Сотрудники</h5>
                            <p class="card-text">
                                Просмотр сотрудников, отделов, должностей и статуса.
                            </p>
                            <a href="${pageContext.request.contextPath}/employees"
                               class="btn btn-dark">
                                Открыть сотрудников
                            </a>
                        </div>
                    </div>
                </div>

            </div>

        </div>
    </div>

</div>

</body>
</html>