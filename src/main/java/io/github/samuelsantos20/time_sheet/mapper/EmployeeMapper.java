package io.github.samuelsantos20.time_sheet.mapper;

import io.github.samuelsantos20.time_sheet.dto.EmployeeDTO;
import io.github.samuelsantos20.time_sheet.model.Employee;
import io.github.samuelsantos20.time_sheet.model.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeMapper {
    Employee toEntity(EmployeeDTO employeeDTO);

    @AfterMapping
    default void linkTimesheets(@MappingTarget User userId) {
        userId.getTimesheets().forEach(timesheet -> timesheet.setUserId(userId));
    }

    EmployeeDTO toDto(Employee employee);

    List<EmployeeDTO> toEntityList(List<Employee> employees);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Employee partialUpdate(EmployeeDTO employeeDTO, @MappingTarget Employee employee);
}