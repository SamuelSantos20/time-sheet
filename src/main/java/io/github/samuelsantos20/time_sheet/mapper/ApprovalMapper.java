package io.github.samuelsantos20.time_sheet.mapper;

import io.github.samuelsantos20.time_sheet.data.TimesheetData;
import io.github.samuelsantos20.time_sheet.data.UserData;
import io.github.samuelsantos20.time_sheet.dto.ApprovalDTO;
import io.github.samuelsantos20.time_sheet.model.Approval;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ApprovalMapper {

    @Autowired
    private TimesheetData timesheetData;

    @Autowired
    private UserData userData;

    public Approval toEntity(ApprovalDTO approvalDTO){

        Approval approval = new Approval();

        if (approval!=null){

            System.out.println(approvalDTO.idUser());

            approval.setTimesheet(timesheetData.findById(approvalDTO.idTimesheet()).orElse(null));
            approval.setUser(userData.findById(approvalDTO.idUser()).orElse(null));
            approval.setComments(approvalDTO.comments());
            approval.setApprovalStatus(approvalDTO.approvalStatus());
        }

        return approval;
    }

    public abstract ApprovalDTO toDto(Approval approval);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Approval partialUpdate(ApprovalDTO approvalDTO, @MappingTarget Approval approval);
}