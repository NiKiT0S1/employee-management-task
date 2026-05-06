INSERT INTO departments (id, department_name, department_phone, department_email)
VALUES (1, 'IT Отдел', '+7 (495) 100-10-05', 'it@company.ru');

INSERT INTO departments (id, department_name, department_phone, department_email)
VALUES (2, 'Отдел кадров', '+7 (495) 200-20-02', 'hr_test@company.ru');

INSERT INTO departments (id, department_name, department_phone, department_email)
VALUES (3, 'Бухгалтерия', '+7 (495) 300-30-03', 'accounting@company.ru');

INSERT INTO departments (id, department_name, department_phone, department_email)
VALUES (6, 'Тестовый отдел', '0000', 'test@company.ru');


INSERT INTO positions (id, position_name, position_salary)
VALUES (1, 'Начальник отдела', 700000);

INSERT INTO positions (id, position_name, position_salary)
VALUES (2, 'Java разработчик', 500000);

INSERT INTO positions (id, position_name, position_salary)
VALUES (3, 'Системный администратор', 450000);

INSERT INTO positions (id, position_name, position_salary)
VALUES (4, 'QA инженер', 400000);

INSERT INTO positions (id, position_name, position_salary)
VALUES (5, 'HR менеджер', 420000);

INSERT INTO positions (id, position_name, position_salary)
VALUES (6, 'Рекрутер', 380000);

INSERT INTO positions (id, position_name, position_salary)
VALUES (7, 'Специалист по кадрам', 350000);

INSERT INTO positions (id, position_name, position_salary)
VALUES (8, 'Главный бухгалтер', 550000);

INSERT INTO positions (id, position_name, position_salary)
VALUES (9, 'Бухгалтер', 400000);

INSERT INTO positions (id, position_name, position_salary)
VALUES (10, 'Финансовый аналитик', 450000);

INSERT INTO positions (id, position_name, position_salary)
VALUES (11, 'QA Тестировщик', 350000);

INSERT INTO positions (id, position_name, position_salary)
VALUES (13, 'Example сотрудник', 9999999);


INSERT INTO employees (id, full_name, department_id, position_id, is_department_head)
VALUES (1, 'Иванов Артем Сергеевич', 1, 1, 1);

INSERT INTO employees (id, full_name, department_id, position_id, is_department_head)
VALUES (2, 'Смирнов Никита Олегович', 1, 2, 0);

INSERT INTO employees (id, full_name, department_id, position_id, is_department_head)
VALUES (3, 'Ким Алексей Андреевич', 1, 3, 0);

INSERT INTO employees (id, full_name, department_id, position_id, is_department_head)
VALUES (4, 'Петрова Дарья Игоревна', 1, 4, 0);

INSERT INTO employees (id, full_name, department_id, position_id, is_department_head)
VALUES (5, 'Сидорова Анна Сергеевна', 2, 1, 1);

INSERT INTO employees (id, full_name, department_id, position_id, is_department_head)
VALUES (6, 'Ахметова Алия Ерлановна', 2, 5, 0);

INSERT INTO employees (id, full_name, department_id, position_id, is_department_head)
VALUES (7, 'Морозов Павел Дмитриевич', 2, 6, 0);

INSERT INTO employees (id, full_name, department_id, position_id, is_department_head)
VALUES (8, 'Васильева Мария Андреевна', 2, 7, 0);

INSERT INTO employees (id, full_name, department_id, position_id, is_department_head)
VALUES (9, 'Кузнецов Дмитрий Александрович', 3, 1, 1);

INSERT INTO employees (id, full_name, department_id, position_id, is_department_head)
VALUES (10, 'Нурланова Айгерим Сериковна', 3, 8, 0);

INSERT INTO employees (id, full_name, department_id, position_id, is_department_head)
VALUES (11, 'Федоров Максим Викторович', 3, 9, 0);

INSERT INTO employees (id, full_name, department_id, position_id, is_department_head)
VALUES (12, 'Григорьева Елена Павловна', 3, 10, 0);

INSERT INTO employees (id, full_name, department_id, position_id, is_department_head)
VALUES (13, 'Бобылев Никита Евгеньевич', 6, 1, 1);

INSERT INTO employees (id, full_name, department_id, position_id, is_department_head)
VALUES (15, 'Васильев Василий Васильевич', 6, 13, 0);