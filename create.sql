
    create table approval (
        approval_status smallint not null check (approval_status between 0 and 2),
        approval_date_created timestamp(6),
        approval_date_update timestamp(6),
        approval_id uuid not null,
        manager uuid,
        timesheet uuid not null unique,
        comments varchar(400) not null,
        primary key (approval_id)
    );

    create table employee (
        registration varchar(7) not null unique,
        employee_id uuid not null,
        first_name varchar(100) not null,
        last_name varchar(100) not null,
        department varchar(200) not null,
        email varchar(200) not null unique,
        position varchar(200) not null,
        primary key (employee_id)
    );

    create table manager (
        manager_id uuid not null,
        first_name varchar(100) not null,
        last_name varchar(100) not null,
        department varchar(200) not null,
        email varchar(200) not null unique,
        primary key (manager_id)
    );

    create table timesheet (
        timesheet_month integer,
        timesheet_year integer,
        total_hours time(6) not null,
        time_sheet_created timestamp(6),
        time_sheet_update timestamp(6),
        employee uuid,
        timesheet_id uuid not null,
        primary key (timesheet_id)
    );

    create table work_entry (
        date_created timestamp(6),
        date_update timestamp(6),
        end_time timestamp(6),
        start_time timestamp(6),
        employee_id uuid,
        timesheet_id uuid,
        work_entry_id uuid not null,
        primary key (work_entry_id)
    );

    alter table if exists approval 
       add constraint FK9ud1h5s6ko10j4864l358fuut 
       foreign key (manager) 
       references manager;

    alter table if exists approval 
       add constraint FKbkpm7lftdsoidswrocqn70u9o 
       foreign key (timesheet) 
       references timesheet;

    alter table if exists timesheet 
       add constraint FKee8t5gxfnsbfte1wvkmel8klk 
       foreign key (employee) 
       references employee;

    alter table if exists work_entry 
       add constraint FK3fosthwu4qde8ec307flth32w 
       foreign key (employee_id) 
       references employee;

    alter table if exists work_entry 
       add constraint FKh8jsds4akk4fqm2j6gg3l4ia0 
       foreign key (timesheet_id) 
       references timesheet;
INSERT INTO employee (employee_id, registration, first_name, last_name, department, email, position) VALUES ('123e4567-e89b-12d3-a456-426614174000', 'EMP001', 'John', 'Doe', 'Finance', 'john.doe@example.com', 'Accountant'),('223e4567-e89b-12d3-a456-426614174001', 'EMP002', 'Jane', 'Smith', 'IT', 'jane.smith@example.com', 'Developer');
INSERT INTO manager (manager_id, first_name, last_name, department, email) VALUES ('323e4567-e89b-12d3-a456-426614174002', 'Alice', 'Johnson', 'Finance', 'alice.johnson@example.com'), ('423e4567-e89b-12d3-a456-426614174003', 'Bob', 'Brown', 'IT', 'bob.brown@example.com');
INSERT INTO timesheet (timesheet_id, employee, timesheet_month, timesheet_year, total_hours, time_sheet_created,  time_sheet_update) VALUES ('523e4567-e89b-12d3-a456-426614174004', '123e4567-e89b-12d3-a456-426614174000', 1, 2025, '14:00:00', '2025-01-01 08:00:00', '2025-01-07 18:00:00'), ('623e4567-e89b-12d3-a456-426614174005', '223e4567-e89b-12d3-a456-426614174001', 1, 2025, '19:00:00', '2025-01-01 08:00:00', '2025-01-07 18:00:00');
INSERT INTO work_entry (work_entry_id, timesheet_id, employee_id, start_time, end_time, date_created, date_update) VALUES ('723e4567-e89b-12d3-a456-426614174006', '523e4567-e89b-12d3-a456-426614174004', '123e4567-e89b-12d3-a456-426614174000', '2025-01-28T09:00:00', '2025-01-28T17:00:00', '2025-01-28T09:00:00', '2025-01-28T17:00:00');
INSERT INTO approval (approval_id, timesheet, manager, approval_status, approval_date_created, approval_date_update, comments) VALUES ('923e4567-e89b-12d3-a456-426614174008', '523e4567-e89b-12d3-a456-426614174004', '323e4567-e89b-12d3-a456-426614174002', 1, '2025-01-08 10:00:00', '2025-01-08 10:30:00', 'Approved successfully.'),('a23e4567-e89b-12d3-a456-426614174009', '623e4567-e89b-12d3-a456-426614174005', '423e4567-e89b-12d3-a456-426614174003', 2, '2025-01-08 11:00:00', '2025-01-08 11:30:00', 'Requires adjustments.');
