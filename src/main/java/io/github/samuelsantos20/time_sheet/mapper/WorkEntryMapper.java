package io.github.samuelsantos20.time_sheet.mapper;

import io.github.samuelsantos20.time_sheet.dto.WorkEntryDTO;
import io.github.samuelsantos20.time_sheet.model.WorkEntry;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface WorkEntryMapper {
    WorkEntry toEntity(WorkEntryDTO workEntryDTO);

    WorkEntryDTO toDto(WorkEntry workEntry);

    List<WorkEntry> toEntityList(List<WorkEntryDTO> workEntryDTOs);

    List<WorkEntryDTO> toDtoList(List<WorkEntry> workEntries);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    WorkEntry partialUpdate(WorkEntryDTO workEntryDTO, @MappingTarget WorkEntry workEntry);
}