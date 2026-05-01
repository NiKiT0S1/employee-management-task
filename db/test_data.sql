INSERT INTO positions (position_name, position_salary)
VALUES ('Начальник отдела', 700000);

INSERT INTO departments (department_name, department_phone, department_email)
VALUES ('IT отдел', '+7 (495) 123-45-67', 'it@company.ru');

INSERT INTO employees (full_name, department_id, position_id)
VALUES ('Иванов Артем Сергеевич', 1, 1);

UPDATE departments
SET head_id = 1
WHERE id = 1;