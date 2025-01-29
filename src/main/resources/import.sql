-- Inserir dados na tabela "employee"
INSERT INTO employee (employee_id, registration, first_name, last_name, department, email, position) VALUES ('123e4567-e89b-12d3-a456-426614174000', 'EMP001', 'John', 'Doe', 'Finance', 'john.doe@example.com', 'Accountant'),('223e4567-e89b-12d3-a456-426614174001', 'EMP002', 'Jane', 'Smith', 'IT', 'jane.smith@example.com', 'Developer');

-- Inserir dados na tabela "manager"
INSERT INTO manager (manager_id, first_name, last_name, department, email) VALUES ('323e4567-e89b-12d3-a456-426614174002', 'Alice', 'Johnson', 'Finance', 'alice.johnson@example.com'), ('423e4567-e89b-12d3-a456-426614174003', 'Bob', 'Brown', 'IT', 'bob.brown@example.com');

-- Inserir dados na tabela "timesheet"
INSERT INTO timesheet (timesheet_id, employee, timesheet_month, timesheet_year, total_hours, time_sheet_created,  time_sheet_update) VALUES ('523e4567-e89b-12d3-a456-426614174004', '123e4567-e89b-12d3-a456-426614174000', 1, 2025, '14:00:00', '2025-01-01 08:00:00', '2025-01-07 18:00:00'), ('623e4567-e89b-12d3-a456-426614174005', '223e4567-e89b-12d3-a456-426614174001', 1, 2025, '19:00:00', '2025-01-01 08:00:00', '2025-01-07 18:00:00');

-- Inserir dados na tabela "work_entry"
INSERT INTO work_entry (work_entry_id, timesheet_id, employee_id, start_time, end_time, date_created, date_update) VALUES ('723e4567-e89b-12d3-a456-426614174006', '523e4567-e89b-12d3-a456-426614174004', '123e4567-e89b-12d3-a456-426614174000', '2025-01-28T09:00:00', '2025-01-28T17:00:00', '2025-01-28T09:00:00', '2025-01-28T17:00:00');

-- Inserir dados na tabela "approval"
INSERT INTO approval (approval_id, timesheet, manager, approval_status, approval_date_created, approval_date_update, comments) VALUES ('923e4567-e89b-12d3-a456-426614174008', '523e4567-e89b-12d3-a456-426614174004', '323e4567-e89b-12d3-a456-426614174002', 1, '2025-01-08 10:00:00', '2025-01-08 10:30:00', 'Approved successfully.'),('a23e4567-e89b-12d3-a456-426614174009', '623e4567-e89b-12d3-a456-426614174005', '423e4567-e89b-12d3-a456-426614174003', 2, '2025-01-08 11:00:00', '2025-01-08 11:30:00', 'Requires adjustments.');
