package io.github.samuelsantos20.time_sheet.mapper;

import io.github.samuelsantos20.time_sheet.dto.ManagerDTO;
import io.github.samuelsantos20.time_sheet.model.Manager;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ManagerMapper {
    Manager toEntity(ManagerDTO managerDTO);

    ManagerDTO toDto(Manager manager);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Manager partialUpdate(ManagerDTO managerDTO, @MappingTarget Manager manager);
}