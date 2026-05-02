<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <title>Форма должности</title>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">

  <h1 class="mb-4">
    <c:choose>
      <c:when test="${formAction == 'save'}">
        Добавление должности
      </c:when>
      <c:otherwise>
        Редактирование должности
      </c:otherwise>
    </c:choose>
  </h1>

  <div class="card shadow-sm">
    <div class="card-body">

      <form action="${pageContext.request.contextPath}/positions?action=${formAction}"
            method="post">

        <c:if test="${formAction == 'update'}">
          <input type="hidden" name="id" value="${position.id}">
        </c:if>

        <div class="mb-3">
          <label for="positionName" class="form-label">Название должности</label>
          <input type="text"
                 class="form-control"
                 id="positionName"
                 name="positionName"
                 value="${position.positionName}"
                 required>
        </div>

        <div class="mb-3">
          <label for="positionSalary" class="form-label">Зарплата</label>
          <input type="number"
                 class="form-control"
                 id="positionSalary"
                 name="positionSalary"
                 value="${position.positionSalary}"
                 min="1"
                 step="0.01"
                 required>
        </div>

        <button type="submit" class="btn btn-success">
          Сохранить
        </button>

        <a href="${pageContext.request.contextPath}/positions"
           class="btn btn-secondary">
          Отмена
        </a>
      </form>

    </div>
  </div>

</div>

</body>
</html>