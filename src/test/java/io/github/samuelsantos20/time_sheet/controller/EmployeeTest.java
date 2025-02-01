package io.github.samuelsantos20.time_sheet.controller;

import io.github.samuelsantos20.time_sheet.model.Employee;
import io.github.samuelsantos20.time_sheet.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmployeeTest {

    @Autowired
    private  EmployeeService employeeService;


    @Test
    public  void saveEmployee() {

        Employee employee = new Employee();

         employee.setDepartment("TI");
         employee.setEmail("samuelsantos20@gmail.com");
         employee.setPosition("ESTAGIARIO");
         employee.setFirstName("Samuel");
         employee.setLastName("Santos Miranda");

         employeeService.employeeSave(employee);


    }

}