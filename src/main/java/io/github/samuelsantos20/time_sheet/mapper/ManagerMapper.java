package io.github.samuelsantos20.time_sheet.mapper;

import io.github.samuelsantos20.time_sheet.dto.ManagerDTO;
import io.github.samuelsantos20.time_sheet.model.Manager;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ManagerMapper {
    Manager toEntity(ManagerDTO managerDTO);

    ManagerDTO toDto(Manager manager);

    List<ManagerDTO> toManagerList(List<Manager> managers);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Manager partialUpdate(ManagerDTO managerDTO, @MappingTarget Manager manager);
}