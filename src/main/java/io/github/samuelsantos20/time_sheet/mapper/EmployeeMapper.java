package io.github.samuelsantos20.time_sheet.mapper;

import io.github.samuelsantos20.time_sheet.dto.EmployeeDTO;
import io.github.samuelsantos20.time_sheet.model.Employee;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeMapper {
    Employee toEntity(EmployeeDTO employeeDTO);

    @AfterMapping
    default void linkTimesheets(@MappingTarget Employee employee) {
        employee.getTimesheets().forEach(timesheet -> timesheet.setEmployee(employee));
    }

    EmployeeDTO toDto(Employee employee);

    List<EmployeeDTO> toEntityList(List<Employee> employees);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Employee partialUpdate(EmployeeDTO employeeDTO, @MappingTarget Employee employee);
}