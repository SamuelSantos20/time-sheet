package io.github.samuelsantos20.time_sheet.mapper;

import io.github.samuelsantos20.time_sheet.dto.ApprovalDTO;
import io.github.samuelsantos20.time_sheet.model.Approval;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ApprovalMapper {
    Approval toEntity(ApprovalDTO approvalDTO);

    ApprovalDTO toDto(Approval approval);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Approval partialUpdate(ApprovalDTO approvalDTO, @MappingTarget Approval approval);
}