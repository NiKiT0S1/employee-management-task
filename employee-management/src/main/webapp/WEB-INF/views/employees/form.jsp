<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <title>Форма сотрудника</title>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        rel="stylesheet">
</head>
<body>
<div class="container mt-5">

  <h1 class="mb-4">
    <c:choose>
      <c:when test="${formAction == 'save'}">
        Добавление сотрудника
      </c:when>
      <c:otherwise>
        Редактирование сотрудника
      </c:otherwise>
    </c:choose>
  </h1>

  <form action="${pageContext.request.contextPath}/employees?action=${formAction}"
        method="post">

    <c:if test="${formAction == 'update'}">
      <input type="hidden" name="id" value="${employee.id}">
    </c:if>

    <div class="mb-3">
      <label for="fullName" class="form-label">ФИО сотрудника</label>
      <input type="text"
             class="form-control"
             id="fullName"
             name="fullName"
             value="${employee.fullName}"
             required>
    </div>

    <div class="mb-3">
      <label for="departmentId" class="form-label">Отдел</label>
      <select class="form-select" id="departmentId" name="departmentId" required>
        <option value="">Выберите отдел</option>

        <c:forEach var="department" items="${departments}">
          <option value="${department.id}"
                  <c:if test="${employee.departmentId == department.id}">selected</c:if>>
              ${department.departmentName}
          </option>
        </c:forEach>
      </select>
    </div>

    <div class="mb-3">
      <label for="positionId" class="form-label">Должность</label>
      <select class="form-select" id="positionId" name="positionId" required>
        <option value="">Выберите должность</option>

        <c:forEach var="position" items="${positions}">
          <option value="${position.id}"
                  <c:if test="${employee.positionId == position.id}">selected</c:if>>
              ${position.positionName} — ${position.positionSalary}
          </option>
        </c:forEach>
      </select>
    </div>

    <button type="submit" class="btn btn-success">
      Сохранить
    </button>

    <a href="${pageContext.request.contextPath}/employees"
       class="btn btn-secondary">
      Отмена
    </a>
  </form>

</div>
</body>
</html>