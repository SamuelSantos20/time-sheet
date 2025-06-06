package io.github.samuelsantos20.time_sheet.mapper;

import io.github.samuelsantos20.time_sheet.dto.TimesheetDTO;
import io.github.samuelsantos20.time_sheet.model.Timesheet;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TimesheetMapper {
    Timesheet toEntity(TimesheetDTO timesheetDTO);

    @AfterMapping
    default void linkWorkEntries(@MappingTarget Timesheet timesheet) {
        timesheet.getWorkEntries().forEach(workEntry -> workEntry.setTimesheetId(timesheet));
    }

    TimesheetDTO toDto(Timesheet timesheet);


    List<TimesheetDTO> TIMESHEET_DTOS(List<Timesheet> timesheets);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Timesheet partialUpdate(TimesheetDTO timesheetDTO, @MappingTarget Timesheet timesheet);
}