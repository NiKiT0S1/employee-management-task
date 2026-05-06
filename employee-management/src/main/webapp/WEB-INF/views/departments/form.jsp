<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <title>Форма отдела</title>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">

  <h1 class="mb-4">
    <c:choose>
      <c:when test="${formAction == 'save'}">
        Добавление отдела
      </c:when>
      <c:otherwise>
        Редактирование отдела
      </c:otherwise>
    </c:choose>
  </h1>

  <div class="card shadow-sm">
    <div class="card-body">

      <form action="${pageContext.request.contextPath}/departments?action=${formAction}"
            method="post">

        <c:if test="${formAction == 'update'}">
          <input type="hidden" name="id" value="${department.id}">
        </c:if>

        <div class="mb-3">
          <label for="departmentName" class="form-label">Название отдела</label>
          <input type="text"
                 class="form-control"
                 id="departmentName"
                 name="departmentName"
                 value="${department.departmentName}"
                 required>
        </div>

        <div class="mb-3">
          <label for="departmentPhone" class="form-label">Телефон отдела</label>
          <input type="text"
                 class="form-control"
                 id="departmentPhone"
                 name="departmentPhone"
                 value="${department.departmentPhone}"
                 required>
        </div>

        <div class="mb-3">
          <label for="departmentEmail" class="form-label">Email отдела</label>
          <input type="email"
                 class="form-control"
                 id="departmentEmail"
                 name="departmentEmail"
                 value="${department.departmentEmail}"
                 required>
        </div>

        <button type="submit" class="btn btn-success">
          Сохранить
        </button>

        <a href="${pageContext.request.contextPath}/departments"
           class="btn btn-secondary">
          Отмена
        </a>
      </form>

    </div>
  </div>

</div>

</body>
</html>