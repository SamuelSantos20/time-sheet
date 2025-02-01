package io.github.samuelsantos20.time_sheet.validation;

import io.github.samuelsantos20.time_sheet.data.EmployeeData;
import io.github.samuelsantos20.time_sheet.exception.DuplicateRecord;
import io.github.samuelsantos20.time_sheet.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class EmployeeValidation {

    private final EmployeeData employeeData;

    public void validation(Employee employee) {

        if (employee == null) {

            throw new IllegalArgumentException("O valor enviado é null");
        }

        if (existsEmployee(employee)) {

            throw new DuplicateRecord("Estes dados já se encontro registrados no sistemas!");

        }

    }

    private boolean existsEmployee(Employee employee) {

        Optional<Employee> optionalEmployee = employeeData.entitySelection(employee.getEmail(), employee.getFirstName(),employee.getLastName());

        if (employee.getId() == null) {

            return optionalEmployee.isPresent();
        }

        optionalEmployee.map(Employee::getId).stream().anyMatch(id -> !id.equals(employee.getId()));
        return false;

    }


}
