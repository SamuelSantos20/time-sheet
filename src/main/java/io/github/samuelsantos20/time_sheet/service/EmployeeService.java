package io.github.samuelsantos20.time_sheet.service;

import io.github.samuelsantos20.time_sheet.data.EmployeeData;
import io.github.samuelsantos20.time_sheet.model.Employee;
import io.github.samuelsantos20.time_sheet.validation.EmployeeValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class EmployeeService {

    private final EmployeeData employeeData;

    private final EmployeeValidation employeeValidation;

    public Employee employeeSave(Employee employee) {

        employeeValidation.validation(employee);

        return employeeData.save(employee);

    }

    @Transactional(readOnly = true)
    public Optional<Employee> employeeSearch(UUID id) {

        return employeeData.findById(id);

    }

    @Transactional(readOnly = true)
    public List<Employee> employeeListString() {

        return employeeData.findAll();


    }


    public void employeeUpdate(Employee employee) {

        employeeValidation.validation(employee);

        employeeData.save(employee);

    }

    public void employeeDelete(Employee employee) {

        employeeData.delete(employee);

    }


}
